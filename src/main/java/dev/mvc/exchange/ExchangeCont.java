package dev.mvc.exchange;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import dev.mvc.member.MemberProcInter;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/exchange")
public class ExchangeCont {

    @Autowired
    @Qualifier("ExchangeProc") 
    private ExchangeProcInter classifyProc;
  
  @Autowired
  @Qualifier("dev.mvc.member.MemberProc")
  private MemberProcInter memberProc;

  /** 페이지당 출력할 레코드 갯수, nowPage는 1부터 시작 */
  public int record_per_page = 10;

  /** 블럭당 페이지 수, 하나의 블럭은 10개의 페이지로 구성됨 */
  public int page_per_block = 10;

  /** 페이징 목록 주소 */
  private String list_file_name = "/classify/list_search";

  public ExchangeCont() {
    System.out.println(" -> ClassifyCont  created.");
  }
  /**
   * 유형 3 카테고리별 목록 + 검색 + 페이징
   * http://localhost:9091/supplements/list_by_classify?classify=5
   * http://localhost:9091/supplements/list_by_classify?classify=6
   * 
   * @return
   */
  @GetMapping(value = "/list")
  public String list_by_classify_search_paging(
      HttpSession session, Model model,
      @RequestParam(name = "classifyno", defaultValue = "1") int classifyno,
      @RequestParam(name = "word", defaultValue = "") String word,
      @RequestParam(name = "now_page", defaultValue = "1") int now_page
      ) {

     System.out.println("-> classifyno: " + classifyno);

//    ArrayList<VitaVOMenu> menu = this.vitaProc.menu();
//    model.addAttribute("menu", menu);
//
//    VitaVO vitaVO = this.vitaProc.read(classify);
//    model.addAttribute("vitaVO", vitaVO);
//
//    word = Tool.checkNull(word).trim();
//
//    HashMap<String, Object> map = new HashMap<>();
//    map.put("classify", classify);
//    map.put("word", word);
//    map.put("now_page", now_page);
//
//    ArrayList<SupplementsVO> list = this.supplementsProc.list_by_classify_search_paging(map);
//    model.addAttribute("list", list);
//
//    // System.out.println("-> size: " + list.size());
//    model.addAttribute("word", word);
//
//    // 페이지 번호 목록 생성
//    int search_count = this.supplementsProc.list_by_classify_search_count(map);
//    String paging = this.supplementsProc.pagingBox(classify, now_page, word, "/supplements/list_by_classify",
//        search_count, Supplements.RECORD_PER_PAGE, Supplements.PAGE_PER_BLOCK);
//    model.addAttribute("paging", paging);
//    model.addAttribute("now_page", now_page);
//
//    model.addAttribute("search_count", search_count);
//
//    // 일련 변호 생성: 레코드 갯수 - ((현재 페이지수 -1) * 페이지당 레코드 수)
//    int no = search_count - ((now_page - 1) * Supplements.RECORD_PER_PAGE);
//    model.addAttribute("no", no);

    return "exchange/list"; // /templates/supplements/list_by_classify_search_paging.html
  }


  
}
