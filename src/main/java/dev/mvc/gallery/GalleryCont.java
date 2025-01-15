package dev.mvc.gallery;

import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import dev.mvc.classify.ClassifyProcInter;
import dev.mvc.classify.ClassifyVO;
import dev.mvc.classify.ClassifyVOMenu;
import dev.mvc.gallerygood.GallerygoodProcInter;
import dev.mvc.genre.GenreProcInter;
import dev.mvc.genre.GenreVOMenu;
import dev.mvc.member.MemberProcInter;
import dev.mvc.tool.Tool;
import dev.mvc.tool.Upload;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@RequestMapping(value = "/gallery")
@Controller
public class GalleryCont {
  @Autowired
  @Qualifier("dev.mvc.member.MemberProc") // @Service("dev.mvc.member.MemberProc")
  private MemberProcInter memberProc;

  @Autowired
  @Qualifier("dev.mvc.classify.ClassifyProc") // @Component("dev.mvc.classify.ClassifyProc")
  private ClassifyProcInter classifyProc;
  
  @Autowired
  @Qualifier("dev.mvc.genre.GenreProc") // @Component("dev.mvc.exchange.ExchangeProc")
  private GenreProcInter genreProc;
  
  @Autowired
  @Qualifier("dev.mvc.gallerygood.GallerygoodProc") // @Component("dev.mvc.weather.WeatherProc")
  private GallerygoodProcInter gallerygoodProc;

  @Autowired
  @Qualifier("dev.mvc.gallery.GalleryProc") // @Component("dev.mvc.gallery.GalleryProc")
  private GalleryProcInter galleryProc;

  public GalleryCont() {
    System.out.println("-> GalleryCont created.");
  }
  
  
  /**
   * POST 요청시 새로고침 방지, POST 요청 처리 완료 → redirect → url → GET → forward -> html 데이터
   * 전송
   * 
   * @return
   */
  @GetMapping(value = "/post2get")
  public String post2get(Model model, 
      @RequestParam(name="url", defaultValue = "") String url) {
    ArrayList<ClassifyVOMenu> menu = this.classifyProc.menu();
    model.addAttribute("menu", menu);

    return url; // forward, /templates/...
  }

  // 등록 폼, gallery 테이블은 FK로 classifyno를 사용함.
  // http://localhost:9091/gallery/create X
  // http://localhost:9091/gallery/create?classifyno=1 // classifyno 변수값을 보내는 목적
  // http://localhost:9091/gallery/create?classifyno=2
  // http://localhost:9091/gallery/create?classifyno=5
  @GetMapping(value = "/create")
  public String create(Model model, 
      @ModelAttribute("galleryVO") GalleryVO galleryVO, 
      @RequestParam(name="classifyno", defaultValue="0") int classifyno) {
    ArrayList<ClassifyVOMenu> menu = this.classifyProc.menu();
    model.addAttribute("menu", menu);

    ClassifyVO classifyVO = this.classifyProc.read(classifyno); // 카테고리 정보를 출력하기위한 목적
    model.addAttribute("classifyVO", classifyVO);

    return "/gallery/create"; // /templates/gallery/create.html
  }

  /**
   * 등록 처리 http://localhost:9091/gallery/create
   * 
   * @return
   */
  @PostMapping(value = "/create")
  public String create(HttpServletRequest request, 
      HttpSession session, 
      Model model, 
      @ModelAttribute("galleryVO") GalleryVO galleryVO,
      RedirectAttributes ra) {

    if (memberProc.isMemberAdmin(session)) { // 관리자로 로그인한경우
      // ------------------------------------------------------------------------------
      // 파일 전송 코드 시작
      // ------------------------------------------------------------------------------
      String file1 = ""; // 원본 파일명 image
      String file1saved = ""; // 저장된 파일명, image
      String thumb1 = ""; // preview image

      String upDir = Gallery.getUploadDir(); // 파일을 업로드할 폴더 준비
      // upDir = upDir + "/" + 한글을 제외한 카테고리 이름
      System.out.println("-> upDir: " + upDir);

      // 전송 파일이 없어도 file1MF 객체가 생성됨.
      // <input type='file' class="form-control" name='file1MF' id='file1MF'
      // value='' placeholder="파일 선택">
      MultipartFile mf = galleryVO.getFile1MF();

      file1 = mf.getOriginalFilename(); // 원본 파일명 산출, 01.jpg
      System.out.println("-> 원본 파일명 산출 file1: " + file1);

      long size1 = mf.getSize(); // 파일 크기
      if (size1 > 0) { // 파일 크기 체크, 파일을 올리는 경우
        if (Tool.checkUploadFile(file1) == true) { // 업로드 가능한 파일인지 검사
          // 파일 저장 후 업로드된 파일명이 리턴됨, spring.jsp, spring_1.jpg, spring_2.jpg...
          file1saved = Upload.saveFileSpring(mf, upDir);

          if (Tool.isImage(file1saved)) { // 이미지인지 검사
            // thumb 이미지 생성후 파일명 리턴됨, width: 200, height: 150
            thumb1 = Tool.preview(upDir, file1saved, 200, 150);
          }

          galleryVO.setFile1(file1); // 순수 원본 파일명
          galleryVO.setFile1saved(file1saved); // 저장된 파일명(파일명 중복 처리)
          galleryVO.setThumb1(thumb1); // 원본이미지 축소판
          galleryVO.setSize1(size1); // 파일 크기

        } else { // 전송 못하는 파일 형식
          ra.addFlashAttribute("code", "check_upload_file_fail"); // 업로드 할 수 없는 파일
          ra.addFlashAttribute("cnt", 0); // 업로드 실패
          ra.addFlashAttribute("url", "/gallery/msg"); // msg.html, redirect parameter 적용
          return "redirect:/gallery/msg"; // Post -> Get - param...
        }
      } else { // 글만 등록하는 경우
        System.out.println("-> 글만 등록");
      }

      // ------------------------------------------------------------------------------
      // 파일 전송 코드 종료
      // ------------------------------------------------------------------------------

      // Call By Reference: 메모리 공유, Hashcode 전달
      int memberno = (int) session.getAttribute("memberno"); // memberno FK
      galleryVO.setMemberno(memberno);
      int cnt = this.galleryProc.create(galleryVO);

      // ------------------------------------------------------------------------------
      // PK의 return
      // ------------------------------------------------------------------------------
      // System.out.println("--> galleryno: " + galleryVO.getGalleryno());
      // mav.addObject("galleryno", galleryVO.getGalleryno()); // redirect
      // parameter 적용
      // ------------------------------------------------------------------------------

      if (cnt == 1) {


        ra.addAttribute("classifyno", galleryVO.getClassifyno()); // controller -> controller: O
        return "redirect:/gallery/list_by_classifyno";

      } else {
        ra.addFlashAttribute("code", "create_fail"); // DBMS 등록 실패
        ra.addFlashAttribute("cnt", 0); // 업로드 실패
        ra.addFlashAttribute("url", "/gallery/msg"); // msg.html, redirect parameter 적용
        return "redirect:/gallery/msg"; // Post -> Get - param...
      }
    } else { // 로그인 실패 한 경우
      return "redirect:/member/login_cookie_need"; // /member/login_cookie_need.html
    }
  }

  /**
   * 전체 목록, 관리자만 사용 가능 http://localhost:9091/gallery/list_all
   * 
   * @return
   */
  @GetMapping(value = "/list_all")
  public String list_all(HttpSession session, Model model) {
    // System.out.println("-> list_all");
    ArrayList<ClassifyVOMenu> menu = this.classifyProc.menu();
    model.addAttribute("menu", menu);

    if (this.memberProc.isMemberAdmin(session)) { // 관리자만 조회 가능
      ArrayList<GalleryVO> list = this.galleryProc.list_all(); // 모든 목록

      // Thymeleaf는 CSRF(크로스사이트) 스크립팅 해킹 방지 자동 지원
      // for문을 사용하여 객체를 추출, Call By Reference 기반의 원본 객체 값 변경
//      for (GalleryVO galleryVO : list) {
//        String title = galleryVO.getTitle();
//        String content = galleryVO.getContent();
//        
//        title = Tool.convertChar(title);  // 특수 문자 처리
//        content = Tool.convertChar(content); 
//        
//        galleryVO.setTitle(title);
//        galleryVO.setContent(content);  
//
//      }

      model.addAttribute("list", list);
      return "/gallery/list_all";

    } else {
      return "redirect:/member/login_cookie_need";

    }

  }

  /**
   * 유형 3
   * 카테고리별 목록 + 검색 + 페이징 http://localhost:9091/gallery/list_by_classifyno?classifyno=5
   * http://localhost:9091/gallery/list_by_classifyno?classifyno=6
   * 
   * @return
   */
  @GetMapping(value = "/list_by_classifyno")
  public String list_by_classifyno_search_paging(HttpSession session, Model model, 
      @RequestParam(name = "classifyno", defaultValue = "1") int classifyno,
      @RequestParam(name = "word", defaultValue = "") String word,
      @RequestParam(name = "now_page", defaultValue = "1") int now_page) {

    // System.out.println("-> classifyno: " + classifyno);

    ArrayList<ClassifyVOMenu> menu = this.classifyProc.menu();
    model.addAttribute("menu", menu);
    
    ArrayList<GenreVOMenu> menu1 = this.genreProc.menu(); // 대분류
    model.addAttribute("menu1", menu1);

    ClassifyVO classifyVO = this.classifyProc.read(classifyno);
    model.addAttribute("classifyVO", classifyVO);

    word = Tool.checkNull(word).trim();

    HashMap<String, Object> map = new HashMap<>();
    System.out.println("classifyno->" + classifyno);
    map.put("classifyno", classifyno);
    map.put("word", word);
    map.put("now_page", now_page);

    
    ArrayList<GalleryVO> list = this.galleryProc.list_by_classifyno_search_paging(map);
    model.addAttribute("list", list);
    System.out.println("-> list:" + list);

    // System.out.println("-> size: " + list.size());
    model.addAttribute("word", word);
    
    int search_count = this.galleryProc.list_by_classifyno_search_count(map);
    String paging = this.galleryProc.pagingBox(classifyno, now_page, word, "/gallery/list_by_classifyno", search_count,
        Gallery.RECORD_PER_PAGE, Gallery.PAGE_PER_BLOCK);
    model.addAttribute("paging", paging);
    model.addAttribute("now_page", now_page);

    model.addAttribute("search_count", search_count);

    // 일련 변호 생성: 레코드 갯수 - ((현재 페이지수 -1) * 페이지당 레코드 수)
    int no = search_count - ((now_page - 1) * Gallery.RECORD_PER_PAGE);
    model.addAttribute("no", no);

    return "/gallery/list_by_classifyno_search_paging"; // /templates/gallery/list_by_classifyno_search_paging.html
  }

  /**
   * 카테고리별 목록 + 검색 + 페이징 + Grid
   * http://localhost:9091/gallery/list_by_classifyno?classifyno=5
   * http://localhost:9091/gallery/list_by_classifyno?classifyno=6
   * 
   * @return
   */
  @GetMapping(value = "/list_by_classifyno_grid")
  public String list_by_classifyno_search_paging_grid(HttpSession session, Model model, 
      @RequestParam(name = "galleryno", defaultValue = "0") int galleryno,
      @RequestParam(name = "classifyno", defaultValue = "0") int classifyno,
      @RequestParam(name = "word", defaultValue = "") String word,
      @RequestParam(name = "now_page", defaultValue = "1") int now_page) {

    // System.out.println("-> classifyno: " + classifyno);

    ArrayList<ClassifyVOMenu> menu = this.classifyProc.menu();
    model.addAttribute("menu", menu);
    
    ArrayList<GenreVOMenu> menu1 = this.genreProc.menu(); // 대분류
    model.addAttribute("menu1", menu1);

    ClassifyVO classifyVO = this.classifyProc.read(classifyno);
    model.addAttribute("classifyVO", classifyVO);

    
    GalleryVO galleryVO = this.galleryProc.read(galleryno);
    model.addAttribute("galleryVO", galleryVO);
    


    word = Tool.checkNull(word).trim();

    HashMap<String, Object> map = new HashMap<>();
    map.put("classifyno", classifyno);
    map.put("word", word);
    map.put("now_page", now_page);

    ArrayList<GalleryVO> list = this.galleryProc.list_by_classifyno_search_paging(map);
    model.addAttribute("list", list);

    // System.out.println("-> size: " + list.size());
    model.addAttribute("word", word);

    int search_count = this.galleryProc.list_by_classifyno_search_count(map);
    String paging = this.galleryProc.pagingBox(classifyno, now_page, word, "/gallery/list_by_classifyno_grid", search_count,
        Gallery.RECORD_PER_PAGE, Gallery.PAGE_PER_BLOCK);
    model.addAttribute("paging", paging);
    model.addAttribute("now_page", now_page);

    model.addAttribute("search_count", search_count);

    // 일련 변호 생성: 레코드 갯수 - ((현재 페이지수 -1) * 페이지당 레코드 수)
    int no = search_count - ((now_page - 1) * Gallery.RECORD_PER_PAGE);
    model.addAttribute("no", no);

    
    // -------------------------------------------------------------------------------------------
    // 추천 관련
    // -------------------------------------------------------------------------------------------
    HashMap<String, Object> map2 = new HashMap<String, Object>();
    map2.put("galleryno", galleryno);

    int hartCnt = 0;
    if (session.getAttribute("memberno") != null) { // 회원인 경우만 카운트 처리
      int memberno = (int) session.getAttribute("memberno");
      map2.put("memberno", memberno);

      System.out.println("->recom: " + galleryVO.getRecom());
      System.out.println("->galleryno: " + galleryno);
      System.out.println("->memberno: " + memberno);

      hartCnt = this.gallerygoodProc.hartCnt(map2);

    }
    model.addAttribute("hartCnt", hartCnt);
    // ---------------------------------------------------------------
    
    return "/gallery/list_by_classifyno_search_paging_grid";
  }

  /**
   * 조회 http://localhost:9091/gallery/read?galleryno=17
   * 
   * @return
   */
  @GetMapping(value = "/read")
  public String read(HttpSession session, Model model, 
      @RequestParam(name="galleryno", defaultValue = "0") int galleryno, 
      @RequestParam(name="word", defaultValue = "") String word, 
      @RequestParam(name="now_page", defaultValue = "1") int now_page) {
    
    ArrayList<ClassifyVOMenu> menu = this.classifyProc.menu();
    model.addAttribute("menu", menu);

    GalleryVO galleryVO = this.galleryProc.read(galleryno);

//    String title = galleryVO.getTitle();
//    String content = galleryVO.getContent();
//    
//    title = Tool.convertChar(title);  // 특수 문자 처리
//    content = Tool.convertChar(content); 
//    
//    galleryVO.setTitle(title);
//    galleryVO.setContent(content);  

    long size1 = galleryVO.getSize1();
    String size1_label = Tool.unit(size1);
    galleryVO.setSize1_label(size1_label);

    model.addAttribute("galleryVO", galleryVO);

    ClassifyVO classifyVO = this.classifyProc.read(galleryVO.getClassifyno());
    model.addAttribute("classifyVO", classifyVO);

    // 조회에서 화면 하단에 출력
    // ArrayList<ReplyVO> reply_list = this.replyProc.list_gallery(galleryno);
    // mav.addObject("reply_list", reply_list);

    model.addAttribute("word", word);
    model.addAttribute("now_page", now_page);
    
    // -------------------------------------------------------------------------------------------
    // 추천 관련
    // -------------------------------------------------------------------------------------------
    HashMap<String, Object> map = new HashMap<String, Object>();
    map.put("galleryno", galleryno);
    
//    int hartCnt = 0;
//    if (session.getAttribute("memberno") != null ) { // 회원인 경우만 카운트 처리
//      int memberno = (int)session.getAttribute("memberno");
//      map.put("memberno", memberno);
//      
//      hartCnt = this.gallerygoodProc.hartCnt(map);
//    } 
    
//    model.addAttribute("hartCnt", hartCnt);
    // -------------------------------------------------------------------------------------------
    
    return "/gallery/read";
  }

  /**
   * 맵 등록/수정/삭제 폼 http://localhost:9091/gallery/map?galleryno=19
   * 
   * @return
   */
  @GetMapping(value = "/map")
  public String map(Model model, 
                            @RequestParam(name="galleryno", defaultValue="0") int galleryno) {
    ArrayList<ClassifyVOMenu> menu = this.classifyProc.menu();
    model.addAttribute("menu", menu);

    GalleryVO galleryVO = this.galleryProc.read(galleryno); // map 정보 읽어 오기
    model.addAttribute("galleryVO", galleryVO); // request.setAttribute("galleryVO", galleryVO);

    ClassifyVO classifyVO = this.classifyProc.read(galleryVO.getClassifyno()); // 그룹 정보 읽기
    model.addAttribute("classifyVO", classifyVO);

    return "/gallery/map"; // //templates/gallery/map.html
  }




  /**
   * 수정 폼 http:// localhost:9091/gallery/update_text?galleryno=1
   *
   */
  @GetMapping(value = "/update_text")
  public String update_text(HttpSession session, Model model, 
      @RequestParam(name="galleryno", defaultValue = "0") int galleryno, RedirectAttributes ra, 
      @RequestParam(name="word", defaultValue = "") String word,
      @RequestParam(name="now_page", defaultValue = "0") int now_page) {
    
    ArrayList<ClassifyVOMenu> menu = this.classifyProc.menu();
    model.addAttribute("menu", menu);

    model.addAttribute("word", word);
    model.addAttribute("now_page", now_page);

    if (this.memberProc.isMemberAdmin(session)) { // 관리자로 로그인한경우
      GalleryVO galleryVO = this.galleryProc.read(galleryno);
      model.addAttribute("galleryVO", galleryVO);

      ClassifyVO classifyVO = this.classifyProc.read(galleryVO.getClassifyno());
      model.addAttribute("classifyVO", classifyVO);

      return "/gallery/update_text"; // /templates/gallery/update_text.html

    } else {

      return "/member/login_cookie_need"; // /templates/member/login_cookie_need.html
    }

  }

  /**
   * 수정 처리 http://localhost:9091/gallery/update_text?galleryno=1
   * 
   * @return
   */
  @PostMapping(value = "/update_text")
  public String update_text(HttpSession session, Model model, RedirectAttributes ra,
      @ModelAttribute("galleryVO") GalleryVO galleryVO,
      @RequestParam(name="search_word", defaultValue = "") String search_word, // galleryVO.word와 구분 필요
      @RequestParam(name="now_page", defaultValue = "0") int now_page) {

    ra.addAttribute("word", search_word);
    ra.addAttribute("now_page", now_page);

    if (this.memberProc.isMemberAdmin(session)) { // 관리자 로그인 확인
      HashMap<String, Object> map = new HashMap<String, Object>();
      map.put("galleryno", galleryVO.getGalleryno());

      this.galleryProc.update_text(galleryVO); // 글수정

      // mav 객체 이용
      ra.addAttribute("galleryno", galleryVO.getGalleryno());
      ra.addAttribute("classifyno", galleryVO.getClassifyno());
      
      return "redirect:/gallery/read"; // @GetMapping(value = "/read")
    }
    
    // 관리자 외의 경우 로그인 페이지로 리다이렉트
    ra.addAttribute("url", "/member/login_cookie_need"); // /templates/member/login_cookie_need.html
    return "redirect:/gallery/post2get"; // @GetMapping(value = "/msg")
  }


  /**
   * 파일 수정 폼 http://localhost:9091/gallery/update_file?galleryno=1
   * 
   * @return
   */
  @GetMapping(value = "/update_file")
  public String update_file(HttpSession session, Model model, 
                                     @RequestParam(name="galleryno", defaultValue = "0") int galleryno,
                                     @RequestParam(name="word", defaultValue = "") String word, 
                                     @RequestParam(name="now_page", defaultValue = "1") int now_page) {
    ArrayList<ClassifyVOMenu> menu = this.classifyProc.menu();
    model.addAttribute("menu", menu);
    
    model.addAttribute("word", word);
    model.addAttribute("now_page", now_page);
    
    GalleryVO galleryVO = this.galleryProc.read(galleryno);
    model.addAttribute("galleryVO", galleryVO);

    ClassifyVO classifyVO = this.classifyProc.read(galleryVO.getClassifyno());
    model.addAttribute("classifyVO", classifyVO);

    return "/gallery/update_file";

  }

  /**
   * 파일 수정 처리 http://localhost:9091/gallery/update_file
   * 
   * @return
   */
  @PostMapping(value = "/update_file")
  public String update_file(HttpSession session, Model model, RedirectAttributes ra,
                                     @ModelAttribute("galleryVO") GalleryVO galleryVO,
                                     @RequestParam(name="word", defaultValue = "") String word, 
                                     @RequestParam(name="now_page", defaultValue = "1") int now_page) {
    if (this.memberProc.isMemberAdmin(session)) {
      // 삭제할 파일 정보를 읽어옴, 기존에 등록된 레코드 저장용
      GalleryVO galleryVO_old = galleryProc.read(galleryVO.getGalleryno());

      // -------------------------------------------------------------------
      // 파일 삭제 시작
      // -------------------------------------------------------------------
      String file1saved = galleryVO_old.getFile1saved(); // 실제 저장된 파일명
      String thumb1 = galleryVO_old.getThumb1(); // 실제 저장된 preview 이미지 파일명
      long size1 = 0;

      String upDir = Gallery.getUploadDir(); // C:/kd/deploy/resort_v4sbm3c/gallery/storage/

      Tool.deleteFile(upDir, file1saved); // 실제 저장된 파일삭제
      Tool.deleteFile(upDir, thumb1); // preview 이미지 삭제
      // -------------------------------------------------------------------
      // 파일 삭제 종료
      // -------------------------------------------------------------------

      // -------------------------------------------------------------------
      // 파일 전송 시작
      // -------------------------------------------------------------------
      String file1 = ""; // 원본 파일명 image

      // 전송 파일이 없어도 file1MF 객체가 생성됨.
      // <input type='file' class="form-control" name='file1MF' id='file1MF'
      // value='' placeholder="파일 선택">
      MultipartFile mf = galleryVO.getFile1MF();

      file1 = mf.getOriginalFilename(); // 원본 파일명
      size1 = mf.getSize(); // 파일 크기

      if (size1 > 0) { // 폼에서 새롭게 올리는 파일이 있는지 파일 크기로 체크 ★
        // 파일 저장 후 업로드된 파일명이 리턴됨, spring.jsp, spring_1.jpg...
        file1saved = Upload.saveFileSpring(mf, upDir);

        if (Tool.isImage(file1saved)) { // 이미지인지 검사
          // thumb 이미지 생성후 파일명 리턴됨, width: 250, height: 200
          thumb1 = Tool.preview(upDir, file1saved, 250, 200);
        }

      } else { // 파일이 삭제만 되고 새로 올리지 않는 경우
        file1 = "";
        file1saved = "";
        thumb1 = "";
        size1 = 0;
      }

      galleryVO.setFile1(file1);
      galleryVO.setFile1saved(file1saved);
      galleryVO.setThumb1(thumb1);
      galleryVO.setSize1(size1);
      // -------------------------------------------------------------------
      // 파일 전송 코드 종료
      // -------------------------------------------------------------------

      this.galleryProc.update_file(galleryVO); // Oracle 처리
      ra.addAttribute ("galleryno", galleryVO.getGalleryno());
      ra.addAttribute("classifyno", galleryVO.getClassifyno());
      ra.addAttribute("word", word);
      ra.addAttribute("now_page", now_page);
      
      return "redirect:/gallery/read";
    } else {
      ra.addAttribute("url", "/member/login_cookie_need"); 
      return "redirect:/gallery/msg"; // GET
    }
  }

  /**
   * 파일 삭제 폼
   * http://localhost:9091/gallery/delete?galleryno=1
   * 
   * @return
   */
  @GetMapping(value = "/delete")
  public String delete(HttpSession session, Model model, RedirectAttributes ra,
      @RequestParam(name="classifyno", defaultValue = "0") int classifyno,
      @RequestParam(name="galleryno", defaultValue = "0") int galleryno,
      @RequestParam(name="word", defaultValue = "") String word, 
      @RequestParam(name="now_page", defaultValue = "1") int now_page) {
    
    if (this.memberProc.isMemberAdmin(session)) { // 관리자로 로그인한경우
      model.addAttribute("classifyno", classifyno);
      model.addAttribute("word", word);
      model.addAttribute("now_page", now_page);
      
      ArrayList<ClassifyVOMenu> menu = this.classifyProc.menu();
      model.addAttribute("menu", menu);
      
      GalleryVO galleryVO = this.galleryProc.read(galleryno);
      model.addAttribute("galleryVO", galleryVO);
      
      ClassifyVO classifyVO = this.classifyProc.read(galleryVO.getClassifyno());
      model.addAttribute("classifyVO", classifyVO);
      
      return "/gallery/delete"; // forward
      
    } else {
      ra.addAttribute("url", "/admin/login_cookie_need");
      return "redirect:/gallery/msg"; 
    }

  }
  
  /**
   * 삭제 처리 http://localhost:9091/gallery/delete
   * 
   * @return
   */
  @PostMapping(value = "/delete")
  public String delete(RedirectAttributes ra,
      @RequestParam(name="classifyno", defaultValue = "0") int classifyno,
      @RequestParam(name="galleryno", defaultValue = "0") int galleryno,
      @RequestParam(name="word", defaultValue = "") String word, 
      @RequestParam(name="now_page", defaultValue = "1") int now_page) {
    
    // -------------------------------------------------------------------
    // 파일 삭제 시작
    // -------------------------------------------------------------------
    // 삭제할 파일 정보를 읽어옴.
    GalleryVO galleryVO_read = galleryProc.read(galleryno);
        
    String file1saved = galleryVO_read.getFile1saved();
    String thumb1 = galleryVO_read.getThumb1();
    
    String uploadDir = Gallery.getUploadDir();
    Tool.deleteFile(uploadDir, file1saved);  // 실제 저장된 파일삭제
    Tool.deleteFile(uploadDir, thumb1);     // preview 이미지 삭제
    // -------------------------------------------------------------------
    // 파일 삭제 종료
    // -------------------------------------------------------------------
        
    this.galleryProc.delete(galleryno); // DBMS 글 삭제
        
    // -------------------------------------------------------------------------------------
    // 마지막 페이지의 마지막 레코드 삭제시의 페이지 번호 -1 처리
    // -------------------------------------------------------------------------------------    
    // 마지막 페이지의 마지막 10번째 레코드를 삭제후
    // 하나의 페이지가 3개의 레코드로 구성되는 경우 현재 9개의 레코드가 남아 있으면
    // 페이지수를 4 -> 3으로 감소 시켜야함, 마지막 페이지의 마지막 레코드 삭제시 나머지는 0 발생
    
    HashMap<String, Object> map = new HashMap<String, Object>();
    map.put("classifyno", classifyno);
    map.put("word", word);
    
    if (this.galleryProc.list_by_classifyno_search_count(map) % Gallery.RECORD_PER_PAGE == 0) {
      now_page = now_page - 1; // 삭제시 DBMS는 바로 적용되나 크롬은 새로고침등의 필요로 단계가 작동 해야함.
      if (now_page < 1) {
        now_page = 1; // 시작 페이지
      }
    }
    // -------------------------------------------------------------------------------------

    ra.addAttribute("classifyno", classifyno);
    ra.addAttribute("word", word);
    ra.addAttribute("now_page", now_page);
    
    return "redirect:/gallery/list_by_classifyno_grid";    
    
  }   
   
//  /**
//   * 추천 처리 http://localhost:9091/gallery/good
//   * 
//   * @return
//   */
//  @PostMapping(value = "/good")
//  @ResponseBody
//  public String good(HttpSession session, Model model, @RequestBody String json_src){ 
//    System.out.println("-> json_src: " + json_src); // json_src: {"galleryno":"5"}
//    
//    JSONObject src = new JSONObject(json_src); // String -> JSON
//    int galleryno = (int)src.get("galleryno"); // 값 가져오기
//    System.out.println("-> galleryno: " + galleryno);
//        
//    if (this.memberProc.isMember(session)) { // 회원 로그인 확인
//      // 추천을 한 상태인지 확인
//      int memberno = (int)session.getAttribute("memberno");
//      
//      HashMap<String, Object> map = new HashMap<String, Object>();
//      map.put("galleryno", galleryno);
//      map.put("memberno", memberno);
//      
//      int good_cnt = this.gallerygoodProc.hartCnt(map);
//      System.out.println("-> good_cnt: " + good_cnt);
//      
//      if (good_cnt == 1) {
//        System.out.println("-> 추천 해제: " + galleryno + ' ' + memberno);
//        
//        GallerygoodVO gallerygoodVO = this.gallerygoodProc.readByGallerynoMemberno(map);
//        
//        this.gallerygoodProc.delete(gallerygoodVO.getGallerygoodno()); // 추천 삭제
//        this.galleryProc.decreaseRecom(galleryno); // 카운트 감소
//      } else {
//        System.out.println("-> 추천: " + galleryno + ' ' + memberno);
//        
//        GallerygoodVO gallerygoodVO_new = new GallerygoodVO();
//        gallerygoodVO_new.setGalleryno(galleryno);
//        gallerygoodVO_new.setMemberno(memberno);
//        
//        this.gallerygoodProc.create(gallerygoodVO_new);
//        this.galleryProc.increaseRecom(galleryno); // 카운트 증가
//      }
//      
//      // 추천 여부가 변경되어 다시 새로운 값을 읽어옴.
//      int hartCnt = this.gallerygoodProc.hartCnt(map);
//      int recom = this.galleryProc.read(galleryno).getRecom();
//            
//      JSONObject result = new JSONObject();
//      result.put("isMember", 1); // 로그인: 1, 비회원: 0
//      result.put("hartCnt", hartCnt); // 추천 여부, 추천:1, 비추천: 0
//      result.put("recom", recom);   // 추천인수
//      
//      System.out.println("-> result.toString(): " + result.toString());
//      return result.toString();
//      
//    } else { // 정상적인 로그인이 아닌 경우 로그인 유도
//      JSONObject result = new JSONObject();
//      result.put("isMember", 0); // 로그인: 1, 비회원: 0
//      
//      System.out.println("-> result.toString(): " + result.toString());
//      return result.toString();
//    }

//  }

}

