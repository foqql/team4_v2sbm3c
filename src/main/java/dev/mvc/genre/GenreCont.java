package dev.mvc.genre;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;



@Controller
//@RequestMapping("/genre")
@RequestMapping("/genre")
public class GenreCont {

    @Autowired
    @Qualifier("GenreProc")
    private GenreProcInter genreProc;

    @GetMapping(value = "/create1")
    @ResponseBody
    public String createForm() {
        return "<h2>Create test [ Genre Cont ] </h2>";
    }

    
    
    
    /**
     * 등록 화면
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
        System.out.println(" -> create post [ product/msg ]");

        if (bindingResult.hasErrors()) {
            System.out.println(" -> Error 에러 발생  [ product/msg ]");
            return "/genre/create";
        }

        System.out.println("장르: " + genreVO.getGenre());
        System.out.println("이름: " + genreVO.getName());
        System.out.println("출력 순서 : " + genreVO.getSeqno());
        System.out.println("출력 모드 : " + genreVO.getVisible());
        System.out.println("추가 날짜 : " + genreVO.getRdate());  // rdate 출력

        int cnt = this.genreProc.create(genreVO);
        System.out.println(" -> cnt [genreCont]: " + cnt);

        if (cnt == 1) {
          
          return "/genre/list_search"; 
        } else {
            model.addAttribute("code", "create_fail");
        }

        model.addAttribute("cnt", cnt);
        // 아직 안만듬
        return "/product/msg"; // templates/product/msg.html
    }

    
    /**
     * 전체 리스트 조회 http://localhost:9092/product/create
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

    
}
