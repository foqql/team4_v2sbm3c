package dev.mvc.translate;

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
//@RequestMapping("/translate")
@RequestMapping("/translate")
public class TranslateCont {

    @Autowired
    @Qualifier("TranslateProc")
    private TranslateProcInter translateProc;

    @GetMapping(value = "/create1")
    @ResponseBody
    public String createForm() {
        return "<h2>Create test [ Translate Cont ] </h2>";
    }

    
    
    
    /**
     * 등록 화면
     * @param model
     * @return
     */
    @GetMapping(value = "/create") // http://localhost:9093/translate/create
    public String create(Model model) {

//      LocalDate today = LocalDate.now();
      TranslateVO translateVO = new TranslateVO();
      model.addAttribute("translateVO", translateVO);
//      translateVO.setName("이름을 입력하세요.");
//      translateVO.setPrice(120000);
//      translateVO.setExpdate(today.plusDays(180)); // 유통기한 기본 180일 추가
      System.out.println(" -> Model Test [ TranslateCont.java ] ");
      return "/translate/create";
    }
    
    
    
    
    
    
//    @PostMapping(value = "/create")// http://localhost:9093/product/create
//    @ResponseBody
//    public String create(Model model) {
//
////    LocalDate today = LocalDate.now();
//    TranslateVO translateVO = new TranslateVO();
//    model.addAttribute("translateVO", translateVO);
//   // translateVO.setName("이름을 입력하세요.");
////    translateVO.setPrice(120000);
////    translateVO.setExpdate(today.plusDays(180)); // 유통기한 기본 180일 추가
//    System.out.println(" -> Model Test [ TranslateCont.java ] ");
//    return "/translate/create";
//    }
    
    
    @PostMapping(value = "/create")
    public String create(Model model, @ModelAttribute TranslateVO translateVO, BindingResult bindingResult) {
        System.out.println(" -> create post [ product/msg ]");

        if (bindingResult.hasErrors()) {
            System.out.println(" -> Error 에러 발생  [ product/msg ]");
            return "/translate/create";
        }

        System.out.println("번역된 제목 : " + translateVO.getTitle());
        System.out.println("번역된 내용 : " + translateVO.getContent());


        int cnt = this.translateProc.create(translateVO);
        System.out.println(" -> cnt [translateCont]: " + cnt);

        if (cnt == 1) {
          
          System.out.println("89 된건다");
          return "/translate/list_search"; 
//            return "redirect:/translate/list_search"; 
//            return "<h2>리스트 페이지 수정중</h2>";
        } else {
            model.addAttribute("code", "create_fail");
        }

        model.addAttribute("cnt", cnt);
        return "/product/msg"; // templates/product/msg.html
    }

    
    
    
}
