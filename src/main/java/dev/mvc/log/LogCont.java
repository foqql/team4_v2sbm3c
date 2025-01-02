package dev.mvc.log;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/log")
public class LogCont {

    @Autowired
    private LogProcInter logProc;

    @GetMapping("/list")
    public String getLogsPage() {
        return "log"; // Ajax가 데이터를 가져올 수 있도록, 기본 페이지는 그대로 반환합니다.
    }

    @GetMapping("/list-ajax")
    @ResponseBody
    public List<LogVO> getLogs(@RequestParam(name = "userType", required = false) String userType) {
        return logProc.getLogs(userType); // 클라이언트에 JSON으로 로그 목록 반환
    }

    @GetMapping("/delete")
    public String deleteLog(@RequestParam("logno") int logno) {
        logProc.deleteLog(logno); // 로그 삭제 처리
        return "redirect:/log/list"; // 삭제 후 로그 목록 페이지로 리다이렉트
    }
}
