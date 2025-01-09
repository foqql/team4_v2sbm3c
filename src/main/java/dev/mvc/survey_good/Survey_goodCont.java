package dev.mvc.survey_good;

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

@RequestMapping(value = "/survey_good")
@Controller
public class Survey_goodCont {
  @Autowired
  @Qualifier("dev.mvc.member.MemberProc") // @Service("dev.mvc.member.MemberProc")
  private MemberProcInter memberProc;

  @Autowired
  @Qualifier("dev.mvc.classify.ClassifyProc") // @Component("dev.mvc.classify.ClassifyProc")
  private ClassifyProcInter classifyProc;

  @Autowired
  @Qualifier("dev.mvc.survey_good.Survey_goodProc") // @Component("dev.mvc.survey_good.Survey_goodProc")
  private Survey_goodProcInter survey_goodProc;
  @Autowired
  @Qualifier("dev.mvc.genre.GenreProc") // @Component("dev.mvc.survey_good.Survey_goodProc")
  private GenreProcInter genreProc;

  public Survey_goodCont() {
    System.out.println("-> Survey_goodCont created.");
  }

  /**
   * POST 요청시 새로고침 방지, POST 요청 처리 완료 → redirect → url → GET → forward -> html 데이터
   * 전송
   * 
   * @return
   */
  @GetMapping(value = "/post2get")
  public String post2get(Model model, @RequestParam(name = "url", defaultValue = "") String url) {
    ArrayList<ClassifyVOMenu> menu = this.classifyProc.menu(); // 중분류
    model.addAttribute("menu", menu);
    ArrayList<GenreVOMenu> menu1 = this.genreProc.menu(); // 대분류
    model.addAttribute("menu1", menu1);

    return url; // forward, /templates/...
  }

  @PostMapping(value = "/create")
  @ResponseBody
  private String create(HttpSession session, @RequestBody Survey_goodVO survey_goodVO) {
    System.out.println(" -> 수신 데이터 : " + survey_goodVO.toString());

//    int memberno = 6;
    int memberno = (int) session.getAttribute("memberno");
    survey_goodVO.setMemberno(memberno);

    int cnt = this.survey_goodProc.create(survey_goodVO);

    JSONObject json = new JSONObject();
    json.put("res", cnt);
    return json.toString();

  }

  @GetMapping(value = "/list_all")
  public String list_all(Model model) {
    ArrayList<ClassifyVOMenu> menu = this.classifyProc.menu(); // 중분류
    model.addAttribute("menu", menu);
    ArrayList<GenreVOMenu> menu1 = this.genreProc.menu(); // 대분류
    model.addAttribute("menu1", menu1);

//    ArrayList<Survey_goodVO> list = this.survey_goodProc.list_all();
//    model.addAttribute("list", list);
    ArrayList<SSdMVO> list = this.survey_goodProc.list_all_join();
    model.addAttribute("list", list);

    return "/survey_good/list_all"; // /templates/surveygood/list_all.html
  }

  @PostMapping(value = "/delete")
  public String delete_proc(HttpSession session, Model model,
      @RequestParam(name = "surveygoodno", defaultValue = "0") int surveygoodno, RedirectAttributes ra) {

    if (this.memberProc.isMemberAdmin(session)) { // 관리자 로그인 확인
      this.survey_goodProc.delete(surveygoodno);
      return "redirect:/survey_good/list_all";

    } else { // 정상적인 로그인이 아닌 경우 로그인 유도
      ra.addAttribute("url", "/member/login_cookie_need"); // /templates/member/login_cookie_need.html
      return "redirect:/survey_good/post2get"; // @GetMapping(value = "/msg")
    }

  }
}