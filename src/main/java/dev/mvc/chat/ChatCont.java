package dev.mvc.chat;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import dev.mvc.classify.ClassifyProc;
import dev.mvc.classify.ClassifyVOMenu;
import dev.mvc.genre.GenreProc;
import dev.mvc.genre.GenreVOMenu;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/chat")
public class ChatCont {
    @Autowired
    private ChatProcInter chatProc;
    @Autowired
    private ClassifyProc classifyProc;
    @Autowired
    private GenreProc genreProc;

    private static final String UPLOAD_DIR = "C:/uploads"; // 이미지 업로드 디렉토리
    private static final String DEFAULT_IMAGE_PATH = "/static/css/images/s1234.jpg"; // 기본 이미지 경로

    @GetMapping("/list")
    public String list(Model model, HttpSession session) {
        List<ChatVO> list = chatProc.list();
        model.addAttribute("chatList", list);
        ArrayList<ClassifyVOMenu> menu = this.classifyProc.menu(); // 중분류
        model.addAttribute("menu", menu);
        ArrayList<GenreVOMenu> menu1 = this.genreProc.menu(); // 대분류
        model.addAttribute("menu1", menu1);
        // 세션에서 사용자 정보 추가
        model.addAttribute("memberno", session.getAttribute("memberno"));
        model.addAttribute("grade", session.getAttribute("grade"));
        model.addAttribute("id", session.getAttribute("id"));
        
        // 로그인된 계정의 등급을 모델에 추가
        model.addAttribute("loggedInUserGrade", session.getAttribute("grade"));
        
        return "chat";
    }

    @GetMapping("/history")
    public String history(Model model, HttpSession session) {
        List<ChatVO> list = chatProc.list();
        model.addAttribute("chatList", list);
        model.addAttribute("loggedInUserGrade", session.getAttribute("grade")); // 로그인된 계정의 등급 추가
        return "chatHistory"; // HTML 파일 이름
    }

    @PostMapping("/create")
    public String create(@ModelAttribute ChatVO chatVO, @RequestParam("image") MultipartFile imageFile, HttpSession session) {
        if (session.getAttribute("memberno") == null) {
            return "redirect:/member/login"; // 로그인 페이지로 리디렉션
        }
        String id = (String) session.getAttribute("id");
        int memberno = (int) session.getAttribute("memberno");
        chatVO.setMemberno(memberno);
        chatVO.setId(id);

        if (!imageFile.isEmpty()) {
            // 이미지 파일을 저장하고 URL을 가져옴
            String imageUrl = saveImageFile(imageFile);
            chatVO.setImageUrl(imageUrl);
        } else {
            chatVO.setImageUrl(DEFAULT_IMAGE_PATH); // 이미지가 없는 경우 기본 이미지 경로로 설정
        }

        chatProc.create(chatVO);
        return "redirect:/chat/list";
    }

    private String saveImageFile(MultipartFile file) {
        String uploadDir = "C:\\uploads\\";
        String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
        File uploadDirFile = new File(uploadDir);
        if (!uploadDirFile.exists()) {
            uploadDirFile.mkdirs(); // 디렉토리가 존재하지 않으면 생성
        }
        File uploadFile = new File(uploadDir + fileName);
        try {
            file.transferTo(uploadFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "/uploads/" + fileName;
    }

    @PostMapping("/delete/{chatno}")
    public String delete(@PathVariable("chatno") int chatno, HttpSession session) {
        String grade = (String) session.getAttribute("grade");
        if (grade != null && grade.equals("admin")) {
            chatProc.delete(chatno);
        }
        return "redirect:/chat/history"; // 채팅 히스토리 페이지로 리디렉션
    }

    // 이미지 파일을 지정된 폴더에 저장하고 URL을 반환
    private String saveImage(MultipartFile image) throws IOException {
        String originalFilename = image.getOriginalFilename();
        String targetLocation = UPLOAD_DIR + File.separator + originalFilename;
        File targetFile = new File(targetLocation);

        image.transferTo(targetFile); // 이미지를 디스크에 저장

        return "/uploads/" + originalFilename; // 이미지 URL 경로 반환
    }
}


