package dev.mvc.chat;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/chat")
public class ChatCont {
    @Autowired
    private ChatProcInter chatProc;

    private static final String UPLOAD_DIR = "C:/uploads"; // 이미지 업로드 디렉토리

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
            chatVO.setImageUrl(""); // 이미지가 없는 경우 빈 문자열로 설정
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
        return "redirect:/chat/list";
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
