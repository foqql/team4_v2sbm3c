package dev.mvc.notice;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import dev.mvc.classify.ClassifyProcInter;
import dev.mvc.classify.ClassifyVOMenu;
import dev.mvc.genre.GenreProcInter;
import dev.mvc.genre.GenreVOMenu;
import dev.mvc.noticegood.NoticegoodProcInter;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/notice")
public class NoticeCont {

  @Autowired
  @Qualifier("dev.mvc.genre.GenreProc") // @Component("dev.mvc.exchange.ExchangeProc")
  private GenreProcInter genreProc;
  
  @Autowired
  @Qualifier("dev.mvc.classify.ClassifyProc") // @Component("dev.mvc.classify.ClassifyProc")
  private ClassifyProcInter classifyProc;
  
    @Autowired
    private NoticeProcInter noticeProc;
    
    @Autowired
    @Qualifier("dev.mvc.noticegood.NoticegoodProc")
    private NoticegoodProcInter noticegoodProc;
    
    
    // 공지사항 목록 조회
    @GetMapping("/list")
    public String list(Model model, HttpSession session) {
        List<NoticeVO> list = noticeProc.list();
        model.addAttribute("list", list);
        
        ArrayList<ClassifyVOMenu> menu = this.classifyProc.menu();
        model.addAttribute("menu", menu);
        
        ArrayList<GenreVOMenu> menu1 = this.genreProc.menu(); // 대분류
        model.addAttribute("menu1", menu1);

        // 세션에서 사용자 정보 가져오기
        String grade = (String) session.getAttribute("grade");
        model.addAttribute("userGrade", grade);  // 모델에 등급 정보 추가

        return "notice/list";
    }

    // 공지사항 작성 페이지
    @GetMapping("/create")
    public String createForm(Model model) {
        // 메뉴 데이터 추가
        ArrayList<ClassifyVOMenu> menu = this.classifyProc.menu();
        model.addAttribute("menu", menu);
        
        ArrayList<GenreVOMenu> menu1 = this.genreProc.menu(); // 대분류
        model.addAttribute("menu1", menu1);

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
    public String read(@PathVariable("notino") int notino, Model model, HttpSession session) {
        NoticeVO noticeVO = noticeProc.read(notino);  // DB에서 데이터 조회
        model.addAttribute("noticeVO", noticeVO);  // 모델에 noticeVO 객체 추가
        int cnt = this.noticegoodProc.count_likes(notino);
        model.addAttribute("recom_cnt", cnt);
        
        ArrayList<ClassifyVOMenu> menu = this.classifyProc.menu();
        model.addAttribute("menu", menu);
        
        ArrayList<GenreVOMenu> menu1 = this.genreProc.menu(); // 대분류
        model.addAttribute("menu1", menu1);
        
        HashMap<String, Object> map = new HashMap<>();
        map.put("check", "check");
        map.put("notino", notino);
        map.put("memberno", (int)session.getAttribute("memberno"));
        int state = this.noticegoodProc.noticeCheck(map);
        model.addAttribute("state", state);
        return "notice/read";  // Thymeleaf 템플릿
    }

    // 공지사항 수정 페이지로 이동
    @GetMapping("/update/{notino}")
    public String update(@PathVariable("notino") int notino, Model model) {
        NoticeVO noticeVO = noticeProc.read(notino);
        ArrayList<ClassifyVOMenu> menu = this.classifyProc.menu();
        model.addAttribute("menu", menu);
        
        ArrayList<GenreVOMenu> menu1 = this.genreProc.menu(); // 대분류
        model.addAttribute("menu1", menu1);
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
        noticeProc.delete(notino);  // delete() 메서드 호출
        return "redirect:/notice/list";  // 목록으로 리다이렉트
    }
}
