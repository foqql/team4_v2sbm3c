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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import dev.mvc.genre.GenreProcInter;
import dev.mvc.genre.GenreVO;
import dev.mvc.genre.GenreVOMenu;
import dev.mvc.member.MemberProcInter;
import dev.mvc.tool.Tool;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/classify")
public class ClassifyCont {

  @Autowired
  @Qualifier("dev.mvc.classify.ClassifyProc")
  private ClassifyProcInter classifyProc;

  @Autowired
  @Qualifier("dev.mvc.genre.GenreProc")
  private GenreProcInter genreProc;

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

  @GetMapping(value = "/create") // http://localhost:9092/classify/create
  public String create(Model model) {
    ArrayList<ClassifyVOMenu> menu = this.classifyProc.menu(); // 중분류
    model.addAttribute("menu", menu);
    ArrayList<GenreVOMenu> menu1 = this.genreProc.menu(); // 대분류
    model.addAttribute("menu1", menu1);

//    LocalDate today = LocalDate.now();
    ClassifyVO classifyVO = new ClassifyVO();
    model.addAttribute("classifyVO", classifyVO);
//    classifyVO.setGenreno();
//    classifyVO.setPrice(120000);
//    classifyVO.setExpdate(today.plusDays(180)); // 유통기한 기본 180일 추가
    System.out.println(" -> Model Test [ ClassifyCont.java ] ");
    return "/classify/create";
  }

  /**
   * 등록 처리, http://localhost:9092/classify/create
   * 
   * @param model         Controller -> Thymeleaf HTML로 데이터 전송
   * @param cateVO        Form 태그 값 -> 검증 -> cateVO 자동저장, request.getParameter()
   *                      자동 실행
   * @param bindingResult 폼에 에러가 있는지 검사 지원
   * @return
   */
  @PostMapping(value = "/create")
  public String create(Model model, @Valid ClassifyVO classifyVO, BindingResult bindingResult) {
    System.out.println(" -> create post [ classify/msg ]");
    if (bindingResult.hasErrors()) {
      System.out.println(" -> Error 에러 발생  [ classify/msg ]");
      return "/classify/create";
    }
    int cnt = this.classifyProc.create(classifyVO);
    System.out.println(" 이름 : " + classifyVO.getClassify());
    System.out.println(" -> cnt [classifyCont]: " + cnt);

    if (cnt == 1) {
//      model.addAttribute("code", "create_success");
//      model.addAttribute("name", classifyVO.getName());
      return "redirect:/classify/list_search"; // @GetMapping(value = "/list_search")
    } else {
      model.addAttribute("code", "create_fail");
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
  public String read(Model model, @PathVariable("classifyno") Integer classifyno,
      @RequestParam(name = "word", defaultValue = "") String word,
      @RequestParam(name = "now_page", defaultValue = "1") int now_page) {
    // 변경: "classifyno"를 "classifyno"로
    ClassifyVO classifyVO = this.classifyProc.read(classifyno); // 수정: 실제 classifyno를 전달
    model.addAttribute("classifyVO", classifyVO);

//    ArrayList<ClassifyVO> list = this.classifyProc.list_all();
    ArrayList<ClassifyVO> list = this.classifyProc.list_search_paging(word, now_page, this.record_per_page);
    model.addAttribute("list", list);

    ArrayList<ClassifyVOMenu> menu = this.classifyProc.menu(); // 중분류
    model.addAttribute("menu", menu);
    ArrayList<GenreVOMenu> menu1 = this.genreProc.menu(); // 대분류
    model.addAttribute("menu1", menu1);
    model.addAttribute("word", word);

    // 프로젝트 목록 번호 생성
    String list_file_name = "/classify/read/" + classifyno;
    int search_count = this.classifyProc.list_search_count(word);
    String paging = this.classifyProc.pagingBox(now_page, word, list_file_name, search_count, this.record_per_page,
        this.page_per_block);
    model.addAttribute("paging", paging);
    model.addAttribute("now_page", now_page);
    // 일련 변호 생성: 레코드 갯수 - ((현재 페이지수 -1) * 페이지당 레코드 수)
    int no = search_count - ((now_page - 1) * this.record_per_page);
    model.addAttribute("no", no);
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
  public String update(Model model, @PathVariable("classifyno") Integer classifyno,
      @RequestParam(name = "word", defaultValue = "") String word,
      @RequestParam(name = "now_page", defaultValue = "1") int now_page, HttpSession session) { // 변경: "classifyno"를
                                                                                                // "classifyno"로

    if (this.memberProc.isMember(session) || this.memberProc.isMemberAdmin(session)) {

      ClassifyVO classifyVO = this.classifyProc.read(classifyno); // 수정: 실제 classifyno를 전달
      model.addAttribute("classifyVO", classifyVO);

      // 카테고리 그룹 목록
      ArrayList<String> list_type = this.classifyProc.classifyset();
//    classifyVO.setClassify(String.join("/", list_type));
      model.addAttribute("list_type", String.join("/", list_type));

//    ArrayList<ClassifyVO> list = this.classifyProc.list_all();
      ArrayList<ClassifyVO> list = this.classifyProc.list_search_paging(word, now_page, this.record_per_page);
      model.addAttribute("list", list);

      ArrayList<ClassifyVOMenu> menu = this.classifyProc.menu(); // 중분류
      model.addAttribute("menu", menu);
      ArrayList<GenreVOMenu> menu1 = this.genreProc.menu(); // 대분류
      model.addAttribute("menu1", menu1);
      model.addAttribute("word", word);

      // 프로젝트 목록 번호 생성
      String list_file_name = "/classify/read/" + classifyno;
      int search_count = this.classifyProc.list_search_count(word);
      String paging = this.classifyProc.pagingBox(now_page, word, list_file_name, search_count, this.record_per_page,
          this.page_per_block);
      model.addAttribute("paging", paging);
      model.addAttribute("now_page", now_page);
      // 일련 변호 생성: 레코드 갯수 - ((현재 페이지수 -1) * 페이지당 레코드 수)
      int no = search_count - ((now_page - 1) * this.record_per_page);
      model.addAttribute("no", no);

      return "/classify/update";
    } else {
      return "redirect:/member/login_cookie_need"; // redirect
    }
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
  public String update(Model model, @Valid @ModelAttribute("classifyVO") ClassifyVO classifyVO,
      BindingResult bindingResult, @RequestParam(name = "word", defaultValue = "") String word, RedirectAttributes ra,
      @RequestParam(name = "now_page", defaultValue = "1") int now_page, HttpSession session) {
    System.out.println(" -> update post [ classify/update ]");
    System.out.println(" 이름 : " + classifyVO.getGenreno());

    if (this.memberProc.isMemberAdmin(session)) {
      if (bindingResult.hasErrors()) {
        System.out.println(" -> Error 발생  [ classify/update ]");
        model.addAttribute("classifyVO", classifyVO); // 에러 발생 시 데이터 유지
        return "/classify/update";
      }

      // 업데이트 로직
      int cnt = this.classifyProc.update(classifyVO);
      System.out.println(" -> cnt [classifyCont]: " + cnt);

      if (cnt == 1) {
        model.addAttribute("code", "update_success");
        ra.addAttribute("word", word);
        ra.addAttribute("now_page", now_page);
        return "redirect:/classify/update/" + classifyVO.getClassifyno();
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
   * 삭제 http://localhost:9091/delete/update/1
   * 
   * @param model
   * @param classifyno
   * @return
   */
  @GetMapping(value = "/delete/{classifyno}")
  public String delete(Model model, @PathVariable("classifyno") Integer classifyno,
      @RequestParam(name = "word", defaultValue = "") String word,
      @RequestParam(name = "now_page", defaultValue = "1") int now_page, HttpSession session) { // 변경: "classifyno"를
                                                                                                // "classifyno"로
    if (this.memberProc.isMemberAdmin(session)) {
      ClassifyVO classifyVO = this.classifyProc.read(classifyno); // 수정: 실제 classifyno를 전달
      model.addAttribute("classifyVO", classifyVO);

//    ArrayList<ClassifyVO> list = this.classifyProc.list_all();
      ArrayList<ClassifyVO> list = this.classifyProc.list_search_paging(word, now_page, this.record_per_page);
      model.addAttribute("list", list);

      ArrayList<ClassifyVOMenu> menu = this.classifyProc.menu(); // 중분류
      model.addAttribute("menu", menu);
      ArrayList<GenreVOMenu> menu1 = this.genreProc.menu(); // 대분류
      model.addAttribute("menu1", menu1);
      model.addAttribute("word", word);

      int search_count = this.classifyProc.list_search_count(word);
      String paging = this.classifyProc.pagingBox(now_page, word, this.list_file_name, search_count,
          this.record_per_page, this.page_per_block);
      model.addAttribute("paging", paging);
      model.addAttribute("now_page", now_page);
      // 일련 변호 생성: 레코드 갯수 - ((현재 페이지수 -1) * 페이지당 레코드 수)
      int no = search_count - ((now_page - 1) * this.record_per_page);
      model.addAttribute("no", no);
      return "/classify/delete";
    } else {
      return "redirect:/member/login_cookie_need"; // redirect
    }
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
  public String delete(Model model, @RequestParam(name = "classifyno", defaultValue = "0") int classifyno,
      @RequestParam(name = "word", defaultValue = "") String word, RedirectAttributes ra,
      @RequestParam(name = "now_page", defaultValue = "1") int now_page, HttpSession session) {
    System.out.println(" -> delete post [ classify/delete ]");

    if (this.memberProc.isMemberAdmin(session)) {
      ClassifyVO classifyVO = this.classifyProc.read(classifyno); // 수정: 실제 classifyno를 전달
      model.addAttribute("classifyVO", classifyVO);

      int cnt = this.classifyProc.delete(classifyno);
      System.out.println(" -> del cnt [classifyCont]: " + cnt);

      if (cnt == 1) {
        model.addAttribute("code", "delete_success");
//      model.addAttribute("name", classifyVO.getName());
//      model.addAttribute("type", classifyVO.getClassify());
        ra.addAttribute("word", word);

        // ----------------------------------------------------------------------------------------------------------
        // 마지막 페이지에서 모든 레코드가 삭제되면 페이지수를 1 감소 시켜야함.
        int search_cnt = this.classifyProc.list_search_count(word);

        if (search_cnt % this.record_per_page == 0) {
          now_page = now_page - 1;
          if (now_page < 1) {
            now_page = 1; // 최소 시작 페이지
          }
        }
        // ----------------------------------------------------------------------------------------------------------
        ra.addAttribute("now_page", now_page);

        return "redirect:/classify/list_search";
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
   * 우선 순위 높임, 10 등 -> 1 등, http://localhost:9091/cate/update_seqno_forward/1
   * 
   * @param model Controller -> Thymeleaf HTML로 데이터 전송에 사용
   * @return
   */
  @GetMapping(value = "/update_seqno_forward/{classifyno}")
  public String update_seqno_forward(Model model, @PathVariable("classifyno") Integer classifyno,
      @RequestParam(name = "word", defaultValue = "") String word, RedirectAttributes ra,
      @RequestParam(name = "now_page", defaultValue = "1") int now_page) {
    ra.addAttribute("now_page", now_page);
    this.classifyProc.update_seqno_forward(classifyno);

    ra.addAttribute("word", word);
    return "redirect:/classify/list_search"; // @GetMapping(value="/list_all")
  }

  /**
   * 우선 순위 낮춤, 1 등 -> 10 등, http://localhost:9091/cate/update_seqno_backward/1
   * 
   * @param model Controller -> Thymeleaf HTML로 데이터 전송에 사용
   * @return
   */
  @GetMapping(value = "/update_seqno_backward/{classifyno}")
  public String update_seqno_backward(Model model, @PathVariable("classifyno") Integer classifyno,
      @RequestParam(name = "word", defaultValue = "") String word, RedirectAttributes ra,
      @RequestParam(name = "now_page", defaultValue = "1") int now_page) {
    ra.addAttribute("now_page", now_page);
    this.classifyProc.update_seqno_backward(classifyno);

    ra.addAttribute("word", word);
    return "redirect:/classify/list_search"; // @GetMapping(value="/list_all")
  }

  /**
   * 카테고리 공개 설정, http://localhost:9091/cate/update_visible_y/1
   * 
   * @param model Controller -> Thymeleaf HTML로 데이터 전송에 사용
   * @return
   */
  @GetMapping(value = "/update_visible_y/{classifyno}")
  public String update_visible_y(Model model, @PathVariable("classifyno") Integer classifyno,
      @RequestParam(name = "word", defaultValue = "") String word, RedirectAttributes ra,
      @RequestParam(name = "now_page", defaultValue = "1") int now_page) {
    ra.addAttribute("now_page", now_page);
    this.classifyProc.update_visible_y(classifyno);

    ra.addAttribute("word", word);
    return "redirect:/classify/list_search"; // @GetMapping(value="/list_all")
  }

  /**
   * 카테고리 비공개 설정, http://localhost:9091/cate/update_visible_n/1
   * 
   * @param model Controller -> Thymeleaf HTML로 데이터 전송에 사용
   * @return
   */
  @GetMapping(value = "/update_visible_n/{classifyno}")
  public String update_visible_n(Model model, @PathVariable("classifyno") Integer classifyno,
      @RequestParam(name = "word", defaultValue = "") String word, RedirectAttributes ra,
      @RequestParam(name = "now_page", defaultValue = "1") int now_page) {
    ra.addAttribute("now_page", now_page);
    this.classifyProc.update_visible_n(classifyno);

    ra.addAttribute("word", word);
    return "redirect:/classify/list_search"; // @GetMapping(value="/list_all")
  }

  /**
   * 등록 폼 및 검색 목록 + 페이징 http://localhost:9092/cate/list_search
   * http://localhost:9092/cate/list_search?word=&word=&now_page=
   * http://localhost:9092/cate/list_search?word=까페&word=&now_page=1
   * 
   * @param model
   * @return
   */
  @GetMapping(value = "/list_search")
  public String list_search_paging(Model model, @RequestParam(name = "word", defaultValue = "") String word,
      @RequestParam(name = "now_page", defaultValue = "1") int now_page, HttpSession session) {
    if (this.memberProc.isMemberAdmin(session)) {
      ClassifyVO classifyVO = new ClassifyVO();
//      this.classifyProc.update_classify_cnt();
      this.classifyProc.update_classify_genre_cnt();

      // 카테고리 그룹 목록
      ArrayList<String> list_type = this.classifyProc.classifyset();
      classifyVO.setClassify(String.join("/", list_type));

      model.addAttribute("classifyVO", classifyVO);
System.out.println("classifyVO : "+ classifyVO );
      word = Tool.checkNull(word);

      ArrayList<ClassifyVO> list = this.classifyProc.list_search_paging(word, now_page, this.record_per_page);
      model.addAttribute("list", list);
//    ArrayList<ClassifyVO> list = this.classifyProc.list_all();
//      System.out.println(list);

      ArrayList<GenreVO> GenreVO = this.genreProc.list_all();
      System.out.println(GenreVO);
      model.addAttribute("GenreVO", GenreVO);
// -------------------- cnt 업데이트 시작
      for (GenreVO i : GenreVO) {
        
          int check = this.classifyProc.checkTable(i.getLink()); // 테이블 여부 확인
          System.out.println("check : " + check);

          if (check == 1) { // 테이블이 있으면 실행
              System.out.println("cnt 업데이트 실행");
              System.out.println("link : " + i.getLink());
              this.classifyProc.update_classify_cnt(i.getLink()); // genre테이블의 link컬럼의 데이터로 쿼리 진행
              System.out.println("cnt 업데이트 끝");
          }
      }
      
      
// -------------------- cnt 업데이트 끝
      ArrayList<ClassifyVOMenu> menu = this.classifyProc.menu(); // 중분류
      model.addAttribute("menu", menu);
      ArrayList<GenreVOMenu> menu1 = this.genreProc.menu(); // 대분류
      model.addAttribute("menu1", menu1);

      int search_cnt = this.classifyProc.list_search_count(word);

      model.addAttribute("search_cnt", search_cnt);
      model.addAttribute("word", word);

      // 페이지 번호 목록 생성
      int search_count = this.classifyProc.list_search_count(word);
      String paging = this.classifyProc.pagingBox(now_page, word, this.list_file_name, search_count,
          this.record_per_page, this.page_per_block);

      model.addAttribute("paging", paging);
      model.addAttribute("now_page", now_page);
      // 일련 변호 생성: 레코드 갯수 - ((현재 페이지수 -1) * 페이지당 레코드 수)
      int no = search_count - ((now_page - 1) * this.record_per_page);
      model.addAttribute("no", no);
      return "/classify/list_search";
    } else {
      return "redirect:/member/login_cookie_need"; // redirect
    }

  }

 



  

}
