package dev.mvc.country;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
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

    
    
    
    
    @GetMapping(value = "/create") // http://localhost:9093/product/create
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
////    vitaVO.setPrice(120000);
////    vitaVO.setExpdate(today.plusDays(180)); // 유통기한 기본 180일 추가
//    System.out.println(" -> Model Test [ CountryCont.java ] ");
//    return "/genre/create";
//    }
    
    
}
