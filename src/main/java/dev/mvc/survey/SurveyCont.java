package dev.mvc.survey;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import dev.mvc.classify.ClassifyProcInter;
import dev.mvc.classify.ClassifyVOMenu;
import dev.mvc.exchange.ExchangeProcInter;
import dev.mvc.genre.GenreProcInter;
import dev.mvc.genre.GenreVOMenu;
import dev.mvc.member.MemberProcInter;
import dev.mvc.survey_good.Survey_goodProcInter;
import dev.mvc.survey_good.Survey_goodVO;
import dev.mvc.tool.Tool;
import dev.mvc.tool.Upload;
import jakarta.servlet.http.HttpServletRequest;
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
  @Qualifier("dev.mvc.survey_good.Survey_goodProc") // @Component("dev.mvc.survey.SurveyProc")
  private Survey_goodProcInter survey_goodProc;

  @Autowired
  @Qualifier("dev.mvc.exchange.ExchangeProc") // @Component("dev.mvc.exchange.ExchangeProc")
  private ExchangeProcInter exchangeProc;
  @Autowired
  @Qualifier("dev.mvc.genre.GenreProc") // @Component("dev.mvc.exchange.ExchangeProc")
  private GenreProcInter genreProc;

  public SurveyCont() {
    System.out.println("-> SurveyCont created.");
  }

  /**
   * 설문조사 생성 겟
   * 
   * @param model
   * @param session
   * @return
   */
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
//    System.out.println("session : " + session);
    return "/survey/create";
  }

  /**
   * 설문조사 생성 포스트
   * 
   * @param request
   * @param session
   * @param model
   * @param surveyVO
   * @param bindingResult
   * @param ra
   * @return
   */
  @PostMapping(value = "/create")
  public String create(HttpServletRequest request, HttpSession session, Model model, @Valid SurveyVO surveyVO,
      BindingResult bindingResult, RedirectAttributes ra) {
    System.out.println(" -> create post [ survey/msg ]");
    if (bindingResult.hasErrors()) {
      System.out.println(" -> Error 에러 발생  [ survey/msg ]");
      bindingResult.getAllErrors().forEach(error -> System.out.println(error.toString()));
      return "/survey/create";
    }
//    System.out.println(" getSurveyno : " + surveyVO.getSurveyno());
//    System.out.println(" getTopic : " + surveyVO.getTopic());
//    System.out.println(" getStartdate : " + surveyVO.getStartdate());
//    System.out.println(" getEnddate : " + surveyVO.getEnddate());
//    System.out.println(" getContinueyn : " + surveyVO.getContinueyn());

    if (memberProc.isMemberAdmin(session)) { // 관리자로 로그인한경우
      // 파일 전송 코드 시작
      String poster = ""; // 원본 파일명 image
      String postersaved = ""; // 저장된 파일명, image
      String posterthumb = ""; // preview image

      String upDir = Survey.getUploadDir(); // 파일을 업로드할 폴더 준비
      // upDir = upDir + "/" + 한글을 제외한 카테고리 이름
      System.out.println("-> upDir: " + upDir);

      // 전송 파일이 없어도 posterMF 객체가 생성됨.
      // <input type='file' class="form-control" name='posterMF' id ='posterMF'
      // value='' placeholder="파일 선택">
      MultipartFile mf = surveyVO.getFile1MF();

      poster = mf.getOriginalFilename(); // 원본 파일명 산출, 01.jpg
      System.out.println("-> 원본 파일명 산출 poster: " + poster);

      long size = mf.getSize(); // 파일 크기
      if (size > 0) { // 파일 크기 체크, 파일을 올리는 경우
        if (Tool.checkUploadFile(poster) == true) { // 업로드 가능한 파일인지 검사
          // 파일 저장 후 업로드된 파일명이 리턴됨, spring.jsp, spring_1.jpg, spring_2.jpg...
          postersaved = Upload.saveFileSpring(mf, upDir);

          if (Tool.isImage(postersaved)) { // 이미지인지 검사
            // thumb 이미지 생성후 파일명 리턴됨, width: 200, height: 150
            posterthumb = Tool.preview(upDir, postersaved, 200, 150);
          }

          surveyVO.setPoster(poster); // 순수 원본 파일명
          surveyVO.setPostersaved(postersaved); // 저장된 파일명(파일명 중복 처리)
          surveyVO.setPosterthumb(posterthumb); // 원본이미지 축소판
          surveyVO.setPostersize(size); // 파일 크기

        } else { // 전송 못하는 파일 형식
          ra.addFlashAttribute("code", "check_upload_file_fail"); // 업로드 할 수 없는 파일
          ra.addFlashAttribute("cnt", 0); // 업로드 실패
          ra.addFlashAttribute("url", "/survey/msg"); // msg.html, redirect parameter 적용
          return "redirect:/survey/msg"; // Post -> Get - param...
        }
      } else { // 글만 등록하는 경우
        System.out.println("-> 글만 등록");
      }
    }
    // 파일 전송 코드 끝
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

  /**
   * 설문조사 전체 리스트 조회
   * 
   * @param session
   * @param model
   * @param word
   * @param now_page
   * @return
   */
  @GetMapping(value = "/list_by_survey")
  public String list_by_classifyno_search_paging(HttpSession session, Model model,
      @RequestParam(name = "word", defaultValue = "") String word,
      @RequestParam(name = "now_page", defaultValue = "1") int now_page) {
    ArrayList<ClassifyVOMenu> menu = this.classifyProc.menu();
    model.addAttribute("menu", menu);

    ArrayList<GenreVOMenu> menu1 = this.genreProc.menu(); // 대분류
    model.addAttribute("menu1", menu1);

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
//    String paging = this.surveyProc.pagingBox(now_page, word, "/survey/list_by_surveyno", search_count,
//        Survey.RECORD_PER_PAGE, Survey.PAGE_PER_BLOCK);
//    model.addAttribute("paging", paging);
    model.addAttribute("now_page", now_page);

    model.addAttribute("search_count", search_count);

    // 일련 변호 생성: 레코드 갯수 - ((현재 페이지수 -1) * 페이지당 레코드 수)
//    int no = search_count - ((now_page - 1) * Survey.RECORD_PER_PAGE);
//    model.addAttribute("no", no);

    int search_cnt = this.surveyProc.list_search_count(word);

    model.addAttribute("search_cnt", search_cnt);

    return "/survey/list_by_classifyno_search_paging"; // /templates/Survey/list_by_classifyno_search_paging.html
  }

  /**
   * 설문조사 결과 조회 http://localhost:9091/survey/read?surveyno=17
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
    int cntsum = surveyitemVO.stream().mapToInt(SurveyVO::getItemcnt) // 각 SurveyVO 객체의 itemcnt 값을 int로 추출
        .sum(); // 전체 합을 구함
//    System.out.println("cntsum : "+ cntsum);
//    for(int i : surveyitemVO) {
//      
//    }
    model.addAttribute("word", word);
    model.addAttribute("now_page", now_page);
    model.addAttribute("cntsum", cntsum);

    return "survey/read";
  }

  /**
   * 설문조사 조회 http://localhost:9091/survey/read?surveyno=17
   * 
   * @return
   */
  @GetMapping(value = "/dosurvey")
  public String dosurvey(HttpSession session, Model model,
      @RequestParam(name = "surveyno", defaultValue = "0") int surveyno,
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
//    System.out.println(surveyitemVO);

    model.addAttribute("word", word);
    model.addAttribute("now_page", now_page);

    // 추천 기능 시작
    HashMap<String, Object> map = new HashMap<>();
    map.put("surveyno", surveyno);

    int hartCnt = 0;
    if (session.getAttribute("memberno") != null) { // 로그인 상태일 때
      int memberno = (int) session.getAttribute("memberno");
      model.addAttribute("memberno", memberno); // 모델에 추가
      map.put("memberno", memberno);
      System.out.println("memberno : " + memberno);
      hartCnt = this.survey_goodProc.hartCnt(map);
      System.out.println("hartCnt : " + hartCnt);
    }
//    System.out.println("surveyVO.getRecom() : " + surveyVO.getRecom());
    model.addAttribute("hartCnt", hartCnt); // 모델에 추가
    // 추천 기능 끝

    long size = surveyVO.getPostersize();
    String size_label = Tool.unit(size);
    surveyVO.setSize_label(size_label);

    return "/survey/dosurvey";
  }

  /**
   * 설문조사 완료
   * 
   * @param session
   * @param model
   * @param surveyno
   * @param word
   * @param now_page
   * @param surveyitemVO
   * @param bindingResult
   * @param surveyItems
   * @return
   * 
   */
  @PostMapping(value = "/dosurvey")
  public String dosurvey_post(HttpSession session, Model model,
      @RequestParam(name = "surveyno", defaultValue = "0") int surveyno,
      @RequestParam(name = "word", defaultValue = "") String word,
      @RequestParam(name = "now_page", defaultValue = "1") int now_page, @Valid SurveyVO surveyitemVO,
      BindingResult bindingResult, @ModelAttribute("surveyitemVO") ArrayList<SurveyVO> surveyItems) {

    ArrayList<ClassifyVOMenu> menu = this.classifyProc.menu();
    model.addAttribute("menu", menu);

    ArrayList<GenreVOMenu> menu1 = this.genreProc.menu(); // 대분류
    model.addAttribute("menu1", menu1);
    if (this.memberProc.isMember(session)) {
      System.out.println("dosurvey_post 생성");

//      System.out.println("surveyitemVO getSurveyitemno : " + surveyitemVO.getSurveyitemno());
//      System.out.println("surveyitemVOgetPick : " + surveyitemVO.getPick()); // = Surveyitemno 선택한 번호
//      System.out.println("surveyitemVO.getSurveyno : " + surveyitemVO.getSurveyno());
//      System.out.println("surveyitemVO.toString : " + surveyitemVO.toString());

      int memberno = (int) session.getAttribute("memberno");
      HashMap<String, Object> map = new HashMap<String, Object>();
      map.put("surveyno", surveyno);
      map.put("memberno", memberno);
      map.put("surveyitemno", surveyitemVO.getPick());
      int cnt_sm = this.surveyProc.cnt_sm(map); // 여러 설문 항목을 리스트로 받아오기
//      System.out.println("출력 확인 " + this.surveyProc.cnt_sm(map));

      // 기존 투표내역을 불러와서
      SurveyVO smVO = this.surveyProc.read_sm(map);
//      System.out.println("smVO.toString() : " + smVO.getSurveymemberno());
//      System.out.println("smVO.toString() : " + smVO.toString());
      if (cnt_sm == 0) {
        this.surveyProc.create_sm(map);
        this.surveyProc.update_pick_surveyitem(surveyitemVO.getPick());
//        System.out.println("설문 완료");
        return "redirect:/survey/list_by_survey";
      } else {
//        System.out.println("중복 불가능");

        this.surveyProc.delete_sm(smVO.getSurveymemberno());
        // 내역 제거 및 픽다운
        this.surveyProc.create_sm(map);
        this.surveyProc.update_unpick_surveyitem(smVO.getSurveyitemno());
        this.surveyProc.update_pick_surveyitem(surveyitemVO.getPick());
        return "redirect:/survey/list_by_survey";
      }
    } else {
      return "redirect:/member/login_cookie_need";
    } // redirect
  }

  /**
   * 설문 항목 추가 겟
   * 
   * @param model
   * @param session
   * @param surveyno
   * @return
   */
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

  /**
   * 설문 항목 추가 포스트
   * 
   * @param model
   * @param surveyVO
   * @param bindingResult
   * @param surveyno
   * @return
   */
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
      model.addAttribute("code", "create_success");
//      model.addAttribute("name", surveyVO.getName());
      return "redirect:/survey/dosurvey?surveyno=" + surveyno; // @GetMapping(value = "/list_search")
    } else {
      model.addAttribute("code", "create_fail");
    }

    model.addAttribute("cnt", cnt);

    return "/survey/msg"; // templates/survey/msg.html
  }

  /**
   * 설문조사 수정 겟
   * 
   * @param model
   * @param surveyno
   * @param word
   * @param now_page
   * @param session
   * @return
   */
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
   * 설문조사 수정 포스트
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
        return "/survey/update";
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

  /**
   * 설문조사 삭제 (25.01.08 이후 사용 안함)
   * 
   * @param model
   * @param surveyno
   * @param word
   * @param now_page
   * @param session
   * @return
   */
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
   * 설문조사 삭제
   * 
   * @param model         Controller -> Thymeleaf HTML로 데이터 전송
   * @param cateVO        Form 태그 값 -> 검증 -> cateVO 자동저장, request.getParameter()
   *                      자동 실행
   * @param bindingResult 폼에 에러가 있는지 검사 지원
   * @return
   */
  @PostMapping(value = "/delete_s")
  public String delete_s(Model model, @Valid @ModelAttribute("surveyVO") SurveyVO surveyVO, BindingResult bindingResult,
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

      SurveyVO surveyVO_read = surveyProc.read(surveyVO.getSurveyno());

      String postersaved = surveyVO_read.getPostersaved();
      String posterthumb = surveyVO_read.getPosterthumb();

      String uploadDir = Survey.getUploadDir();
      Tool.deleteFile(uploadDir, postersaved); // 실제 저장된 파일삭제
      Tool.deleteFile(uploadDir, posterthumb); // preview 이미지 삭제

      // 업데이트 로직
      int cnt = this.surveyProc.delete(surveyVO);
      System.out.println(" -> cnt [classifyCont]: " + cnt);

      if (cnt == 1) {
        model.addAttribute("code", "delete_success");
        ra.addAttribute("word", word);
        ra.addAttribute("now_page", now_page);
        return "redirect:/survey/list_by_survey";
      } else {
        model.addAttribute("code", "delete_fail");
      }

      model.addAttribute("cnt", cnt);
      return "/classify/msg"; // templates/classify/msg.html
    } else {
      return "redirect:/member/login_cookie_need"; // redirect
    }
  }

  /**
   * 추천 AJax 포스트
   * 
   * @param session
   * @param model
   * @param json_src
   * @param ra
   * @return
   */
  @PostMapping(value = "/good")
  @ResponseBody
  public String good(HttpSession session, Model model, @RequestBody String json_src, RedirectAttributes ra) {
    System.out.println(" -> json_src : " + json_src);
    JSONObject src = new JSONObject(json_src);
    int surveyno = (int) src.getInt("surveyno");
    System.out.println("surveyno : " + surveyno);

    if (this.memberProc.isMember(session)) {
      // 추천을 한 상태
      int memberno = (int) session.getAttribute("memberno");
      HashMap<String, Object> map = new HashMap<String, Object>();
      map.put("surveyno", surveyno);
      map.put("memberno", memberno);
      int good_cnt = this.survey_goodProc.hartCnt(map);
      // System.out.println("good_cnt -> "+ good_cnt);

      if (good_cnt == 1) {
        // 추천 해제
        Survey_goodVO survey_goodVO = this.survey_goodProc.read_fk(map);

//        System.out.println("-> 설문 추천 해제 : "+ surveyno + "  " + memberno);
        this.survey_goodProc.delete(survey_goodVO.getSurveygoodno());
        this.surveyProc.decreaseRecom(surveyno);

      } else {
        // 추천
        // System.out.println("-> 설문 추천 : "+ surveyno + " " + memberno);
        Survey_goodVO Survey_goodVO_new = new Survey_goodVO();
        Survey_goodVO_new.setSurveyno(surveyno);
        Survey_goodVO_new.setMemberno(memberno);

        this.survey_goodProc.create(Survey_goodVO_new);
        this.surveyProc.increaseRecom(surveyno);
      }

      int hartCnt = this.survey_goodProc.hartCnt(map);
      int recom = this.surveyProc.read(surveyno).getRecom();
      // System.out.println("hartCnt : "+ hartCnt);

      JSONObject result = new JSONObject();
      result.put("isMember", 1);
      result.put("hartCnt", hartCnt);
      result.put("recom", recom);

      // System.out.println("result.toString() : "+ result.toString());
      return result.toString();
    } else {
      JSONObject result = new JSONObject();
      result.put("isMember", 0); // 로그인: 1, 비회원: 0
      // ra.addAttribute("url", "/member/login_cookie_need");
//      return "redirect:/survey/post2get"; // redirect
      return result.toString();
//      return "redirect:/member/login_cookie_need"; // redirect
    }
  }

  /**
   * 파일(사진) 수정 겟매핑
   * 
   * @param model
   * @param surveyno
   * @param word
   * @param now_page
   * @param session
   * @return
   */
  @GetMapping(value = "/update_file/{surveyno}")
  public String update_file(Model model, @PathVariable("surveyno") Integer surveyno,
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

      return "/survey/update_file";
    } else {
      return "redirect:/member/login_cookie_need"; // redirect
    }
  }

  /**
   * 파일(사진) 수정 포스트
   * 
   * @param session
   * @param model
   * @param ra
   * @param surveyVO
   * @param word
   * @param now_page
   * @return
   */
  @PostMapping(value = "/update_file")
  public String update_file(HttpSession session, Model model, RedirectAttributes ra,
      @ModelAttribute("surveyVO") SurveyVO surveyVO, @RequestParam(name = "word", defaultValue = "") String word,
      @RequestParam(name = "now_page", defaultValue = "1") int now_page) {

    if (this.memberProc.isMemberAdmin(session)) {
      // 삭제할 파일 정보를 읽어옴, 기존에 등록된 레코드 저장용
      SurveyVO surveyVO_old = surveyProc.read(surveyVO.getSurveyno());

      // -------------------------------------------------------------------
      // 파일 삭제 시작
      // -------------------------------------------------------------------
      String postersaved = surveyVO_old.getPostersaved(); // 실제 저장된 파일명
      String posterthumb = surveyVO_old.getPosterthumb(); // 실제 저장된 preview 이미지 파일명
      long size1 = 0;

      String upDir = Survey.getUploadDir(); // C:/kd/deploy/team4/survey/storage/

      Tool.deleteFile(upDir, postersaved); // 실제 저장된 파일삭제
      Tool.deleteFile(upDir, posterthumb); // preview 이미지 삭제
      // -------------------------------------------------------------------
      // 파일 삭제 종료
      // -------------------------------------------------------------------

      // -------------------------------------------------------------------
      // 파일 전송 시작
      // -------------------------------------------------------------------
      String poster = ""; // 원본 파일명 image

      // 전송 파일이 없어도 file1MF 객체가 생성됨.
      // <input type='file' class="form-control" name='file1MF' id='file1MF'
      // value='' placeholder="파일 선택">
      MultipartFile mf = surveyVO.getFile1MF();

      poster = mf.getOriginalFilename(); // 원본 파일명
      size1 = mf.getSize(); // 파일 크기

      if (size1 > 0) { // 폼에서 새롭게 올리는 파일이 있는지 파일 크기로 체크 ★
        // 파일 저장 후 업로드된 파일명이 리턴됨, spring.jsp, spring_1.jpg...
        postersaved = Upload.saveFileSpring(mf, upDir);

        if (Tool.isImage(postersaved)) { // 이미지인지 검사
          // thumb 이미지 생성후 파일명 리턴됨, width: 250, height: 200
          posterthumb = Tool.preview(upDir, postersaved, 250, 200);
        }

      } else { // 파일이 삭제만 되고 새로 올리지 않는 경우
        poster = "";
        postersaved = "";
        posterthumb = "";
        size1 = 0;
      }

      surveyVO.setPoster(poster);
      surveyVO.setPostersaved(postersaved);
      surveyVO.setPosterthumb(posterthumb);
      surveyVO.setPostersize(size1);
      // -------------------------------------------------------------------
      // 파일 전송 코드 종료
      // -------------------------------------------------------------------

      this.surveyProc.update_file(surveyVO); // Oracle 처리
      ra.addAttribute("surveyno", surveyVO.getSurveyno());
//      ra.addAttribute("word", word);
//      ra.addAttribute("now_page", now_page);

      return "redirect:/survey/list_by_survey";
    } else {
      ra.addAttribute("url", "/member/login_cookie_need");
      return "redirect:/survey/post2get"; // GET
    }
  }

  /**
   * 삭제 surveyitem 항목 제거
   * 
   * @param model
   * @param surveyVO
   * @param bindingResult
   * @param word
   * @param ra
   * @param now_page
   * @param session
   * @param surveyno
   * @return
   */
  @PostMapping(value = "/delete_item")
  public String delete_item(Model model, @Valid @ModelAttribute("surveyVO") SurveyVO surveyVO,
      BindingResult bindingResult, @RequestParam(name = "word", defaultValue = "") String word, RedirectAttributes ra,
      @RequestParam(name = "now_page", defaultValue = "1") int now_page, HttpSession session,
      @RequestParam(name = "surveyno", required = false) int surveyno) {
    System.out.println(" -> delete_item post [ surveyVO/update ]");
    System.out.println("surveyno : " + surveyno);

    if (this.memberProc.isMemberAdmin(session)) {
      if (bindingResult.hasErrors()) {
        System.out.println(" -> Error 발생  [ surveyVO/del ]");
        model.addAttribute("surveyVO", surveyVO); // 에러 발생 시 데이터 유지
//        System.out.println(" surveyVO.toString() : " + surveyVO.toString());
        bindingResult.getAllErrors().forEach(error -> {
          System.out.println("Error: " + error.getDefaultMessage());
        });
//        return "/survey/delete";
      }

//      SurveyVO surveyVO_read = surveyProc.read(surveyVO.getSurveyno());
//
//      String postersaved = surveyVO_read.getPostersaved();
//      String posterthumb = surveyVO_read.getPosterthumb();
//
//      String uploadDir = Survey.getUploadDir();
//      Tool.deleteFile(uploadDir, postersaved); // 실제 저장된 파일삭제
//      Tool.deleteFile(uploadDir, posterthumb); // preview 이미지 삭제

      // 삭제
      int cnt = this.surveyProc.delete_item(surveyVO.getSurveyitemno());
//      System.out.println(" -> cnt [surveyCont]: " + cnt);

      if (cnt == 1) {
        model.addAttribute("code", "delete_success");
        ra.addAttribute("word", word);
        ra.addAttribute("now_page", now_page);
        return "redirect:/survey/dosurvey?surveyno=" + surveyno;
      } else {
        model.addAttribute("code", "delete_success");
      }
      model.addAttribute("cnt", cnt);

      return "/classify/msg"; // templates/classify/msg.html
    } else {
      return "redirect:/member/login_cookie_need"; // redirect
    }
  }

  /**
   * 설문 참여자 리스트
   * 
   * @param model
   * @return
   */
  @GetMapping(value = "/list_all")
  public String list_all(Model model, @RequestParam(name = "word", defaultValue = "") String word,
      @RequestParam(name = "now_page", defaultValue = "1") int now_page) {
    ArrayList<ClassifyVOMenu> menu = this.classifyProc.menu(); // 중분류
    model.addAttribute("menu", menu);
    ArrayList<GenreVOMenu> menu1 = this.genreProc.menu(); // 대분류
    model.addAttribute("menu1", menu1);

    HashMap<String, Object> map = new HashMap<>();
//  map.put("classifyno", classifyno);
    map.put("word", word);
    map.put("now_page", now_page);
//    ArrayList<Survey_goodVO> list = this.survey_goodProc.list_all();
//    model.addAttribute("list", list);
    ArrayList<SurveyVO> list = this.surveyProc.list_all(map);
    model.addAttribute("list", list);
    System.out.println("list : " + list);

    
    
 // 프로젝트 목록 번호 생성
    String list_file_name = "/survey/list_all";
    int search_count = this.classifyProc.list_search_count(word);
    String paging = this.classifyProc.pagingBox(now_page, word, list_file_name, search_count, Survey.RECORD_PER_PAGE,
        Survey.PAGE_PER_BLOCK);
    model.addAttribute("paging", paging);
    model.addAttribute("now_page", now_page);
    // 일련 변호 생성: 레코드 갯수 - ((현재 페이지수 -1) * 페이지당 레코드 수)
    int no = search_count - ((now_page - 1) * Survey.RECORD_PER_PAGE);
    model.addAttribute("no", no);
    
    return "/survey/list_all"; // /templates/surveygood/list_all.html
  }

  /**
   * 삭제 surveymember
   * 
   * @param session
   * @param model
   * @param surveymemberno
   * @param ra
   * @return
   */
  @PostMapping(value = "/delete_sm")
  public String delete_sm(HttpSession session, Model model,
      @RequestParam(name = "surveymemberno", defaultValue = "0") int surveymemberno, RedirectAttributes ra) {
    if (this.memberProc.isMemberAdmin(session)) { // 관리자 로그인 확인
      this.surveyProc.delete_sm(surveymemberno);
      return "redirect:/survey/list_all";

    } else { // 정상적인 로그인이 아닌 경우 로그인 유도
      ra.addAttribute("url", "/member/login_cookie_need"); // /templates/member/login_cookie_need.html
      return "redirect:/survey/post2get"; // @GetMapping(value = "/msg")
    }
  }

}