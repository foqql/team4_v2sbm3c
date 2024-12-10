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

import jakarta.servlet.http.HttpSession;

@Controller
//@RequestMapping("/genre")
@RequestMapping("/genre")
public class GenreCont {

  @Autowired
  @Qualifier("GenreProc")
  private GenreProcInter genreProc;

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

}
