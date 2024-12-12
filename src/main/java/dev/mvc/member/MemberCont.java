package dev.mvc.member;

import java.util.ArrayList;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import dev.mvc.classify.ClassifyProcInter;
import dev.mvc.classify.ClassifyVOMenu;


@RequestMapping("/member")
@Controller
public class MemberCont {
  @Autowired
  @Qualifier("dev.mvc.member.MemberProc")  // @Service("dev.mvc.member.MemberProc")
  private MemberProcInter memberProc;
  
  @Autowired
  @Qualifier("ClassifyProc")
  private ClassifyProcInter classifyProc;
  
  public MemberCont() {
    System.out.println("-> MemberCont created.");  
  }
  
  @GetMapping(value="/checkID") // http://localhost:9091/member/checkID?id=admin
  @ResponseBody
  public String checkID(@RequestParam(name="id", defaultValue = "") String id) {    
    System.out.println("-> id: " + id);
    int cnt = this.memberProc.checkID(id);
    
    // return "cnt: " + cnt;
    // return "{\"cnt\": " + cnt + "}";    // {"cnt": 1} JSON
    
    JSONObject obj = new JSONObject();
    obj.put("cnt", cnt);
    
    return obj.toString();
  }
  
  /**
   * 회원 가입 폼
   * @param model
   * @param memberVO
   * @return
   */
  @GetMapping(value="/create") // http://localhost:9093/member/create
  public String create_form(Model model, 
                                      @ModelAttribute("memberVO") MemberVO memberVO) {
    ArrayList<ClassifyVOMenu> menu = this.classifyProc.menu();
    model.addAttribute("menu", menu);
    
    return "/member/create";    // /template/member/create.html
  }
  
}