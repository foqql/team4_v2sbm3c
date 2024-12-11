package dev.mvc.genre;

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
//@RequestMapping("/genre")
@RequestMapping("/genre")
public class GenreCont {

  @Autowired
  @Qualifier("GenreProc")
  private GenreProcInter genreProc;
  
  @Autowired
  @Qualifier("dev.mvc.member.MemberProc")
  private MemberProcInter memberProc;

  /** 페이지당 출력할 레코드 갯수, nowPage는 1부터 시작 */
  public int record_per_page = 10;

  /** 블럭당 페이지 수, 하나의 블럭은 10개의 페이지로 구성됨 */
  public int page_per_block = 10;

  /** 페이징 목록 주소 */
  private String list_file_name = "/genre/list_search";

  public GenreCont() {
    System.out.println(" -> GenreCont  created.");
  }

  @GetMapping(value = "/create1")
  @ResponseBody
  public String createForm() {
    return "<h2>Create test [ Genre Cont ] </h2>";
  }

  /**
   * 등록 화면
   * 
   * @param model
   * @return
   */
  @GetMapping(value = "/create") // http://localhost:9093/genre/create
  public String create(Model model) {

//      LocalDate today = LocalDate.now();
    GenreVO genreVO = new GenreVO();
    model.addAttribute("genreVO", genreVO);
    System.out.println(" -> Model Test [ GenreCont.java ] ");
    return "/genre/create";
  }

  @PostMapping(value = "/create")
  public String create(Model model, @ModelAttribute GenreVO genreVO, BindingResult bindingResult) {
    System.out.println(" -> create post [ genre/msg ]");

    if (bindingResult.hasErrors()) {
      System.out.println(" -> Error 에러 발생  [ genre/msg ]");
      return "/genre/create";
    }

    System.out.println("장르: " + genreVO.getGenre());
    System.out.println("이름: " + genreVO.getName());
    System.out.println("출력 순서 : " + genreVO.getSeqno());
    System.out.println("출력 모드 : " + genreVO.getVisible());
    System.out.println("추가 날짜 : " + genreVO.getRdate()); // rdate 출력

    int cnt = this.genreProc.create(genreVO);
    System.out.println(" -> cnt [genreCont]: " + cnt);

    if (cnt == 1) {
      System.out.println(" -> 72      cnt [genreCont]: " + cnt);

      return "/genre/list_search";
    } else {
      model.addAttribute("code", "create_fail");
    }

    model.addAttribute("cnt", cnt);
    // 아직 안만듬
    return "/genre/msg"; // templates/genre/msg.html
  }

  /**
   * 전체 리스트 조회 http://localhost:9092/genre/create
   * 
   * @param model
   * @return
   */
  @GetMapping(value = "/list_all") // http://localhost:9093/genre/list_search
  public String list_all(Model model) {
    GenreVO genreVO = new GenreVO();
//System.out.println("92 디버깅");
    // 카테고리 그룹 목록
    ArrayList<String> list_type = this.genreProc.typeset();
    genreVO.setGenre(String.join("/", list_type));

    model.addAttribute("genreVO", genreVO);

    ArrayList<GenreVO> list = this.genreProc.list_all();
    model.addAttribute("list", list);

    ArrayList<GenreVOMenu> menu = this.genreProc.menu();
    model.addAttribute("menu", menu);

    return "/genre/list_search";
  }

  /**
   * 삭제 http://localhost:9091/delete/update/1
   * 
   * @param model
   * @param genreno
   * @return
   */
  @GetMapping(value = "/delete/{genreno}")
  public String delete(Model model, @PathVariable("genreno") Integer genreno
//      , @RequestParam(name = "word", defaultValue = "") String word,
//      @RequestParam(name = "now_page", defaultValue = "1") int now_page, HttpSession session
      ) {
    GenreVO genreVO = this.genreProc.read(genreno); // 수정: 실제 genreno를 전달
    model.addAttribute("genreVO", genreVO);

    return "/genre/delete";


  }

  /**
   * 삭제 처리, http://localhost:9092/genre/update
   * 
   * @param model         Controller -> Thymeleaf HTML로 데이터 전송
   * @param cateVO        Form 태그 값 -> 검증 -> cateVO 자동저장, request.getParameter()
   *                      자동 실행
   * @param bindingResult 폼에 에러가 있는지 검사 지원
   * @return
   */
  @PostMapping(value = "/delete")
  public String delete(Model model, @RequestParam(name = "genreno", defaultValue = "0") int genreno
//      ,@RequestParam(name = "word", defaultValue = "") String word, RedirectAttributes ra,
//      @RequestParam(name = "now_page", defaultValue = "1") int now_page, HttpSession session
      ) {
    System.out.println(" -> delete post [ genre/delete ]");

    GenreVO genreVO = this.genreProc.read(genreno); // 수정: 실제 genreno를 전달
    model.addAttribute("genreVO", genreVO);

    int cnt = this.genreProc.delete(genreno);
    System.out.println(" -> del cnt [genreCont]: " + cnt);

    if (cnt == 1) {
      model.addAttribute("code", "delete_success");
      model.addAttribute("name", genreVO.getName());
      model.addAttribute("type", genreVO.getGenre());
    } else {
      model.addAttribute("code", "delete_fail");
    }

    model.addAttribute("cnt", cnt);

    return "/genre/msg"; // templates/genre/msg.html
  }

  
  /**
   * 번호에 따른 조회 http://localhost:9091/read/1
   * 
   * @param model
   * @param genreno
   * @return
   */
  @GetMapping(value = "/read/{genreno}")
  public String read(Model model, @PathVariable("genreno") Integer genreno
//      ,@RequestParam(name = "word", defaultValue = "") String word,
//      @RequestParam(name = "now_page", defaultValue = "1") int now_page
      ) {
    // 변경: "genreno"를 "genreno"로
    GenreVO genreVO = this.genreProc.read(genreno); // 수정: 실제 genreno를 전달
    model.addAttribute("genreVO", genreVO);

    ArrayList<GenreVO> list = this.genreProc.list_all();
//    ArrayList<GenreVO> list = this.genreProc.list_search_paging(word, now_page, this.record_per_page);
    model.addAttribute("list", list);

    ArrayList<GenreVOMenu> menu = this.genreProc.menu();
    model.addAttribute("menu", menu);
//    model.addAttribute("word", word);

//    // 프로젝트 목록 번호 생성
//    String list_file_name = "/genre/read/" + genreno;
//    int search_count = this.genreProc.list_search_count(word);
//    String paging = this.genreProc.pagingBox(now_page, word, list_file_name, search_count, this.record_per_page,
//        this.page_per_block);
//    model.addAttribute("paging", paging);
//    model.addAttribute("now_page", now_page);
//    // 일련 변호 생성: 레코드 갯수 - ((현재 페이지수 -1) * 페이지당 레코드 수)
//    int no = search_count - ((now_page - 1) * this.record_per_page);
//    model.addAttribute("no", no);
    return "/genre/read";
  }
  
  
  
  /**
   * 수정 http://localhost:9091/genre/update/1
   * 
   * @param model
   * @param genreno
   * @return
   */
  @GetMapping(value = "/update/{genreno}")
  public String update(Model model, @PathVariable("genreno") Integer genreno
//      , @RequestParam(name = "word", defaultValue = "") String word,
//      @RequestParam(name = "now_page", defaultValue = "1") int now_page, HttpSession session
      ) { 
    GenreVO genreVO = this.genreProc.read(genreno);                                                               
    model.addAttribute("genreVO", genreVO);

    return "/genre/update";
   
//    //추후 수정 시작
//    if (this.memberProc.isMember(session) || this.memberProc.isMemberAdmin(session)) {
//
//      GenreVO genreVO = this.genreProc.read(genreno); // 수정: 실제 genreno를 전달
//      model.addAttribute("genreVO", genreVO);
//
//      // 카테고리 그룹 목록
//      ArrayList<String> list_type = this.genreProc.typeset();
////    genreVO.setGenre(String.join("/", list_type));
//      model.addAttribute("list_type", String.join("/", list_type));
//
//    ArrayList<GenreVO> list = this.genreProc.list_all();
//      ArrayList<GenreVO> list = this.genreProc.list_search_paging(word, now_page, this.record_per_page);
//      model.addAttribute("list", list);
//
//      ArrayList<GenreVOMenu> menu = this.genreProc.menu();
//      model.addAttribute("menu", menu);
//      model.addAttribute("word", word);
//
//      // 프로젝트 목록 번호 생성
//      String list_file_name = "/genre/read/" + genreno;
//      int search_count = this.genreProc.list_search_count(word);
//      String paging = this.genreProc.pagingBox(now_page, word, list_file_name, search_count, this.record_per_page,
//          this.page_per_block);
//      model.addAttribute("paging", paging);
//      model.addAttribute("now_page", now_page);
//      // 일련 변호 생성: 레코드 갯수 - ((현재 페이지수 -1) * 페이지당 레코드 수)
//      int no = search_count - ((now_page - 1) * this.record_per_page);
//      model.addAttribute("no", no);
//
//      return "/genre/update";
//    } else {
//      return "redirect:/member/login_cookie_need"; // redirect
//    }
//  //추후 수정 끝
  }

  /**
   * 수정 처리, http://localhost:9092/genre/update
   * 
   * @param model         Controller -> Thymeleaf HTML로 데이터 전송
   * @param cateVO        Form 태그 값 -> 검증 -> cateVO 자동저장, request.getParameter()
   *                      자동 실행
   * @param bindingResult 폼에 에러가 있는지 검사 지원
   * @return
   */
  @PostMapping(value = "/update")
  public String update(Model model, @Valid @ModelAttribute("genreVO") GenreVO genreVO, BindingResult bindingResult,
      @RequestParam(name = "word", defaultValue = "") String word, RedirectAttributes ra,
      @RequestParam(name = "now_page", defaultValue = "1") int now_page, HttpSession session) {
    System.out.println(" -> update post [ genre/update ]");
    if (bindingResult.hasErrors()) {
      System.out.println(" -> Error 에러 발생  [ genre/update ]");
      return "/genre/update";
    }
//    System.out.println("영양제 이름 : " + genreVO.getName());
//    System.out.println("영양제 가격 : " + genreVO.getPrice());
//    System.out.println("영양제 개봉일 : " + genreVO.getRdate());

    int cnt = this.genreProc.update(genreVO);
    System.out.println(" -> cnt [genreCont]: " + cnt);

    if (cnt == 1) {
      model.addAttribute("code", "update_success");
      model.addAttribute("name", genreVO.getName());
      model.addAttribute("type", genreVO.getGenre());
    } else {
      model.addAttribute("code", "update_fail");
    }

    model.addAttribute("cnt", cnt);

    return "/genre/msg";
//    //추후 수정 시작
//    
//    System.out.println(" -> update post [ genre/update ]");
//    System.out.println("영양제 이름 : " + genreVO.getName());
//    System.out.println("영양제 Seqno : " + genreVO.getSeqno());
//    System.out.println("영양제 CNT : " + genreVO.getCnt());
////    System.out.println("영양제 가격 : " + genreVO.getPrice());
//    System.out.println("영양제 개봉일 : " + genreVO.getRdate());
//    if (this.memberProc.isMemberAdmin(session)) {
//      if (bindingResult.hasErrors()) {
//        System.out.println(" -> Error 발생  [ genre/update ]");
//        model.addAttribute("genreVO", genreVO); // 에러 발생 시 데이터 유지
//        return "/genre/update";
//      }
//
//      // 업데이트 로직
//      int cnt = this.genreProc.update(genreVO);
//      System.out.println(" -> cnt [genreCont]: " + cnt);
//
//      if (cnt == 1) {
//        model.addAttribute("code", "update_success");
//        ra.addAttribute("word", word);
//        ra.addAttribute("now_page", now_page);
//        return "redirect:/genre/update/" + genreVO.getGenreno();
//      } else {
//        model.addAttribute("code", "update_fail");
//      }
//
//      model.addAttribute("cnt", cnt);
//      return "/genre/msg"; // templates/genre/msg.html
//    } else {
//      return "redirect:/member/login_cookie_need"; // redirect
//    }
//    // 추후 수정 끝
  }

  
  
}
