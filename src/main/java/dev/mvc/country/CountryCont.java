package dev.mvc.country;

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
//@RequestMapping("/country")
@RequestMapping("/genre")
public class CountryCont {

    @Autowired
    @Qualifier("CountryProc")
    private CountryProcInter countryProc;

    @GetMapping(value = "/create1")
    @ResponseBody
    public String createForm() {
        return "<h2>Create test [ Country Cont ] </h2>";
    }

    
    
    
    /**
     * 등록 화면
     * @param model
     * @return
     */
    @GetMapping(value = "/create") // http://localhost:9093/genre/create
    public String create(Model model) {

//      LocalDate today = LocalDate.now();
      CountryVO countryVO = new CountryVO();
      model.addAttribute("countryVO", countryVO);
//      countryVO.setName("이름을 입력하세요.");
//      countryVO.setPrice(120000);
//      countryVO.setExpdate(today.plusDays(180)); // 유통기한 기본 180일 추가
      System.out.println(" -> Model Test [ CountryCont.java ] ");
      return "/genre/create";
    }
    
    
    
    
    
    
//    @PostMapping(value = "/create")// http://localhost:9093/product/create
//    @ResponseBody
//    public String create(Model model) {
//
////    LocalDate today = LocalDate.now();
//    CountryVO countryVO = new CountryVO();
//    model.addAttribute("countryVO", countryVO);
//   // countryVO.setName("이름을 입력하세요.");
////    countryVO.setPrice(120000);
////    countryVO.setExpdate(today.plusDays(180)); // 유통기한 기본 180일 추가
//    System.out.println(" -> Model Test [ CountryCont.java ] ");
//    return "/genre/create";
//    }
    
    
    @PostMapping(value = "/create")
    public String create(Model model, @ModelAttribute CountryVO countryVO, BindingResult bindingResult) {
        System.out.println(" -> create post [ product/msg ]");

        if (bindingResult.hasErrors()) {
            System.out.println(" -> Error 에러 발생  [ product/msg ]");
            return "/genre/create";
        }

        System.out.println("회사 명 : " + countryVO.getCompany());
        System.out.println("출력 순서 : " + countryVO.getSeqno());
        System.out.println("출력 모드 : " + countryVO.getVisible());
        System.out.println("추가 날짜 : " + countryVO.getRdate());  // rdate 출력

        int cnt = this.countryProc.create(countryVO);
        System.out.println(" -> cnt [countryCont]: " + cnt);

        if (cnt == 1) {
          
          System.out.println("89 된건다");
          return "/genre/list_search"; 
//            return "redirect:/genre/list_search"; 
//            return "<h2>리스트 페이지 수정중</h2>";
        } else {
            model.addAttribute("code", "create_fail");
        }

        model.addAttribute("cnt", cnt);
        return "/product/msg"; // templates/product/msg.html
    }

    
    
    
}
