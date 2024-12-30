package dev.mvc.chat;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/chat")
public class ChatCont {
    @Autowired
    private ChatProcInter chatProc;

    @GetMapping("/list")
    public String list(Model model, HttpSession session) {
        List<ChatVO> list = chatProc.list();
        model.addAttribute("chatList", list);
        
        // 세션에서 사용자 정보 추가
        model.addAttribute("memberno", session.getAttribute("memberno"));
        model.addAttribute("grade", session.getAttribute("grade"));
        model.addAttribute("id", session.getAttribute("id"));
        
        // 로그인된 계정의 등급을 모델에 추가
        model.addAttribute("loggedInUserGrade", session.getAttribute("grade"));
        
        return "chat";
    }

    @PostMapping("/create")
    public String create(@ModelAttribute ChatVO chatVO, HttpSession session) {
        if (session.getAttribute("memberno") == null) {
            return "redirect:/member/login";  // 로그인 페이지로 리디렉션
        }
        String id = (String) session.getAttribute("id");
        int memberno = (int) session.getAttribute("memberno");

        chatVO.setMemberno(memberno);
        chatVO.setId(id);

        chatProc.create(chatVO);
        return "redirect:/chat/list";
    }

    @PostMapping("/delete/{chatno}")
    public String delete(@PathVariable("chatno") int chatno, HttpSession session) {
        String grade = (String) session.getAttribute("grade");
        if (grade != null && grade.equals("admin")) {
            chatProc.delete(chatno);
        }
        return "redirect:/chat/list";
    }
}
