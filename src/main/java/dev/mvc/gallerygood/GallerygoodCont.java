package dev.mvc.gallerygood;

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
@RequestMapping(value = "/gallerygood")
public class GallerygoodCont {
  @Autowired
  @Qualifier("dev.mvc.member.MemberProc") // @Service("dev.mvc.member.MemberProc")
  private MemberProcInter memberProc;

  @Autowired
  @Qualifier("dev.mvc.gallerygood.GallerygoodProc")
  private GallerygoodProcInter gallerygoodProc;

  @Autowired
  @Qualifier("dev.mvc.classify.ClassifyProc")
  private ClassifyProcInter classifyProc;

  @Autowired
  @Qualifier("dev.mvc.genre.GenreProc")
  private GenreProcInter genreProc;

  public GallerygoodCont() {
    System.out.println("-> GallerygoodCont created.");
  }

  /**
   * POST 요청시 새로고침 방지, POST 요청 처리 완료 → redirect → url → GET → forward -> html 데이터
   * 전송
   * 
   * @return
   */
  @GetMapping(value = "/post2get")
  public String post2get(Model model, @RequestParam(name = "url", defaultValue = "") String url) {
    ArrayList<ClassifyVOMenu> menu = this.classifyProc.menu();
    model.addAttribute("menu", menu);

    return url; 
  }

  @PostMapping(value = "/create")
  @ResponseBody
  public String create(HttpSession session, @RequestBody GallerygoodVO gallerygoodVO) {
    System.out.println("-> 수신 데이터: " + gallerygoodVO.toString());

    int memberno = 1;
    // int memberno = (int)session.getAttribute("memberno"); // 보안성 향상
    gallerygoodVO.setMemberno(memberno);

    int cnt = this.gallerygoodProc.create(gallerygoodVO);

    JSONObject json = new JSONObject();
    json.put("res", cnt);

    return json.toString();
  }
  
  /**
   * 목록 join
   * 
   * @param model
   * @return
   */
  // http://localhost:9091/cate/list_all
  @GetMapping(value = "/list_all")
  public String list_all_join(Model model) {
    ArrayList<GalleryGallerygoodMemberVO> list = this.gallerygoodProc.list_all_join();
    model.addAttribute("list", list);

    ArrayList<ClassifyVOMenu> menu = this.classifyProc.menu(); // 중분류
    model.addAttribute("menu", menu);

    ArrayList<GenreVOMenu> menu1 = this.genreProc.menu(); // 대분류
    model.addAttribute("menu1", menu1);

    return "/gallerygood/list_all"; // /templates/calendar/list_all.html
  }


  /**
   * 삭제 처리 http://localhost:9091/gallerygood/delete?gallerygoodno=1
   * 
   * @return
   */
  @PostMapping(value = "/delete")
  public String delete_proc(HttpSession session, Model model,
      @RequestParam(name = "gallerygoodno", defaultValue = "0") int gallerygoodno, RedirectAttributes ra) {

    if (this.memberProc.isMemberAdmin(session)) { // 관리자 로그인 확인
      this.gallerygoodProc.delete(gallerygoodno);

      return "redirect:/gallerygood/list_all";

    } else { // 정상적인 로그인이 아닌 경우 로그인 유도
      ra.addAttribute("url", "/member/login_cookie_need"); // /templates/member/login_cookie_need.html
      return "redirect:/contentsgood/post2get"; // @GetMapping(value = "/msg")
    }

  }

}