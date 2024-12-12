package dev.mvc.classify;

import java.util.ArrayList;

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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import dev.mvc.member.MemberProcInter;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
//@RequestMapping("/classify")
@RequestMapping("/classify")
public class ClassifyCont {

  @Autowired
  @Qualifier("ClassifyProc")
  private ClassifyProcInter classifyProc;
  
  @Autowired
  @Qualifier("dev.mvc.member.MemberProc")
  private MemberProcInter memberProc;

  /** 페이지당 출력할 레코드 갯수, nowPage는 1부터 시작 */
  public int record_per_page = 10;

  /** 블럭당 페이지 수, 하나의 블럭은 10개의 페이지로 구성됨 */
  public int page_per_block = 10;

  /** 페이징 목록 주소 */
  private String list_file_name = "/classify/list_search";

  public ClassifyCont() {
    System.out.println(" -> ClassifyCont  created.");
  }

  @GetMapping(value = "/create1")
  @ResponseBody
  public String createForm() {
    return "<h2>Create test [ Classify Cont ] </h2>";
  }

  /**
   * 등록 화면
   * 
   * @param model
   * @return
   */
  @GetMapping(value = "/create") // http://localhost:9093/classify/create
  public String create(Model model) {

//      LocalDate today = LocalDate.now();
    ClassifyVO classifyVO = new ClassifyVO();
    model.addAttribute("classifyVO", classifyVO);
    System.out.println(" -> Model Test [ ClassifyCont.java ] ");
    return "/classify/create";
  }

  @PostMapping(value = "/create")
  public String create(Model model, @ModelAttribute ClassifyVO classifyVO, BindingResult bindingResult) {
    System.out.println(" -> create post [ classify/msg ]");

    if (bindingResult.hasErrors()) {
      System.out.println(" -> Error 에러 발생  [ classify/msg ]");
      return "/classify/create";
    }

    System.out.println("장르: " + classifyVO.getClassify());
    System.out.println("이름: " + classifyVO.getName());
    System.out.println("출력 순서 : " + classifyVO.getSeqno());
    System.out.println("출력 모드 : " + classifyVO.getVisible());
    System.out.println("추가 날짜 : " + classifyVO.getRdate()); // rdate 출력

    int cnt = this.classifyProc.create(classifyVO);
    System.out.println(" -> cnt [classifyCont]: " + cnt);

    if (cnt == 1) {
      System.out.println(" -> 72      cnt [classifyCont]: " + cnt);

      return "/classify/list_search";
    } else {
      model.addAttribute("code", "create_fail");
    }

    model.addAttribute("cnt", cnt);
    // 아직 안만듬
    return "/classify/msg"; // templates/classify/msg.html
  }

  /**
   * 전체 리스트 조회 http://localhost:9092/classify/create
   * 
   * @param model
   * @return
   */
  @GetMapping(value = "/list_all") // http://localhost:9093/classify/list_search
  public String list_all(Model model) {
    ClassifyVO classifyVO = new ClassifyVO();
//System.out.println("92 디버깅");
    // 카테고리 그룹 목록
    ArrayList<String> list_type = this.classifyProc.typeset();
    classifyVO.setClassify(String.join("/", list_type));

    model.addAttribute("classifyVO", classifyVO);

    ArrayList<ClassifyVO> list = this.classifyProc.list_all();
    model.addAttribute("list", list);

    ArrayList<ClassifyVOMenu> menu = this.classifyProc.menu();
    model.addAttribute("menu", menu);

    return "/classify/list_search";
  }

  /**
   * 삭제 http://localhost:9091/delete/update/1
   * 
   * @param model
   * @param classifyno
   * @return
   */
  @GetMapping(value = "/delete/{classifyno}")
  public String delete(Model model, @PathVariable("classifyno") Integer classifyno
//      , @RequestParam(name = "word", defaultValue = "") String word,
//      @RequestParam(name = "now_page", defaultValue = "1") int now_page, HttpSession session
      ) {
    ClassifyVO classifyVO = this.classifyProc.read(classifyno); // 수정: 실제 classifyno를 전달
    model.addAttribute("classifyVO", classifyVO);

    return "/classify/delete";


  }

  /**
   * 삭제 처리, http://localhost:9092/classify/update
   * 
   * @param model         Controller -> Thymeleaf HTML로 데이터 전송
   * @param cateVO        Form 태그 값 -> 검증 -> cateVO 자동저장, request.getParameter()
   *                      자동 실행
   * @param bindingResult 폼에 에러가 있는지 검사 지원
   * @return
   */
  @PostMapping(value = "/delete")
  public String delete(Model model, @RequestParam(name = "classifyno", defaultValue = "0") int classifyno
//      ,@RequestParam(name = "word", defaultValue = "") String word, RedirectAttributes ra,
//      @RequestParam(name = "now_page", defaultValue = "1") int now_page, HttpSession session
      ) {
    System.out.println(" -> delete post [ classify/delete ]");

    ClassifyVO classifyVO = this.classifyProc.read(classifyno); // 수정: 실제 classifyno를 전달
    model.addAttribute("classifyVO", classifyVO);

    int cnt = this.classifyProc.delete(classifyno);
    System.out.println(" -> del cnt [classifyCont]: " + cnt);

    if (cnt == 1) {
      model.addAttribute("code", "delete_success");
      model.addAttribute("name", classifyVO.getName());
      model.addAttribute("type", classifyVO.getClassify());
    } else {
      model.addAttribute("code", "delete_fail");
    }

    model.addAttribute("cnt", cnt);

    return "/classify/msg"; // templates/classify/msg.html
  }

  
  /**
   * 번호에 따른 조회 http://localhost:9091/read/1
   * 
   * @param model
   * @param classifyno
   * @return
   */
  @GetMapping(value = "/read/{classifyno}")
  public String read(Model model, @PathVariable("classifyno") Integer classifyno
//      ,@RequestParam(name = "word", defaultValue = "") String word,
//      @RequestParam(name = "now_page", defaultValue = "1") int now_page
      ) {
    // 변경: "classifyno"를 "classifyno"로
    ClassifyVO classifyVO = this.classifyProc.read(classifyno); // 수정: 실제 classifyno를 전달
    model.addAttribute("classifyVO", classifyVO);

    ArrayList<ClassifyVO> list = this.classifyProc.list_all();
//    ArrayList<ClassifyVO> list = this.classifyProc.list_search_paging(word, now_page, this.record_per_page);
    model.addAttribute("list", list);

    ArrayList<ClassifyVOMenu> menu = this.classifyProc.menu();
    model.addAttribute("menu", menu);
//    model.addAttribute("word", word);

//    // 프로젝트 목록 번호 생성
//    String list_file_name = "/classify/read/" + classifyno;
//    int search_count = this.classifyProc.list_search_count(word);
//    String paging = this.classifyProc.pagingBox(now_page, word, list_file_name, search_count, this.record_per_page,
//        this.page_per_block);
//    model.addAttribute("paging", paging);
//    model.addAttribute("now_page", now_page);
//    // 일련 변호 생성: 레코드 갯수 - ((현재 페이지수 -1) * 페이지당 레코드 수)
//    int no = search_count - ((now_page - 1) * this.record_per_page);
//    model.addAttribute("no", no);
    return "/classify/read";
  }
  
  
  
  /**
   * 수정 http://localhost:9091/classify/update/1
   * 
   * @param model
   * @param classifyno
   * @return
   */
  @GetMapping(value = "/update/{classifyno}")
  public String update(Model model, @PathVariable("classifyno") Integer classifyno
//      , @RequestParam(name = "word", defaultValue = "") String word,
//      @RequestParam(name = "now_page", defaultValue = "1") int now_page, HttpSession session
      ) { 
    ClassifyVO classifyVO = this.classifyProc.read(classifyno);                                                               
    model.addAttribute("classifyVO", classifyVO);

    return "/classify/update";
   
//    //추후 수정 시작
//    if (this.memberProc.isMember(session) || this.memberProc.isMemberAdmin(session)) {
//
//      ClassifyVO classifyVO = this.classifyProc.read(classifyno); // 수정: 실제 classifyno를 전달
//      model.addAttribute("classifyVO", classifyVO);
//
//      // 카테고리 그룹 목록
//      ArrayList<String> list_type = this.classifyProc.typeset();
////    classifyVO.setClassify(String.join("/", list_type));
//      model.addAttribute("list_type", String.join("/", list_type));
//
//    ArrayList<ClassifyVO> list = this.classifyProc.list_all();
//      ArrayList<ClassifyVO> list = this.classifyProc.list_search_paging(word, now_page, this.record_per_page);
//      model.addAttribute("list", list);
//
//      ArrayList<ClassifyVOMenu> menu = this.classifyProc.menu();
//      model.addAttribute("menu", menu);
//      model.addAttribute("word", word);
//
//      // 프로젝트 목록 번호 생성
//      String list_file_name = "/classify/read/" + classifyno;
//      int search_count = this.classifyProc.list_search_count(word);
//      String paging = this.classifyProc.pagingBox(now_page, word, list_file_name, search_count, this.record_per_page,
//          this.page_per_block);
//      model.addAttribute("paging", paging);
//      model.addAttribute("now_page", now_page);
//      // 일련 변호 생성: 레코드 갯수 - ((현재 페이지수 -1) * 페이지당 레코드 수)
//      int no = search_count - ((now_page - 1) * this.record_per_page);
//      model.addAttribute("no", no);
//
//      return "/classify/update";
//    } else {
//      return "redirect:/member/login_cookie_need"; // redirect
//    }
//  //추후 수정 끝
  }

  /**
   * 수정 처리, http://localhost:9092/classify/update
   * 
   * @param model         Controller -> Thymeleaf HTML로 데이터 전송
   * @param cateVO        Form 태그 값 -> 검증 -> cateVO 자동저장, request.getParameter()
   *                      자동 실행
   * @param bindingResult 폼에 에러가 있는지 검사 지원
   * @return
   */
  @PostMapping(value = "/update")
  public String update(Model model, @Valid @ModelAttribute("classifyVO") ClassifyVO classifyVO, BindingResult bindingResult,
      @RequestParam(name = "word", defaultValue = "") String word, RedirectAttributes ra,
      @RequestParam(name = "now_page", defaultValue = "1") int now_page, HttpSession session) {
    System.out.println(" -> update post [ classify/update ]");
    if (bindingResult.hasErrors()) {
      System.out.println(" -> Error 에러 발생  [ classify/update ]");
      return "/classify/update";
    }
//    System.out.println("영양제 이름 : " + classifyVO.getName());
//    System.out.println("영양제 가격 : " + classifyVO.getPrice());
//    System.out.println("영양제 개봉일 : " + classifyVO.getRdate());

    int cnt = this.classifyProc.update(classifyVO);
    System.out.println(" -> cnt [classifyCont]: " + cnt);

    if (cnt == 1) {
      model.addAttribute("code", "update_success");
      model.addAttribute("name", classifyVO.getName());
      model.addAttribute("type", classifyVO.getClassify());
    } else {
      model.addAttribute("code", "update_fail");
    }

    model.addAttribute("cnt", cnt);

    return "/classify/msg";
//    //추후 수정 시작
//    
//    System.out.println(" -> update post [ classify/update ]");
//    System.out.println("영양제 이름 : " + classifyVO.getName());
//    System.out.println("영양제 Seqno : " + classifyVO.getSeqno());
//    System.out.println("영양제 CNT : " + classifyVO.getCnt());
////    System.out.println("영양제 가격 : " + classifyVO.getPrice());
//    System.out.println("영양제 개봉일 : " + classifyVO.getRdate());
//    if (this.memberProc.isMemberAdmin(session)) {
//      if (bindingResult.hasErrors()) {
//        System.out.println(" -> Error 발생  [ classify/update ]");
//        model.addAttribute("classifyVO", classifyVO); // 에러 발생 시 데이터 유지
//        return "/classify/update";
//      }
//
//      // 업데이트 로직
//      int cnt = this.classifyProc.update(classifyVO);
//      System.out.println(" -> cnt [classifyCont]: " + cnt);
//
//      if (cnt == 1) {
//        model.addAttribute("code", "update_success");
//        ra.addAttribute("word", word);
//        ra.addAttribute("now_page", now_page);
//        return "redirect:/classify/update/" + classifyVO.getClassifyno();
//      } else {
//        model.addAttribute("code", "update_fail");
//      }
//
//      model.addAttribute("cnt", cnt);
//      return "/classify/msg"; // templates/classify/msg.html
//    } else {
//      return "redirect:/member/login_cookie_need"; // redirect
//    }
//    // 추후 수정 끝
  }

  
  
}
