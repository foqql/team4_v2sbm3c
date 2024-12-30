package dev.mvc.exchange;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import dev.mvc.classify.ClassifyProcInter;
import dev.mvc.classify.ClassifyVO;
import dev.mvc.classify.ClassifyVOMenu;
import dev.mvc.genre.GenreProcInter;
import dev.mvc.genre.GenreVOMenu;
import dev.mvc.member.MemberProcInter;
import dev.mvc.tool.Tool;
import dev.mvc.tool.Upload;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@RequestMapping(value = "/exchange")
@Controller
public class ExchangeCont {
  @Autowired
  @Qualifier("dev.mvc.member.MemberProc") // @Service("dev.mvc.member.MemberProc")
  private MemberProcInter memberProc;

  @Autowired
  @Qualifier("dev.mvc.classify.ClassifyProc") // @Component("dev.mvc.classify.ClassifyProc")
  private ClassifyProcInter classifyProc;

  @Autowired
  @Qualifier("dev.mvc.exchange.ExchangeProc") // @Component("dev.mvc.exchange.ExchangeProc")
  private ExchangeProcInter exchangeProc;
  @Autowired
  @Qualifier("dev.mvc.genre.GenreProc") // @Component("dev.mvc.exchange.ExchangeProc")
  private GenreProcInter genreProc;

  public ExchangeCont() {
    System.out.println("-> ExchangeCont created.");
  }

  /**
   * 
   * 최근 정보 열람
   * http://localhost:9093/exchange/list_by_classifyno?classifyno=6
   * 
   * @return
   */
  @GetMapping(value = "/list_by_classifyno")
  public String list_by_classifyno_search_paging(
      HttpSession session, 
      Model model, 
      @RequestParam(name = "classifyno", defaultValue = "1") int classifyno) {

    // System.out.println("-> classifyno: " + classifyno);

    ArrayList<ClassifyVOMenu> menu = this.classifyProc.menu(); // 중분류
    model.addAttribute("menu", menu);
    ArrayList<GenreVOMenu> menu1 = this.genreProc.menu(); // 대분류
    model.addAttribute("menu1", menu1);

    ClassifyVO classifyVO = this.classifyProc.read(classifyno);
    model.addAttribute("classifyVO", classifyVO);
    ExchangeVO exchangeVO = this.exchangeProc.reading(classifyno);
    model.addAttribute("exchangeVO", exchangeVO);

    HashMap<String, Object> map = new HashMap<>();
    map.put("classifyno", classifyno);
//    System.out.println("exchangeVO.getPrice() : " +exchangeVO.getPrice());
//    System.out.println("exchangeVO.getPrice() : " +exchangeVO.getKrw());
//    System.out.println("exchangeVO.getPrice() : " +exchangeVO.getValue());
//    System.out.println("exchangeVO.getPrice() : " +exchangeVO.getYesterday());
    // 1원당 가치 시작
    DecimalFormat df = new DecimalFormat("0.000000");
    String formattedValue = df.format(exchangeVO.getKrw());
//    System.out.println("formattedValue " + formattedValue);
    // krw에 formattedValue 삽입
    model.addAttribute("krw", formattedValue);
    // 1원당 가치 끝
    return "/exchange/read"; // /templates/exchange/list_by_classifyno_search_paging.html
  }

  
  /**
   * 맵 등록/수정/삭제 폼 http://localhost:9091/exchange/map?exchangeno=19
   * 
   * @return
   */
  @GetMapping(value = "/map")
  public String map(Model model, 
                            @RequestParam(name="exchangeno", defaultValue="0") int exchangeno,
                            @RequestParam(name="classifyno", defaultValue="0") int classifyno
                            ) {
    ArrayList<ClassifyVOMenu> menu = this.classifyProc.menu();
    model.addAttribute("menu", menu);

//    ExchangeVO exchangeVO = this.exchangeProc.read(exchangeno); // map 정보 읽어 오기
    ExchangeVO exchangeVO = this.exchangeProc.reading(classifyno); // map 정보 읽어 오기
    model.addAttribute("exchangeVO", exchangeVO); // request.setAttribute("exchangeVO", exchangeVO);

    ClassifyVO classifyVO = this.classifyProc.read(exchangeVO.getClassifyno()); // 그룹 정보 읽기
    model.addAttribute("classifyVO", classifyVO);
//System.out.println("겟 맵 확인");
    return "/exchange/map"; // //templates/exchange/map.html
  }


  /**
   * MAP 등록/수정/삭제 처리 http://localhost:9091/exchange/map
   * 
   * @param exchangeVO
   * @return
   */
  @PostMapping(value = "/map")
  public String map_update(Model model, 
      @RequestParam(name="classifyno", defaultValue = "0") int classifyno, 
      @RequestParam(name="exchangeno", defaultValue = "0") int exchangeno, 
      @RequestParam(name="map", defaultValue = "") String map) {
    
    HashMap<String, Object> hashMap = new HashMap<String, Object>();
    hashMap.put("classifyno", classifyno);
    hashMap.put("exchangeno", exchangeno);
    hashMap.put("map", map);
    
    ClassifyVO classifyVO = this.classifyProc.read(classifyno);
    model.addAttribute("classifyVO", classifyVO);
    ExchangeVO exchangeVO = this.exchangeProc.reading(classifyno);
    model.addAttribute("exchangeVO", exchangeVO);
    this.exchangeProc.map(hashMap);
    return "redirect:/exchange/list_by_classifyno?classifyno=" + classifyno;
  }
  
 
}