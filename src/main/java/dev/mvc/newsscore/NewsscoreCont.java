package dev.mvc.newsscore;

import java.util.ArrayList;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import dev.mvc.classify.ClassifyProcInter;
import dev.mvc.classify.ClassifyVOMenu;
import dev.mvc.genre.GenreProcInter;
import dev.mvc.genre.GenreVOMenu;
import dev.mvc.member.MemberProcInter;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping(value = "/newsscore")
public class NewsscoreCont {
  
  @Autowired
  @Qualifier("dev.mvc.newsscore.NewsscoreProc")
  NewsscoreProcInter newsscoreProc;
  
  @Autowired
  @Qualifier("dev.mvc.classify.ClassifyProc") // @Component("dev.mvc.cate.CateProc")
  private ClassifyProcInter classifyProc;
  
  @Autowired
  @Qualifier("dev.mvc.member.MemberProc") // @Service("dev.mvc.member.MemberProc")
  private MemberProcInter memberProc;
  
  @Autowired
  @Qualifier("dev.mvc.genre.GenreProc") // @Component("dev.mvc.exchange.ExchangeProc")
  private GenreProcInter genreProc;
  
  public NewsscoreCont() {
    System.out.println("-> NewsscoreCont created.");
  }
    /**
     * POST 요청시 새로고침 방지, POST 요청 처리 완료 → redirect → url → GET → forward -> html 데이터
     * 전송
     * 
     * @return
     */
    @GetMapping(value = "/post2get")
    public String post2get(Model model, 
        @RequestParam(name="url", defaultValue = "") String url) {
      ArrayList<ClassifyVOMenu> menu = this.classifyProc.menu();
      model.addAttribute("menu", menu);

      return url; // forward, /templates/...
    }
    
  
    /**
     * 목록
     * 
     * @param model
     * @return
     */
    // http://localhost:9091/newsscore/list_all
    @GetMapping(value = "/list_all")
    public String list_all(Model model) {
      ArrayList<NewsscoreVO> list = this.newsscoreProc.list_all();
      model.addAttribute("list", list);
      
      ArrayList<ClassifyVOMenu> menu = this.classifyProc.menu();
      model.addAttribute("menu", menu);
      
      ArrayList<GenreVOMenu> menu1 = this.genreProc.menu(); // 대분류
      model.addAttribute("menu1", menu1);

      return "/newsscore/list_all"; // /templates/calendar/list_all.html
    }
    
    /**
     * 삭제 처리 http://localhost:9091/calendar/delete?calendarno=1
     * 
     * @return
     */
    @PostMapping(value = "/delete")
    public String delete(HttpSession session, 
        Model model, 
        @RequestParam(name="newsscoreno", defaultValue = "0") int newsscoreno, 
        RedirectAttributes ra) {    
      
      if (this.memberProc.isMemberAdmin(session)) { // 관리자 로그인 확인
        this.newsscoreProc.delete(newsscoreno);

        return "redirect:/newsscore/list_all";

      } else { // 정상적인 로그인이 아닌 경우 로그인 유도
        ra.addAttribute("url", "/member/login_cookie_need"); // /templates/member/login_cookie_need.html
        return "redirect:/newsscore/post2get"; // @GetMapping(value = "/msg")
      }

    }
    
    
    
    
  }
  
  
