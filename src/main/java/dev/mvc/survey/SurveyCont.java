package dev.mvc.survey;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import dev.mvc.classify.ClassifyProcInter;
import dev.mvc.classify.ClassifyVO;
import dev.mvc.classify.ClassifyVOMenu;
import dev.mvc.exchange.ExchangeProcInter;
import dev.mvc.genre.GenreProcInter;
import dev.mvc.genre.GenreVOMenu;
import dev.mvc.member.MemberProcInter;
import dev.mvc.tool.Tool;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@RequestMapping(value = "/survey")
@Controller
public class SurveyCont {
  @Autowired
  @Qualifier("dev.mvc.classify.ClassifyProc") // @Service("dev.mvc.member.MemberProc")
  private ClassifyProcInter classifyProc;

  @Autowired
  @Qualifier("dev.mvc.member.MemberProc") // @Service("dev.mvc.member.MemberProc")
  private MemberProcInter memberProc;

  @Autowired
  @Qualifier("dev.mvc.survey.SurveyProc") // @Component("dev.mvc.survey.SurveyProc")
  private SurveyProcInter surveyProc;

  @Autowired
  @Qualifier("dev.mvc.exchange.ExchangeProc") // @Component("dev.mvc.exchange.ExchangeProc")
  private ExchangeProcInter exchangeProc;
  @Autowired
  @Qualifier("dev.mvc.genre.GenreProc") // @Component("dev.mvc.exchange.ExchangeProc")
  private GenreProcInter genreProc;

  public SurveyCont() {
    System.out.println("-> SurveyCont created.");
  }

  @GetMapping("/create")
  public String create(Model model, HttpSession session) {
    ArrayList<ClassifyVOMenu> menu = this.classifyProc.menu(); // 중분류
    model.addAttribute("menu", menu);
    ArrayList<GenreVOMenu> menu1 = this.genreProc.menu(); // 대분류
    model.addAttribute("menu1", menu1);

    SurveyVO surveyVO = new SurveyVO();
    model.addAttribute("surveyVO", surveyVO);
    System.out.println(" -> Model Test [ SurveyCont.java ] ");
    model.addAttribute("currentDate", LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
    System.out.println("session : " + session);
    return "/survey/create";
  }

  @PostMapping(value = "/create")
  public String create(Model model, @Valid SurveyVO surveyVO, BindingResult bindingResult) {
    System.out.println(" -> create post [ survey/msg ]");
    if (bindingResult.hasErrors()) {
      System.out.println(" -> Error 에러 발생  [ survey/msg ]");
      return "/survey/create";
    }
    System.out.println(" getSurveyno : " + surveyVO.getSurveyno());
    System.out.println(" getTopic : " + surveyVO.getTopic());
    System.out.println(" getStartdate : " + surveyVO.getStartdate());
    System.out.println(" getEnddate : " + surveyVO.getEnddate());
    System.out.println(" getContinueyn : " + surveyVO.getContinueyn());

    int cnt = this.surveyProc.create(surveyVO);
    System.out.println(" -> cnt [surveyCont]: " + cnt);

    if (cnt == 1) {
//      model.addAttribute("code", "create_success");
//      model.addAttribute("name", surveyVO.getName());
      return "redirect:/survey/list_by_survey"; // @GetMapping(value = "/list_search")
    } else {
      model.addAttribute("code", "create_fail");
    }

    model.addAttribute("cnt", cnt);

    return "/survey/msg"; // templates/survey/msg.html
  }

  @GetMapping(value = "/list_by_survey")
  public String list_by_classifyno_search_paging(HttpSession session, Model model,
      @RequestParam(name = "word", defaultValue = "") String word,
      @RequestParam(name = "now_page", defaultValue = "1") int now_page) {

    ArrayList<ClassifyVOMenu> menu = this.classifyProc.menu();
    model.addAttribute("menu", menu);

    ArrayList<GenreVOMenu> menu1 = this.genreProc.menu(); // 대분류
    model.addAttribute("menu1", menu1);

//    SurveyVO surveyVO = this.surveyProc.read(14);
//    model.addAttribute("surveyVO", surveyVO);

//    ClassifyVO classifyVO = this.classifyProc.read(classifyno);
//    model.addAttribute("classifyVO", classifyVO);

    word = Tool.checkNull(word).trim();

    HashMap<String, Object> map = new HashMap<>();
//    map.put("classifyno", classifyno);
    map.put("word", word);
    map.put("now_page", now_page);

    ArrayList<SurveyVO> list = this.surveyProc.list_search_paging(word, now_page, Survey.RECORD_PER_PAGE);
    model.addAttribute("list", list);
//     System.out.println("-> size: " + list.size());
//    System.out.println(list);
    model.addAttribute("word", word);

    int search_count = this.surveyProc.list_by_classifyno_search_count(map);
    String paging = this.surveyProc.pagingBox(now_page, word, "/survey/list_by_surveyno", search_count,
        Survey.RECORD_PER_PAGE, Survey.PAGE_PER_BLOCK);
    model.addAttribute("paging", paging);
    model.addAttribute("now_page", now_page);

    model.addAttribute("search_count", search_count);

    // 일련 변호 생성: 레코드 갯수 - ((현재 페이지수 -1) * 페이지당 레코드 수)
    int no = search_count - ((now_page - 1) * Survey.RECORD_PER_PAGE);
    model.addAttribute("no", no);

    int search_cnt = this.surveyProc.list_search_count(word);

    model.addAttribute("search_cnt", search_cnt);
    model.addAttribute("word", word);

    return "/survey/list_by_classifyno_search_paging"; // /templates/Survey/list_by_classifyno_search_paging.html
  }

  /**
   * 조회 http://localhost:9091/survey/read?surveyno=17
   * 
   * @return
   */
  @GetMapping(value = "/read")
  public String read(Model model, @RequestParam(name = "surveyno", defaultValue = "0") int surveyno,
      @RequestParam(name = "word", defaultValue = "") String word,
      @RequestParam(name = "now_page", defaultValue = "1") int now_page) { // int productid =
                                                                           // (int)request.getParameter("productid");
    ArrayList<ClassifyVOMenu> menu = this.classifyProc.menu();
    model.addAttribute("menu", menu);

    ArrayList<GenreVOMenu> menu1 = this.genreProc.menu(); // 대분류
    model.addAttribute("menu1", menu1);

    SurveyVO surveyVO = this.surveyProc.read(surveyno);
    model.addAttribute("surveyVO", surveyVO);
    List<SurveyVO> surveyitemVO = this.surveyProc.read_item_list(surveyno); // 여러 설문 항목을 리스트로 받아오기
    model.addAttribute("surveyitemVO", surveyitemVO);

//    String title = surveyVO.getTitle();
//    String content = surveyVO.getContent();
//    
//    title = Tool.convertChar(title);  // 특수 문자 처리
//    content = Tool.convertChar(content); 
//    
//    surveyVO.setTitle(title);
//    surveyVO.setContent(content);  

//    long size1 = surveyVO.getPostersize();
//    String size1_label = Tool.unit(size1);
//    surveyVO.setSize1_label(size1_label);

//    SurveyVO surveyVO = this.surveyProc.read(surveyVO.getSurveyno());

    // 조회에서 화면 하단에 출력
    // ArrayList<ReplyVO> reply_list =
    // this.replyProc.list_survey(surveyno);
    // mav.addObject("reply_list", reply_list);

    model.addAttribute("word", word);
    model.addAttribute("now_page", now_page);

    return "survey/read";
  }

  /**
   * 조회 http://localhost:9091/survey/read?surveyno=17
   * 
   * @return
   */
  @GetMapping(value = "/dosurvey")
  public String dosurvey(Model model, @RequestParam(name = "surveyno", defaultValue = "0") int surveyno,
      @RequestParam(name = "word", defaultValue = "") String word,
      @RequestParam(name = "now_page", defaultValue = "1") int now_page) {
    ArrayList<ClassifyVOMenu> menu = this.classifyProc.menu();
    model.addAttribute("menu", menu);

    ArrayList<GenreVOMenu> menu1 = this.genreProc.menu(); // 대분류
    model.addAttribute("menu1", menu1);

    SurveyVO surveyVO = this.surveyProc.read(surveyno);
    model.addAttribute("surveyVO", surveyVO);

    List<SurveyVO> surveyitemVO = this.surveyProc.read_item_list(surveyno); // 여러 설문 항목을 리스트로 받아오기
    model.addAttribute("surveyitemVO", surveyitemVO);
    System.out.println(surveyitemVO);

    model.addAttribute("word", word);
    model.addAttribute("now_page", now_page);
    return "/survey/dosurvey";
  }

  @PostMapping(value = "/dosurvey")
  public String dosurvey_post(Model model, @RequestParam(name = "surveyno", defaultValue = "0") int surveyno,
      @RequestParam(name = "word", defaultValue = "") String word,
      @RequestParam(name = "now_page", defaultValue = "1") int now_page, @Valid SurveyVO surveyitemVO,
      BindingResult bindingResult, @ModelAttribute("surveyitemVO") ArrayList<SurveyVO> surveyItems) {

    ArrayList<ClassifyVOMenu> menu = this.classifyProc.menu();
    model.addAttribute("menu", menu);

    ArrayList<GenreVOMenu> menu1 = this.genreProc.menu(); // 대분류
    model.addAttribute("menu1", menu1);
    System.out.println("dosurvey_post 생성");

    System.out.println("surveyitemVO getSurveyitemno : " + surveyitemVO.getSurveyitemno());
    System.out.println("surveyitemVOgetPick : " + surveyitemVO.getPick());
    this.surveyProc.update_pick_surveyitem(surveyitemVO.getPick());

    return "redirect:/survey/list_by_survey";
  }

  @GetMapping("/create_item")
  public String create_item(Model model, HttpSession session,
      @RequestParam(name = "surveyno", defaultValue = "0") int surveyno) {
    ArrayList<ClassifyVOMenu> menu = this.classifyProc.menu(); // 중분류
    model.addAttribute("menu", menu);
    ArrayList<GenreVOMenu> menu1 = this.genreProc.menu(); // 대분류
    model.addAttribute("menu1", menu1);

    SurveyVO surveyVO = new SurveyVO();
    model.addAttribute("surveyVO", surveyVO);
    System.out.println(" -> Model Test [ SurveyCont.java ] ");
    model.addAttribute("currentDate", LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
    model.addAttribute("surveyno", surveyno);

    return "/survey/create_item";
  }

  @PostMapping(value = "/create_item")
  public String create_item(Model model, @Valid SurveyVO surveyVO, BindingResult bindingResult,
      @RequestParam(name = "surveyno", defaultValue = "0") int surveyno) {
    System.out.println(" -> create post [ survey/msg ]");
    if (bindingResult.hasErrors()) {
      System.out.println(" -> Error 에러 발생  [ survey/msg ]");
      return "/survey/create_item";
    }
    System.out.println(" getContinueyn : " + surveyVO.getContinueyn());
    System.out.println("surveyno : " + surveyno);
    int cnt = this.surveyProc.create_item(surveyVO);
    System.out.println(" -> cnt [surveyCont]: " + cnt);

    if (cnt == 1) {
//      model.addAttribute("code", "create_success");
//      model.addAttribute("name", surveyVO.getName());
      return "redirect:/survey/list_by_survey"; // @GetMapping(value = "/list_search")
    } else {
      model.addAttribute("code", "create_fail");
    }

    model.addAttribute("cnt", cnt);

    return "/survey/msg"; // templates/survey/msg.html
  }

  @GetMapping(value = "/update/{surveyno}")
  public String update(Model model, @PathVariable("surveyno") Integer surveyno,
      @RequestParam(name = "word", defaultValue = "") String word,
      @RequestParam(name = "now_page", defaultValue = "1") int now_page, HttpSession session) {
    ArrayList<ClassifyVOMenu> menu = this.classifyProc.menu(); // 중분류
    model.addAttribute("menu", menu);
    ArrayList<GenreVOMenu> menu1 = this.genreProc.menu(); // 대분류
    model.addAttribute("menu1", menu1);
    model.addAttribute("word", word);
    if (this.memberProc.isMemberAdmin(session)) {

      SurveyVO surveyVO = this.surveyProc.read(surveyno);
      model.addAttribute("surveyVO", surveyVO);
      List<SurveyVO> surveyitemVO = this.surveyProc.read_item_list(surveyno); // 여러 설문 항목을 리스트로 받아오기
      model.addAttribute("surveyitemVO", surveyitemVO);

      return "/survey/update";
    } else {
      return "redirect:/member/login_cookie_need"; // redirect
    }
  }

  /**
   * 
   * @param model         Controller -> Thymeleaf HTML로 데이터 전송
   * @param cateVO        Form 태그 값 -> 검증 -> cateVO 자동저장, request.getParameter()
   *                      자동 실행
   * @param bindingResult 폼에 에러가 있는지 검사 지원
   * @return
   */
  @PostMapping(value = "/update")
  public String update(Model model, @Valid @ModelAttribute("surveyVO") SurveyVO surveyVO, BindingResult bindingResult,
      @RequestParam(name = "word", defaultValue = "") String word, RedirectAttributes ra,
      @RequestParam(name = "now_page", defaultValue = "1") int now_page, HttpSession session) {
    System.out.println(" -> update post [ surveyVO/update ]");
    System.out.println(" 주제 : " + surveyVO.getTopic());

    if (this.memberProc.isMemberAdmin(session)) {
      if (bindingResult.hasErrors()) {
        System.out.println(" -> Error 발생  [ surveyVO/update ]");
        model.addAttribute("surveyVO", surveyVO); // 에러 발생 시 데이터 유지
        return "/classify/update";
      }

      // 업데이트 로직
      int cnt = this.surveyProc.update(surveyVO);
      System.out.println(" -> cnt [classifyCont]: " + cnt);

      if (cnt == 1) {
        model.addAttribute("code", "update_success");
        ra.addAttribute("word", word);
        ra.addAttribute("now_page", now_page);
        return "redirect:/survey/list_by_survey";
      } else {
        model.addAttribute("code", "update_fail");
      }

      model.addAttribute("cnt", cnt);
      return "/classify/msg"; // templates/classify/msg.html
    } else {
      return "redirect:/member/login_cookie_need"; // redirect
    }
  }

  @GetMapping(value = "/delete/{surveyno}")
  public String delete(Model model, @PathVariable("surveyno") Integer surveyno,
      @RequestParam(name = "word", defaultValue = "") String word,
      @RequestParam(name = "now_page", defaultValue = "1") int now_page, HttpSession session) {
    ArrayList<ClassifyVOMenu> menu = this.classifyProc.menu(); // 중분류
    model.addAttribute("menu", menu);
    ArrayList<GenreVOMenu> menu1 = this.genreProc.menu(); // 대분류
    model.addAttribute("menu1", menu1);
    model.addAttribute("word", word);
    if (this.memberProc.isMemberAdmin(session)) {

      SurveyVO surveyVO = this.surveyProc.read(surveyno);
      model.addAttribute("surveyVO", surveyVO);
      List<SurveyVO> surveyitemVO = this.surveyProc.read_item_list(surveyno); // 여러 설문 항목을 리스트로 받아오기
      model.addAttribute("surveyitemVO", surveyitemVO);

      return "/survey/delete";
    } else {
      return "redirect:/member/login_cookie_need"; // redirect
    }
  }

  /**
   * 
   * @param model         Controller -> Thymeleaf HTML로 데이터 전송
   * @param cateVO        Form 태그 값 -> 검증 -> cateVO 자동저장, request.getParameter()
   *                      자동 실행
   * @param bindingResult 폼에 에러가 있는지 검사 지원
   * @return
   */
  @PostMapping(value = "/delete")
  public String delete(Model model, @Valid @ModelAttribute("surveyVO") SurveyVO surveyVO, BindingResult bindingResult,
      @RequestParam(name = "word", defaultValue = "") String word, RedirectAttributes ra,
      @RequestParam(name = "now_page", defaultValue = "1") int now_page, HttpSession session) {
    System.out.println(" -> delete post [ surveyVO/update ]");
    System.out.println(" 주제 : " + surveyVO.getTopic());
    System.out.println(" 설문 번호 : " + surveyVO.getSurveyno());

    if (this.memberProc.isMemberAdmin(session)) {
      if (bindingResult.hasErrors()) {
        System.out.println(" -> Error 발생  [ surveyVO/update ]");
        model.addAttribute("surveyVO", surveyVO); // 에러 발생 시 데이터 유지
        return "/survey/delete";
      }

      // 업데이트 로직
      int cnt = this.surveyProc.delete(surveyVO);
      System.out.println(" -> cnt [classifyCont]: " + cnt);

      if (cnt == 1) {
        model.addAttribute("code", "delete_success");
        ra.addAttribute("word", word);
        ra.addAttribute("now_page", now_page);
        return "redirect:/survey/list_by_survey";
      } else {
        model.addAttribute("code", "update_fail");
      }

      model.addAttribute("cnt", cnt);
      return "/classify/msg"; // templates/classify/msg.html
    } else {
      return "redirect:/member/login_cookie_need"; // redirect
    }
  }
}