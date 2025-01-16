package dev.mvc.noticegood;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.fasterxml.jackson.databind.ObjectMapper;

import dev.mvc.member.MemberProcInter;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping(value = "/noticegood")
public class NoticegoodCont {
  
  @Autowired
  @Qualifier("dev.mvc.member.MemberProc")
  private MemberProcInter memberProc;
  
  @Autowired
  @Qualifier("dev.mvc.noticegood.NoticegoodProc")
  private NoticegoodProcInter noticegoodProc;
  /**
   * 즐겨찾기 추가/삭제
   */
  @PostMapping(value="/ngood")
  @ResponseBody
  public Map<String, Object> notice_work(HttpSession session, RedirectAttributes ra,
      @RequestBody String json_src){
    
    
    ObjectMapper objectMapper = new ObjectMapper();
    Map<String, Object> response = new HashMap<>();
    
    if (this.memberProc.isMember(session)) { 
      try {
        // JSON 문자열을 HashMap으로 변환
        HashMap<String, Object> map = objectMapper.readValue(json_src, HashMap.class);
        map.put("memberno", (int) session.getAttribute("memberno"));
        // int state = this.noticegoodProc.notice_check(map);
        // response.put("state", state);
        // int cnt = this.asdfsadf; 개추:1, 개추취소:0
        int state = this.noticegoodProc.noticeCheck(map);
        response.put("state", state);
        System.out.println("state->" + state);
        
        int noticeno = Integer.parseInt(map.get("notino").toString());
        int cnt = this.noticegoodProc.count_likes(noticeno);
        System.out.println("cnt->" + cnt);
        response.put("cnt", cnt);
        
        // int 개추 =  this.개추갯수 가져오는 xml.(noticeno);
        // response.put("개추", 개추);
        
      } catch (IOException e) {
        e.printStackTrace();
        response.put("error", e);
      } catch (Exception e) {
        e.printStackTrace();
        response.put("error", e);
      }
    } else {
      response.put("url", "/member/login_cookie_need");      
    }
    

    return response;
  }
  
}