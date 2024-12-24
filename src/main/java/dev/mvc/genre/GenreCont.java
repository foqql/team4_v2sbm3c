package dev.mvc.genre;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import dev.mvc.classify.ClassifyProcInter;
import dev.mvc.classify.ClassifyVOMenu;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/genre")
public class GenreCont {
  @Autowired
  @Qualifier("dev.mvc.genre.GenreProc")
  private GenreProcInter genreProc;
  
  @Autowired
  @Qualifier("dev.mvc.classify.ClassifyProc")
  private ClassifyProcInter classifyProc;

  public GenreCont() {
    System.out.println(" -> GenreCont  created.");
  }

  @GetMapping(value = "/create") // http://localhost:9092/genre/create
  public String create(Model model) {

    GenreVO genreVO = new GenreVO();
    model.addAttribute("genreVO", genreVO);
    genreVO.setGenre("이름을 입력하세요.");
    System.out.println(" -> Model Test [ GenreCont.java ] ");
    return "/genre/create";
  }

  /**
   * 등록 처리, http://localhost:9092/genre/create
   * 
   * @param model         Controller -> Thymeleaf HTML로 데이터 전송
   * @param cateVO        Form 태그 값 -> 검증 -> cateVO 자동저장, request.getParameter()
   *                      자동 실행
   * @param bindingResult 폼에 에러가 있는지 검사 지원
   * @return
   */
  @PostMapping(value = "/create")
  public String create(Model model, @Valid GenreVO genreVO, BindingResult bindingResult) {
    System.out.println(" -> create post [ genre/msg ]");
    if (bindingResult.hasErrors()) {
      System.out.println(" -> Error 에러 발생  [ genre/msg ]");
      return "/genre/create";
    }
//    System.out.println("영양제 이름 : " + genreVO.getName());
//    System.out.println("영양제 가격 : " + genreVO.getPrice());
//    System.out.println("영양제 개봉일 : " + genreVO.getRdate());

    int cnt = this.genreProc.create(genreVO);
    System.out.println(" -> cnt [genreCont]: " + cnt);

    if (cnt == 1) {
      model.addAttribute("code", "create_success");
      model.addAttribute("name", genreVO.getGenre());
    } else {
      model.addAttribute("code", "create_fail");
    }

    model.addAttribute("cnt", cnt);

    return "/genre/msg"; // templates/genre/msg.html
  }

  /**
   * 전체 리스트 조회 http://localhost:9092/genre/create
   * 
   * @param model
   * @return
   */
  @GetMapping(value = "/list_all") // http://localhost:9092/genre/create
  public String list_all(Model model) {
    ArrayList<GenreVO> list = this.genreProc.list_all();
    model.addAttribute("list", list);

    ArrayList<ClassifyVOMenu> menu = this.classifyProc.menu();
    model.addAttribute("menu", menu);

    ArrayList<GenreVOMenu> menu1 = this.genreProc.menu();
    model.addAttribute("menu1", menu1);
    return "/genre/list_all";
  }

  /**
   * 번호에 따른 조회 http://localhost:9091/read/1
   * 
   * @param model
   * @param genreno
   * @return
   */
  @GetMapping(value = "/read/{genreno}")
  public String read(Model model, @PathVariable("genreno") Integer genreno) { // 변경: "genreno"를 "ProductID"로
    GenreVO genreVO = this.genreProc.read(genreno); // 수정: 실제 genreno를 전달
    model.addAttribute("genreVO", genreVO);
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
  public String update(Model model, @PathVariable("genreno") Integer genreno) { // 변경: "genreno"를 "ProductID"로
    GenreVO genreVO = this.genreProc.read(genreno); // 수정: 실제 genreno를 전달
    model.addAttribute("genreVO", genreVO);

    return "/genre/update";
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
  public String update(Model model, @Valid GenreVO genreVO, BindingResult bindingResult) {
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
      model.addAttribute("name", genreVO.getGenre());
    } else {
      model.addAttribute("code", "update_fail");
    }

    model.addAttribute("cnt", cnt);

    return "/genre/msg"; // templates/genre/msg.html
  }

  /**
   * 삭제 http://localhost:9091/delete/update/1
   * 
   * @param model
   * @param genreno
   * @return
   */
  @GetMapping(value = "/delete/{genreno}")
  public String delete(Model model, @PathVariable("genreno") Integer genreno) { // 변경: "genreno"를 "ProductID"로
    GenreVO genreVO = this.genreProc.read(genreno); // 수정: 실제 genreno를 전달
    model.addAttribute("genreVO", genreVO);

    return "/genre/delete";
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
  @PostMapping(value = "/delete")
  public String delete(Model model, @RequestParam(name = "genreno", defaultValue = "0") int genreno) {
//    System.out.println(" -> delete post [ genre/delete ]");

    GenreVO genreVO = this.genreProc.read(genreno); // 수정: 실제 genreno를 전달
    model.addAttribute("genreVO", genreVO);

    int cnt = this.genreProc.delete(genreno);
    System.out.println(" -> del cnt [genreCont]: " + cnt);

    if (cnt == 1) {
      model.addAttribute("code", "delete_success");
      model.addAttribute("name", genreVO.getGenre());
    } else {
      model.addAttribute("code", "delete_fail");
    }

    model.addAttribute("cnt", cnt);

    return "/genre/msg"; // templates/genre/msg.html
  }

  /**
   * 우선 순위 높임, 10 등 -> 1 등, http://localhost:9091/cate/update_seqno_forward/1
   * 
   * @param model Controller -> Thymeleaf HTML로 데이터 전송에 사용
   * @return
   */
  @GetMapping(value = "/update_seqno_forward/{genreno}")
  public String update_seqno_forward(Model model, @PathVariable("genreno") Integer genreno) {
    this.genreProc.update_seqno_forward(genreno);

    return "redirect:/genre/list_all"; // @GetMapping(value="/list_all")
  }

  /**
   * 우선 순위 낮춤, 1 등 -> 10 등, http://localhost:9091/cate/update_seqno_backward/1
   * 
   * @param model Controller -> Thymeleaf HTML로 데이터 전송에 사용
   * @return
   */
  @GetMapping(value = "/update_seqno_backward/{genreno}")
  public String update_seqno_backward(Model model, @PathVariable("genreno") Integer genreno) {
    this.genreProc.update_seqno_backward(genreno);

    return "redirect:/genre/list_all"; // @GetMapping(value="/list_all")
  }

  /**
   * 카테고리 공개 설정, http://localhost:9091/cate/update_visible_y/1
   * 
   * @param model Controller -> Thymeleaf HTML로 데이터 전송에 사용
   * @return
   */
  @GetMapping(value = "/update_visible_y/{genreno}")
  public String update_visible_y(Model model, @PathVariable("genreno") Integer genreno) {
    this.genreProc.update_visible_y(genreno);

    return "redirect:/genre/list_all"; // @GetMapping(value="/list_all")
  }

  /**
   * 카테고리 비공개 설정, http://localhost:9091/cate/update_visible_n/1
   * 
   * @param model Controller -> Thymeleaf HTML로 데이터 전송에 사용
   * @return
   */
  @GetMapping(value = "/update_visible_n/{genreno}")
  public String update_visible_n(Model model, @PathVariable("genreno") Integer genreno) {


    this.genreProc.update_visible_n(genreno);
    return "redirect:/genre/list_all"; // @GetMapping(value="/list_all")
  }

  
}
