package dev.mvc.exchange;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import dev.mvc.member.MemberProcInter;

@Controller
@RequestMapping("/news")
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


  
}
