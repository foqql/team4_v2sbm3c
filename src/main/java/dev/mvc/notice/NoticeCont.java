package dev.mvc.notice;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;

import org.springframework.http.ResponseEntity;

@Controller
@RequestMapping("/notice")
public class NoticeCont {

    @Autowired
    private NoticeProcInter noticeProc;

    // 공지사항 목록 조회
    @GetMapping("/list")
    public String list(Model model, HttpSession session) {
        List<NoticeVO> list = noticeProc.list();
        model.addAttribute("list", list);

        // 세션에서 사용자 정보 가져오기
        String grade = (String) session.getAttribute("grade");
        model.addAttribute("userGrade", grade);  // 모델에 등급 정보 추가

        return "notice/list";
    }

    // 공지사항 작성 페이지
    @GetMapping("/create")
    public String createForm() {
        return "notice/create";
    }

    // 공지사항 작성 처리
    @PostMapping("/create")
    public String create(NoticeVO noticeVO) {
        noticeProc.create(noticeVO);
        return "redirect:/notice/list";
    }

    // 공지사항 읽기
    @GetMapping("/read/{notino}")
    public String read(@PathVariable("notino") int notino, Model model) {
        NoticeVO noticeVO = noticeProc.read(notino);  // DB에서 데이터 조회
        model.addAttribute("noticeVO", noticeVO);  // 모델에 noticeVO 객체 추가
        return "notice/read";  // Thymeleaf 템플릿
    }

    // 공지사항 수정 페이지로 이동
    @GetMapping("/update/{notino}")
    public String update(@PathVariable("notino") int notino, Model model) {
        NoticeVO noticeVO = noticeProc.read(notino);
        model.addAttribute("noticeVO", noticeVO);
        return "notice/update";  // 수정 페이지로 이동
    }

    // 공지사항 수정 처리
    @PostMapping("/update")
    public String update(NoticeVO noticeVO) {
        // noticeVO를 DB에 업데이트합니다.
        int result = noticeProc.update(noticeVO);
        
        // 성공적으로 수정된 후 목록 페이지로 리다이렉트
        return "redirect:/notice/list";
    }

    // 공지사항 삭제
    @GetMapping("/delete/{notino}")
    public String delete(@PathVariable("notino") int notino) {
        noticeProc.delete(notino);
        return "redirect:/notice/list";
    }

    // Ajax: 공지사항 목록 가져오기
    @GetMapping("/list/json")
    @ResponseBody
    public ResponseEntity<List<NoticeVO>> getNotices() {
        List<NoticeVO> list = noticeProc.list();
        return ResponseEntity.ok(list);
    }

    // Ajax: 공지사항 삭제
    @DeleteMapping("/delete/json/{notino}")
    @ResponseBody
    public ResponseEntity<String> deleteNotice(@PathVariable int notino) {
        int result = noticeProc.delete(notino);
        if (result > 0) {
            return ResponseEntity.ok("삭제 완료");
        } else {
            return ResponseEntity.status(400).body("삭제 실패");
        }
    }
}
