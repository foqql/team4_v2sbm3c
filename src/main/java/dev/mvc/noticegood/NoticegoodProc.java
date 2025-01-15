package dev.mvc.noticegood;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpSession;

import java.util.HashMap;

@Component("dev.mvc.noticegood.NoticegoodProc")
public class NoticegoodProc implements NoticegoodProcInter {
    @Autowired
    private NoticegoodDAOInter noticegoodDAO;

    @Override
    public int noticeCheck(HashMap<String, Object> map) {

        // 교환정보 준비
        NoticegoodVO noticegoodVO = new NoticegoodVO();
        noticegoodVO.setNotino(Integer.parseInt(map.get("notino").toString()));
        noticegoodVO.setMemberno((int) map.get("memberno"));
        // 교환정보 준비

        // 알림 상태에 따라 호출 변경
        int state = this.noticegoodDAO.notice_check(noticegoodVO);
        
        if(map.get("check") != null && map.get("check").equals("check")) {
            return state;

        } else if(state == 0) { // 추가
            int cnt = this.noticegoodDAO.noticegood_insert(noticegoodVO);
            return 1;
            
        } else if(state == 1) { // 삭제
            int cnt = this.noticegoodDAO.noticegood_delete(noticegoodVO);
            return 0;
            
        } else { // 오류
            return 100;
            
        }
        // 알림 상태에 따라 호출 변경
    }
    public boolean isMemberLoggedIn(HttpSession session) {
      return session.getAttribute("memberno") != null;
  }
    
    public int count_likes(int notino) {
      int cnt = this.noticegoodDAO.count_likes(notino);
      return cnt;
    }
}
