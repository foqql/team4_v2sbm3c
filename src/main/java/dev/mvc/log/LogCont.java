package dev.mvc.log;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.ui.Model;

import java.util.List;

@Controller
@RequestMapping("/log")
public class LogCont {

    @Autowired
    private LogProcInter logProc;

    @GetMapping("/list")
    public String getLogs(@RequestParam(name = "userType", required = false) String userType, Model model) {
        List<LogVO> logs = logProc.getLogs(userType);
        model.addAttribute("logs", logs);
        return "log"; // 뷰 이름을 "log"로 변경합니다.
    }

    // 로그 삭제 처리
    @GetMapping("/delete")
    public String deleteLog(@RequestParam("logno") int logno) {
        logProc.deleteLog(logno); // LogProc에 삭제 메서드 호출
        return "redirect:/log/list"; // 삭제 후 로그 목록 페이지로 리다이렉트
    }
}
