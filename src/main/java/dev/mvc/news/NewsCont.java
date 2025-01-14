package dev.mvc.news;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;


import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import dev.mvc.classify.ClassifyProcInter;
import dev.mvc.classify.ClassifyVO;
import dev.mvc.classify.ClassifyVOMenu;
import dev.mvc.genre.GenreProcInter;
import dev.mvc.genre.GenreVOMenu;
import dev.mvc.member.MemberProcInter;
import dev.mvc.newsrecom.NewsrecomProcInter;
import dev.mvc.newsrecom.NewsrecomVO;
import dev.mvc.tool.Tool;
import dev.mvc.tool.Upload;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@RequestMapping(value = "/news")
@Controller
public class NewsCont {
  
  /** 페이지당 출력할 레코드 갯수, nowPage는 1부터 시작 */
  public int record_per_page = 10;

  /** 블럭당 페이지 수, 하나의 블럭은 10개의 페이지로 구성됨 */
  public int page_per_block = 10;

  /** 페이징 목록 주소 */
  private String list_file_name = "/news/list_search";
  
  @Autowired
  @Qualifier("dev.mvc.member.MemberProc") // @Service("dev.mvc.member.MemberProc")
  private MemberProcInter memberProc;

  @Autowired
  @Qualifier("dev.mvc.classify.ClassifyProc") // @Component("dev.mvc.classify.ClassifyProc")
  private ClassifyProcInter classifyProc;

  @Autowired
  @Qualifier("dev.mvc.news.NewsProc") // @Component("dev.mvc.news.NewsProc")
  private NewsProcInter newsProc;
  
  @Autowired
  @Qualifier("dev.mvc.genre.GenreProc") // @Component("dev.mvc.exchange.ExchangeProc")
  private GenreProcInter genreProc;
  
  @Autowired
  @Qualifier("dev.mvc.newsrecom.NewsrecomProc")
  NewsrecomProcInter newsrecomProc;

  public NewsCont() {
    System.out.println("-> NewsCont created.");
  }

  /**
   * POST 요청시 새로고침 방지, POST 요청 처리 완료 → redirect → url → GET → forward -> html 데이터
   * 전송
   * 
   * @return
   */
  @GetMapping(value = "/msg")
  public String post2get(Model model, 
      @RequestParam(name="url", defaultValue = "") String url) {
    ArrayList<ClassifyVOMenu> menu = this.classifyProc.menu();
    model.addAttribute("menu", menu);

    return url; // forward, /templates/...
  }

  // 등록 폼, news 테이블은 FK로 classifyno를 사용함.
  // http://localhost:9093/news/create X
  // http://localhost:9093/news/create?classifyno=1 // classifyno 변수값을 보내는 목적
  // http://localhost:9093/news/create?classifyno=2
  // http://localhost:9093/news/create?classifyno=5
  @GetMapping(value = "/create")
  public String create(
      Model model, 
      RedirectAttributes ra,
      HttpSession session,
      @ModelAttribute("newsVO") NewsVO newsVO, 
      @RequestParam(name="classifyno", defaultValue="0") int classifyno) {

    if (this.memberProc.isMember(session)) {
      ArrayList<ClassifyVOMenu> menu = this.classifyProc.menu();
      model.addAttribute("menu", menu);
      
      ArrayList<GenreVOMenu> menu1 = this.genreProc.menu(); // 대분류
      model.addAttribute("menu1", menu1);
  
      ClassifyVO classifyVO = this.classifyProc.read(classifyno); // 카테고리 정보를 출력하기위한 목적
      model.addAttribute("classifyVO", classifyVO);
  
      return "/news/create"; // /templates/news/create.html
    } else {
      ra.addAttribute("url", "/member/login_cookie_need"); 
      return "redirect:/news/msg"; // GET
    }

  }

  /**
   * 등록 처리 http://localhost:9093/news/create
   * 
   * @return
   */
  @PostMapping(value = "/create")
  public String create(
      HttpServletRequest request, 
      HttpSession session, 
      Model model, 
      @ModelAttribute("newsVO") NewsVO newsVO,
      RedirectAttributes ra) {

    if (this.memberProc.isMember(session)) {      
      // ------------------------------------------------------------------------------
      // 파일 전송 코드 시작
      // ------------------------------------------------------------------------------
      String file1 = ""; // 원본 파일명 image
      String file1saved = ""; // 저장된 파일명, image
      String thumb1 = ""; // preview image

      String upDir = News.getUploadDir(); // 파일을 업로드할 폴더 준비
      // upDir = upDir + "/" + 한글을 제외한 카테고리 이름
      System.out.println("-> upDir: " + upDir);

      // 전송 파일이 없어도 file1MF 객체가 생성됨.
      // <input type='file' class="form-control" name='file1MF' id='file1MF'
      // value='' placeholder="파일 선택">
      MultipartFile mf = newsVO.getFile1MF();

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

          newsVO.setFile1(file1); // 순수 원본 파일명
          newsVO.setFile1saved(file1saved); // 저장된 파일명(파일명 중복 처리)
          newsVO.setThumb1(thumb1); // 원본이미지 축소판
          newsVO.setSize1(size1); // 파일 크기

        } else { // 전송 못하는 파일 형식
          ra.addFlashAttribute("code", "check_upload_file_fail"); // 업로드 할 수 없는 파일
          ra.addFlashAttribute("cnt", 0); // 업로드 실패
          ra.addFlashAttribute("url", "/news/msg"); // msg.html, redirect parameter 적용
          return "redirect:/news/msg"; // Post -> Get - param...
        }
      } else { // 글만 등록하는 경우
        System.out.println("-> 글만 등록");
      }

      // ------------------------------------------------------------------------------
      // 파일 전송 코드 종료
      // ------------------------------------------------------------------------------

      // Call By Reference: 메모리 공유, Hashcode 전달
      int memberno = (int) session.getAttribute("memberno"); // memberno FK
      newsVO.setMemberno(memberno);
      int cnt = this.newsProc.create(newsVO);

      
      // ------------------------------------------------------------------------------
      // Python 스크립트 실행 (추가된 부분)
      // ------------------------------------------------------------------------------
      try {
          String pythonScriptPath = "src/main/python/pernews.py"; // Python 스크립트 경로
          ProcessBuilder processBuilder = new ProcessBuilder("python", pythonScriptPath);
          processBuilder.redirectErrorStream(true);
          Process process = processBuilder.start();

          // Python 스크립트의 출력 결과를 읽기
          BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
          String line;
          while ((line = reader.readLine()) != null) {
              System.out.println(line);
          }

          int exitCode = process.waitFor();
          System.out.println("Python script finished with exit code: " + exitCode);

          if (exitCode != 0) {
              System.out.println("Python script execution failed");
          }
      } catch (IOException | InterruptedException e) {
          e.printStackTrace();
      }
      
      // ------------------------------------------------------------------------------
      // 리다이렉트 처리
      // ------------------------------------------------------------------------------
      
      // ------------------------------------------------------------------------------
      // PK의 return
      // ------------------------------------------------------------------------------
      // System.out.println("--> newsno: " + newsVO.getNewsno());
      // mav.addObject("newsno", newsVO.getNewsno()); // redirect
      // parameter 적용
      // ------------------------------------------------------------------------------

      if (cnt == 1) {
        // type 1, 재업로드 발생
        // return "<h1>파일 업로드 성공</h1>"; // 연속 파일 업로드 발생

        // type 2, 재업로드 발생
        // model.addAttribute("cnt", cnt);
        // model.addAttribute("code", "create_success");
        // return "news/msg";

        // type 3 권장
        // return "redirect:/news/list_all"; // /templates/news/list_all.html

        // System.out.println("-> newsVO.getClassifyno(): " + newsVO.getClassifyno());
        // ra.addFlashAttribute("classifyno", newsVO.getClassifyno()); // controller ->
        // controller: X

        ra.addAttribute("classifyno", newsVO.getClassifyno()); // controller -> controller: O
        return "redirect:/news/list_by_classifyno";

        // return "redirect:/news/list_by_classifyno?classifyno=" + newsVO.getClassifyno();
        // // /templates/news/list_by_classifyno.html
      } else {
        return "redirect:/news/list_by_classifyno?classifyno=5"; // Post -> Get - param...
      }
    } else {
      ra.addAttribute("url", "/member/login_cookie_need"); 
      return "redirect:/news/msg"; // GET
    }
  }

  /**
   * 전체 목록, 관리자만 사용 가능 http://localhost:9093/news/list_all
   * 
   * @return
   */
  @GetMapping(value = "/list_all")
  public String list_all(HttpSession session, Model model) {
    // System.out.println("-> list_all");
    ArrayList<ClassifyVOMenu> menu = this.classifyProc.menu();
    model.addAttribute("menu", menu);
    
    ArrayList<GenreVOMenu> menu1 = this.genreProc.menu(); // 대분류
    model.addAttribute("menu1", menu1);

    if (this.memberProc.isMemberAdmin(session)) { // 관리자만 조회 가능
      ArrayList<NewsVO> list = this.newsProc.list_all(); // 모든 목록

      // Thymeleaf는 CSRF(크로스사이트) 스크립팅 해킹 방지 자동 지원
      // for문을 사용하여 객체를 추출, Call By Reference 기반의 원본 객체 값 변경
//      for (NewsVO newsVO : list) {
//        String title = newsVO.getTitle();
//        String content = newsVO.getContent();
//        
//        title = Tool.convertChar(title);  // 특수 문자 처리
//        content = Tool.convertChar(content); 
//        
//        newsVO.setTitle(title);
//        newsVO.setContent(content);  
//
//      }

      model.addAttribute("list", list);
      return "/news/list_all";

    } else {
      return "redirect:/member/login_cookie_need";

    }

  }
  
//  /**
//   * 유형 1
//   * 카테고리별 목록
//   * http://localhost:9093/news/list_by_classifyno?classifyno=5
//   * http://localhost:9093/news/list_by_classifyno?classifyno=6 
//   * @return
//   */
//  @GetMapping(value="/list_by_classifyno")
//  public String list_by_classifyno(HttpSession session, Model model, 
//      @RequestParam(name="classifyno", defaultValue = "") int classifyno) {
//    ArrayList<ClassifyVOMenu> menu = this.classifyProc.menu();
//    model.addAttribute("menu", menu);
//    
//     ClassifyVO classifyVO = this.classifyProc.read(classifyno);
//     model.addAttribute("classifyVO", classifyVO);
//    
//    ArrayList<NewsVO> list = this.newsProc.list_by_classifyno(classifyno);
//    model.addAttribute("list", list);
//    
//    // System.out.println("-> size: " + list.size());
//
//    return "/news/list_by_classifyno";
//  }

//  /**
//   * 유형 2
//   * 카테고리별 목록 + 검색
//   * http://localhost:9093/news/list_by_classifyno?classifyno=5
//   * http://localhost:9093/news/list_by_classifyno?classifyno=6 
//   * @return
//   */
//  @GetMapping(value="/list_by_classifyno")
//  public String list_by_classifyno_search(HttpSession session, Model model, 
//                                                   @RequestParam(name="classifyno", defaultValue = "0" ) int classifyno, 
//                                                   @RequestParam(name="word", defaultValue = "") String word) {
//    ArrayList<ClassifyVOMenu> menu = this.classifyProc.menu();
//    model.addAttribute("menu", menu);
//    
//     ClassifyVO classifyVO = this.classifyProc.read(classifyno);
//     model.addAttribute("classifyVO", classifyVO);
//    
//     word = Tool.checkNull(word).trim(); // 검색어 공백 삭제
//     
//     HashMap<String, Object> map = new HashMap<>();
//     map.put("classifyno", classifyno);
//     map.put("word", word);
//     
//    ArrayList<NewsVO> list = this.newsProc.list_by_classifyno_search(map);
//    model.addAttribute("list", list);
//    
//    // System.out.println("-> size: " + list.size());
//    model.addAttribute("word", word);
//    
//    int search_count = this.newsProc.list_by_classifyno_search_count(map);
//    model.addAttribute("search_count", search_count);
//    
//    return "/news/list_by_classifyno_search"; // /templates/news/list_by_classifyno_search.html
//  }

  /**
   * 유형 3
   * 카테고리별 목록 + 검색 + 페이징 http://localhost:9093/news/list_by_classifyno?classifyno=5
   * http://localhost:9093/news/list_by_classifyno?classifyno=6
   * 
   * @return
   */
  @GetMapping(value = "/list_by_classifyno")
  public String list_by_classifyno_search_paging(
      HttpSession session, 
      Model model, 
      @RequestParam(name = "classifyno", defaultValue = "1") int classifyno,
      @RequestParam(name = "word", defaultValue = "") String word,
      @RequestParam(name = "now_page", defaultValue = "1") int now_page,
      @RequestParam(name = "newsgenre", defaultValue = "") String newsgenre) {

    // System.out.println("-> classifyno: " + classifyno);
    
    ArrayList<ClassifyVOMenu> menu = this.classifyProc.menu();
    model.addAttribute("menu", menu);
    
    ArrayList<GenreVOMenu> menu1 = this.genreProc.menu(); // 대분류
    model.addAttribute("menu1", menu1);
    
    ClassifyVO classifyVO = this.classifyProc.read(classifyno);
    model.addAttribute("classifyVO", classifyVO);
    
    word = Tool.checkNull(word).trim();
    newsgenre = Tool.checkNull(newsgenre).trim();
    
    HashMap<String, Object> map = new HashMap<>();
    map.put("classifyno", classifyno);
    map.put("word", word);
    map.put("now_page", now_page);
    map.put("newsgenre", newsgenre);
    
    ArrayList<NewsVO> list = this.newsProc.list_by_classifyno_search_paging(map);
    model.addAttribute("list", list);
    // System.out.println("-> size: " + list.size());
    model.addAttribute("word", word);
    model.addAttribute("newsgenre", newsgenre);
    
    int search_count = this.newsProc.list_by_classifyno_search_count(map);
    String paging = this.newsProc.pagingBox(classifyno, now_page, word, "/news/list_by_classifyno", search_count,
        News.RECORD_PER_PAGE, News.PAGE_PER_BLOCK);
    model.addAttribute("paging", paging);
    model.addAttribute("now_page", now_page);
    
    model.addAttribute("search_count", search_count);

    // 일련 변호 생성: 레코드 갯수 - ((현재 페이지수 -1) * 페이지당 레코드 수)
    int no = search_count - ((now_page - 1) * News.RECORD_PER_PAGE);
    model.addAttribute("no", no);

    return "/news/list_by_classifyno_search_paging"; // /templates/news/list_by_classifyno_search_paging.html
  }

  /**
   * 카테고리별 목록 + 검색 + 페이징 + Grid
   * http://localhost:9093/news/list_by_classifyno?classifyno=5
   * http://localhost:9093/news/list_by_classifyno?classifyno=6
   * 
   * @return
   */
  
  @GetMapping(value = "/list_by_classifyno_grid")
  public String list_by_classifyno_search_paging_grid(
      HttpSession session, 
      Model model, 
      @RequestParam(name = "classifyno", defaultValue = "0") int classifyno,
      @RequestParam(name = "word", defaultValue = "") String word,
      @RequestParam(name = "now_page", defaultValue = "1") int now_page) {

    // System.out.println("-> classifyno: " + classifyno);
    // newsgenre
    ArrayList<ClassifyVOMenu> menu = this.classifyProc.menu();
    model.addAttribute("menu", menu);
    
    ArrayList<GenreVOMenu> menu1 = this.genreProc.menu(); // 대분류
    model.addAttribute("menu1", menu1);
    
    ClassifyVO classifyVO = this.classifyProc.read(classifyno);
    model.addAttribute("classifyVO", classifyVO);
    
    word = Tool.checkNull(word).trim();

    HashMap<String, Object> map = new HashMap<>();
    map.put("classifyno", classifyno);
    map.put("word", word);
    map.put("now_page", now_page);
    
    ArrayList<NewsVO> list = this.newsProc.list_by_classifyno_search_paging(map);
    model.addAttribute("list", list);

    // System.out.println("-> size: " + list.size());
    model.addAttribute("word", word);

    int search_count = this.newsProc.list_by_classifyno_search_count(map);
    String paging = this.newsProc.pagingBox(classifyno, now_page, word, "/news/list_by_classifyno_grid", search_count,
        News.RECORD_PER_PAGE, News.PAGE_PER_BLOCK);
    model.addAttribute("paging", paging);
    model.addAttribute("now_page", now_page);

    model.addAttribute("search_count", search_count);

    // 일련 변호 생성: 레코드 갯수 - ((현재 페이지수 -1) * 페이지당 레코드 수)
    int no = search_count - ((now_page - 1) * News.RECORD_PER_PAGE);
    model.addAttribute("no", no);

    // /templates/news/list_by_classifyno_search_paging_grid.html
    return "/news/list_by_classifyno_search_paging_grid";
  }

  /**
   * 조회 http://localhost:9093/news/read?newsno=17
   * 
   * @return
   */
  @GetMapping(value = "/read")
  public String read(
      HttpSession session,
      Model model, 
      @RequestParam(name="newsno", defaultValue = "0") int newsno,
      @RequestParam(name="word", defaultValue = "") String word, 
      @RequestParam(name="now_page", defaultValue = "1") int now_page,
      @RequestParam(name="modno", defaultValue="0") int modno) {
    
    ArrayList<ClassifyVOMenu> menu = this.classifyProc.menu();
    model.addAttribute("menu", menu);
    
    ArrayList<GenreVOMenu> menu1 = this.genreProc.menu(); // 대분류
    model.addAttribute("menu1", menu1);
    
    System.out.println("newsno : "+ newsno);

    NewsVO newsVO = this.newsProc.read(newsno);
//    String title = newsVO.getTitle();
//    String content = newsVO.getContent();
//    
//    title = Tool.convertChar(title);  // 특수 문자 처리
//    content = Tool.convertChar(content); 
//    
//    newsVO.setTitle(title);
//    newsVO.setContent(content);  

    long size1 = newsVO.getSize1();
    String size1_label = Tool.unit(size1);
    newsVO.setSize1_label(size1_label);

   // 조건에 맞는 뉴스 모드 처리
    if (modno == 1) {
        System.out.println("원문 모드");
        newsVO.setTitle("1111");
    } else if (modno == 2) {
        System.out.println("번역 및 요약 모드");
        newsVO.setTitle("2222");
    }
    
    model.addAttribute("newsVO", newsVO);

    ClassifyVO classifyVO = this.classifyProc.read(newsVO.getClassifyno());
    model.addAttribute("classifyVO", classifyVO);
    System.out.println("newsVO.getClassifyno() : "+ newsVO.getClassifyno());
    
    System.out.println("modno : " + modno);
    
    // 조회에서 화면 하단에 출력
    // ArrayList<ReplyVO> reply_list = this.replyProc.list_news(newsno);
    // mav.addObject("reply_list", reply_list);
    model.addAttribute("word", word);
    model.addAttribute("now_page", now_page);
    
    // -------------------------------------------------------------------------------------------
    // 추천 관련
    // -------------------------------------------------------------------------------------------
    HashMap<String, Object> map = new HashMap<String, Object>();
    map.put("newsno", newsno);
    
    int heartCnt = 0;
    if (session.getAttribute("memberno") != null ) { // 회원인 경우만 카운트 처리
      int memberno = (int)session.getAttribute("memberno");
      map.put("memberno", memberno);
      
      heartCnt = this.newsrecomProc.heartCnt(map);
    } 
    
    model.addAttribute("heartCnt", heartCnt);
    // -------------------------------------------------------------------------------------------
    
    return "/news/read";
  }

  /**
   * 맵 등록/수정/삭제 폼 http://localhost:9093/news/map?newsno=19
   * 
   * @return
   */
  @GetMapping(value = "/map")
  public String map(
      Model model, 
      @RequestParam(name="newsno", defaultValue="0") int newsno,
      @RequestParam(name="now_page", defaultValue="0") int now_page,
      @RequestParam(name="word", defaultValue="")  String word) {
    ArrayList<ClassifyVOMenu> menu = this.classifyProc.menu();
    model.addAttribute("menu", menu);

    NewsVO newsVO = this.newsProc.read(newsno); // map 정보 읽어 오기
    model.addAttribute("newsVO", newsVO); // request.setAttribute("newsVO", newsVO);

    ClassifyVO classifyVO = this.classifyProc.read(newsVO.getClassifyno()); // 그룹 정보 읽기
    model.addAttribute("classifyVO", classifyVO);
    
    model.addAttribute("word", word);
    model.addAttribute("now_page", now_page);

    return "/news/map"; // //templates/news/map.html
  }

  /**
   * MAP 등록/수정/삭제 처리 http://localhost:9093/news/map
   * 
   * @param newsVO
   * @return
   */
  @PostMapping(value = "/map")
  public String map_update(
      Model model, 
      RedirectAttributes ra,
      @RequestParam(name="newsno", defaultValue = "0") int newsno, 
      @RequestParam(name="map", defaultValue = "") String map,
      @RequestParam(name="now_page", defaultValue="0") int now_page,
      @RequestParam(name="word", defaultValue="")  String word) {
    
    HashMap<String, Object> hashMap = new HashMap<String, Object>();
    hashMap.put("newsno", newsno);
    hashMap.put("map", map);

    this.newsProc.map(hashMap);

    ra.addAttribute("newsno", newsno);
    ra.addAttribute("word", word);
    ra.addAttribute("now_page", now_page);

    // return "redirect:/news/read?newsno=" + newsno;
    return "redirect:/news/read";
  }

  /**
   * Youtube 등록/수정/삭제 폼 http://localhost:9093/news/youtube?newsno=1
   * 
   * @return
   */
  @GetMapping(value = "/youtube")
  public String youtube(Model model, 
      @RequestParam(name="newsno", defaultValue="0") int newsno,
      @RequestParam(name="word", defaultValue="")  String word, 
      @RequestParam(name="now_page", defaultValue="0") int now_page) {
    ArrayList<ClassifyVOMenu> menu = this.classifyProc.menu();
    model.addAttribute("menu", menu);

    NewsVO newsVO = this.newsProc.read(newsno); // map 정보 읽어 오기
    model.addAttribute("newsVO", newsVO); // request.setAttribute("newsVO", newsVO);

    ClassifyVO classifyVO = this.classifyProc.read(newsVO.getClassifyno()); // 그룹 정보 읽기
    model.addAttribute("classifyVO", classifyVO);

    model.addAttribute("word", word);
    model.addAttribute("now_page", now_page);
    
    return "/news/youtube";  // forward, /templates/news/youtube.html
  }

  /**
   * Youtube 등록/수정/삭제 처리 http://localhost:9093/news/youtube
   * 
   * @param newsVO
   * @return
   */
  @PostMapping(value = "/youtube")
  public String youtube_update(
      Model model, 
    RedirectAttributes ra,
    @RequestParam(name="newsno", defaultValue = "0") int newsno, 
    @RequestParam(name="youtube", defaultValue = "") String youtube, 
    @RequestParam(name="word", defaultValue = "") String word, 
    @RequestParam(name="now_page", defaultValue = "0") int now_page) {

    if (youtube.trim().length() > 0) { // 삭제 중인지 확인, 삭제가 아니면 youtube 크기 변경
      youtube = Tool.youtubeResize(youtube, 640); // youtube 영상의 크기를 width 기준 640 px로 변경
    }

    HashMap<String, Object> hashMap = new HashMap<String, Object>();
    hashMap.put("newsno", newsno);
    hashMap.put("youtube", youtube);

    this.newsProc.youtube(hashMap);
    
    ra.addAttribute("newsno", newsno);
    ra.addAttribute("word", word);
    ra.addAttribute("now_page", now_page);

    // return "redirect:/news/read?newsno=" + newsno;
    return "redirect:/news/read";
  }

  /**
   * 수정 폼 http:// localhost:9093/news/update_text?newsno=1
   *
   */
  @GetMapping(value = "/update_text")
  public String update_text(
      HttpSession session, 
      Model model,  
      RedirectAttributes ra,
      @RequestParam(name="newsno", defaultValue = "0") int newsno,
      @RequestParam(name="word", defaultValue = "") String word,
      @RequestParam(name="now_page", defaultValue = "0") int now_page) {
    if (this.memberProc.isMember(session)) {
      ArrayList<ClassifyVOMenu> menu = this.classifyProc.menu();
      model.addAttribute("menu", menu);
      
      ArrayList<GenreVOMenu> menu1 = this.genreProc.menu(); // 대분류
      model.addAttribute("menu1", menu1);
  
      model.addAttribute("word", word);
      model.addAttribute("now_page", now_page);

      NewsVO newsVO = this.newsProc.read(newsno);
      model.addAttribute("newsVO", newsVO);

      ClassifyVO classifyVO = this.classifyProc.read(newsVO.getClassifyno());
      model.addAttribute("classifyVO", classifyVO);

      return "/news/update_text"; // /templates/news/update_text.html
      // String content = "장소:\n인원:\n준비물:\n비용:\n기타:\n";
      // model.addAttribute("content", content);

    } else {
      ra.addAttribute("url", "/member/login_cookie_need"); 
      return "redirect:/news/msg"; // GET
    }
  }

  /**
   * 수정 처리 http://localhost:9093/news/update_text?newsno=1
   * 
   * @return
   */
  @PostMapping(value = "/update_text")
  public String update_text(
      HttpSession session, 
      Model model, 
      RedirectAttributes ra,
      @ModelAttribute("newsVO") NewsVO newsVO, 
      @RequestParam(name="search_word", defaultValue = "") String search_word, // newsVO.word와 구분 필요
      @RequestParam(name="now_page", defaultValue = "0") int now_page) {
    if (this.memberProc.isMember(session)) {
      ra.addAttribute("word", search_word);
      ra.addAttribute("now_page", now_page);
  
      HashMap<String, Object> map = new HashMap<String, Object>();
      map.put("newsno", newsVO.getNewsno());
      map.put("passwd", newsVO.getPasswd());
  
      if (this.newsProc.password_check(map) == 1) { // 패스워드 일치
        this.newsProc.update_text(newsVO); // 글수정
  
        // mav 객체 이용
        ra.addAttribute("newsno", newsVO.getNewsno());
        ra.addAttribute("classifyno", newsVO.getClassifyno());
        return "redirect:/news/read"; // @GetMapping(value = "/read")
  
      } else { // 패스워드 불일치
        ra.addFlashAttribute("code", "passwd_fail"); // redirect -> forward -> html
        ra.addFlashAttribute("cnt", 0);
        ra.addAttribute("url", "/news/msg"); // msg.html, redirect parameter 적용
  
        return "redirect:/news/post2get"; // @GetMapping(value = "/msg")
      }

    } else {
      ra.addAttribute("url", "/member/login_cookie_need"); 
      return "redirect:/news/msg"; // GET
    }

  }

  /**
   * 파일 수정 폼 http://localhost:9093/news/update_file?newsno=1
   * 
   * @return
   */
  @GetMapping(value = "/update_file")
  public String update_file(
      HttpSession session, 
      Model model,
      RedirectAttributes ra,
     @RequestParam(name="newsno", defaultValue = "0") int newsno,
     @RequestParam(name="word", defaultValue = "") String word, 
     @RequestParam(name="now_page", defaultValue = "1") int now_page) {
    if (this.memberProc.isMember(session)) {
      ArrayList<ClassifyVOMenu> menu = this.classifyProc.menu();
      model.addAttribute("menu", menu);
      
      ArrayList<GenreVOMenu> menu1 = this.genreProc.menu(); // 대분류
      model.addAttribute("menu1", menu1);
      
      model.addAttribute("word", word);
      model.addAttribute("now_page", now_page);
      
      NewsVO newsVO = this.newsProc.read(newsno);
      model.addAttribute("newsVO", newsVO);
  
      ClassifyVO classifyVO = this.classifyProc.read(newsVO.getClassifyno());
      model.addAttribute("classifyVO", classifyVO);
  
      return "/news/update_file";
    } else {
      ra.addAttribute("url", "/member/login_cookie_need"); 
      return "redirect:/news/msg"; // GET
    }
  }

  /**
   * 파일 수정 처리 http://localhost:9093/news/update_file
   * 
   * @return
   */
  @PostMapping(value = "/update_file")
  public String update_file(
      HttpSession session, Model model, RedirectAttributes ra,
       @ModelAttribute("newsVO") NewsVO newsVO,
       @RequestParam(name="word", defaultValue = "") String word, 
       @RequestParam(name="now_page", defaultValue = "1") int now_page) {
    if (this.memberProc.isMember(session)) {
      // 삭제할 파일 정보를 읽어옴, 기존에 등록된 레코드 저장용
      NewsVO newsVO_old = newsProc.read(newsVO.getNewsno());

      // -------------------------------------------------------------------
      // 파일 삭제 시작
      // -------------------------------------------------------------------
      String file1saved = newsVO_old.getFile1saved(); // 실제 저장된 파일명
      String thumb1 = newsVO_old.getThumb1(); // 실제 저장된 preview 이미지 파일명
      long size1 = 0;

      String upDir = News.getUploadDir(); // C:/kd/deploy/team4/news/storage/

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
      MultipartFile mf = newsVO.getFile1MF();

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

      newsVO.setFile1(file1);
      newsVO.setFile1saved(file1saved);
      newsVO.setThumb1(thumb1);
      newsVO.setSize1(size1);
      // -------------------------------------------------------------------
      // 파일 전송 코드 종료
      // -------------------------------------------------------------------

      this.newsProc.update_file(newsVO); // Oracle 처리
      ra.addAttribute ("newsno", newsVO.getNewsno());
      ra.addAttribute("classifyno", newsVO.getClassifyno());
      ra.addAttribute("word", word);
      ra.addAttribute("now_page", now_page);
      
      return "redirect:/news/read";
    } else {
      ra.addAttribute("url", "/member/login_cookie_need"); 
      return "redirect:/news/msg"; // GET
    }
  }

  /**
   * 파일 삭제 폼
   * http://localhost:9093/news/delete?newsno=1
   * 
   * @return
   */
  @GetMapping(value = "/delete")
  public String delete(
      HttpSession session, Model model, RedirectAttributes ra,
      @RequestParam(name="classifyno", defaultValue = "0") int classifyno,
      @RequestParam(name="newsno", defaultValue = "0") int newsno,
      @RequestParam(name="word", defaultValue = "") String word, 
      @RequestParam(name="now_page", defaultValue = "1") int now_page) {
    
    if (this.memberProc.isMember(session)) {
      model.addAttribute("classifyno", classifyno);
      model.addAttribute("word", word);
      model.addAttribute("now_page", now_page);
      
      ArrayList<ClassifyVOMenu> menu = this.classifyProc.menu();
      model.addAttribute("menu", menu);
      
      ArrayList<GenreVOMenu> menu1 = this.genreProc.menu(); // 대분류
      model.addAttribute("menu1", menu1);
      
      NewsVO newsVO = this.newsProc.read(newsno);
      model.addAttribute("newsVO", newsVO);
      
      ClassifyVO classifyVO = this.classifyProc.read(newsVO.getClassifyno());
      model.addAttribute("classifyVO", classifyVO);
      
      return "/news/delete"; // forward
    } else {
      ra.addAttribute("url", "/member/login_cookie_need"); 
      return "redirect:/news/msg"; // GET
    } 
  }
  
  /**
   * 삭제 처리 http://localhost:9093/news/delete
   * 
   * @return
   */
  @PostMapping(value = "/delete")
  public String delete(
      RedirectAttributes ra,
      HttpSession session,
      @RequestParam(name="classifyno", defaultValue = "0") int classifyno,
      @RequestParam(name="newsno", defaultValue = "0") int newsno,
      @RequestParam(name="word", defaultValue = "") String word, 
      @RequestParam(name="now_page", defaultValue = "1") int now_page) {
    if (this.memberProc.isMember(session)) {
      // -------------------------------------------------------------------
      // 파일 삭제 시작
      // -------------------------------------------------------------------
      // 삭제할 파일 정보를 읽어옴.
      NewsVO newsVO_read = newsProc.read(newsno);
          
      String file1saved = newsVO_read.getFile1saved();
      String thumb1 = newsVO_read.getThumb1();
      
      String uploadDir = News.getUploadDir();
      Tool.deleteFile(uploadDir, file1saved);  // 실제 저장된 파일삭제
      Tool.deleteFile(uploadDir, thumb1);     // preview 이미지 삭제
      // -------------------------------------------------------------------
      // 파일 삭제 종료
      // -------------------------------------------------------------------
          
      this.newsProc.delete(newsno); // DBMS 글 삭제
          
      // -------------------------------------------------------------------------------------
      // 마지막 페이지의 마지막 레코드 삭제시의 페이지 번호 -1 처리
      // -------------------------------------------------------------------------------------    
      // 마지막 페이지의 마지막 10번째 레코드를 삭제후
      // 하나의 페이지가 3개의 레코드로 구성되는 경우 현재 9개의 레코드가 남아 있으면
      // 페이지수를 4 -> 3으로 감소 시켜야함, 마지막 페이지의 마지막 레코드 삭제시 나머지는 0 발생
      
      HashMap<String, Object> map = new HashMap<String, Object>();
      map.put("classifyno", classifyno);
      map.put("word", word);
      
      if (this.newsProc.list_by_classifyno_search_count(map) % News.RECORD_PER_PAGE == 0) {
        now_page = now_page - 1; // 삭제시 DBMS는 바로 적용되나 크롬은 새로고침등의 필요로 단계가 작동 해야함.
        if (now_page < 1) {
          now_page = 1; // 시작 페이지
        }
      }
      // -------------------------------------------------------------------------------------
    
      ra.addAttribute("classifyno", classifyno);
      ra.addAttribute("word", word);
      ra.addAttribute("now_page", now_page);
      
      return "redirect:/news/list_by_classifyno";    
    } else {
      ra.addAttribute("url", "/member/login_cookie_need"); 
      return "redirect:/news/msg"; // GET
    }
  }   


//  /**
//   * 전체 목록, 관리자만 사용 가능 http://localhost:9093/news/list_all
//   * 
//   * @return
//   */
//  @GetMapping(value = "/newsgenre")
//  public String newsgenre(HttpSession session, Model model,
//      @RequestParam(name="newsgenre", defaultValue = "") String newsgenre) {
//
//
//    System.out.println("newsgenre1111:" + newsgenre);
//    ArrayList<ClassifyVOMenu> menu = this.classifyProc.menu();
//    model.addAttribute("menu", menu);
//  
//    ArrayList<NewsVO> list = this.newsProc.newsgenre(newsgenre); // 모든 목록
//    model.addAttribute("list", list);
//    
//    newsgenre = Tool.checkNull(newsgenre).trim();
//    
//    HashMap<String, Object> map = new HashMap<>();
//    map.put("newsgenre", newsgenre);
//    model.addAttribute("newsgenre", newsgenre);
//    
//    System.out.println("newsgenre:" + newsgenre);
//    
//    return "/news/newsgenre";
//
//   
//  }
  
  /**
   * 번역 요약 http://localhost:9093/news/trans_sum?newsno=17
   * 
   * @return
   */
  @GetMapping(value = "/trans_sum")
  public String trans_sum(Model model,
      @RequestParam(name="newsno", defaultValue = "0") int newsno,
      @RequestParam(name="newscrawlingno", defaultValue = "0") int newscrawlingno,
      @RequestParam(name="word", defaultValue = "") String word, 
      @RequestParam(name="now_page", defaultValue = "1") int now_page
      ) {
    ArrayList<ClassifyVOMenu> menu = this.classifyProc.menu();
    model.addAttribute("menu", menu);
    
    ArrayList<GenreVOMenu> menu1 = this.genreProc.menu(); // 대분류
    model.addAttribute("menu1", menu1);
    
    System.out.println("newscrawlingno : "+ newscrawlingno);
    System.out.println("newsno : "+ newsno);

    NewsVO newsVO = this.newsProc.trans_sum(newsno);
//    String title = newsVO.getTitle();
//    String content = newsVO.getContent();
//    
//    title = Tool.convertChar(title);  // 특수 문자 처리
//    content = Tool.convertChar(content); 
//    
//    newsVO.setTitle(title);
//    newsVO.setContent(content);  

    long size1 = newsVO.getSize1();
    String size1_label = Tool.unit(size1);
    newsVO.setSize1_label(size1_label);

    model.addAttribute("newsVO", newsVO);
    System.out.println("newsno : "+ newsno);

    ClassifyVO classifyVO = this.classifyProc.read(newsVO.getClassifyno());
    model.addAttribute("classifyVO", classifyVO);
    System.out.println("newsVO.getClassifyno() : "+ newsVO.getClassifyno());

    // 조회에서 화면 하단에 출력
    // ArrayList<ReplyVO> reply_list = this.replyProc.list_news(newsno);
    // mav.addObject("reply_list", reply_list);
    model.addAttribute("word", word);
    model.addAttribute("now_page", now_page);

    return "/news/trans_sum";

  } 

  /**
   * 추천 처리 http://localhost:9093/news/recom
   * 
   * @return
   */
  @PostMapping(value = "/recom")
  @ResponseBody
  public String recom(
      HttpSession session, 
      Model model,
      RedirectAttributes ra,
      @RequestBody String json_src) {
         
      System.out.println("-> json_src: " + json_src); // json_src: {"current_passwd":"1234"}
      
      JSONObject src = new JSONObject(json_src); // String -> JSON
      int newsno = (int)src.get("newsno"); // 값 가져오기
      System.out.println("-> newsno: " + newsno);
      
      if (this.memberProc.isMember(session)) {
        // 추천을 한 상태인지 확인
        int memberno = (int)session.getAttribute("memberno");
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("newsno", newsno);
        map.put("memberno", memberno);
        
        int recom_cnt = this.newsrecomProc.heartCnt(map);
        System.out.println("-> recom_cnt: " + recom_cnt);
        
        if(recom_cnt == 1) {
          // 추천 해제
          System.out.println("-> 추천해제: " + newsno + ' ' + memberno);
          
          NewsrecomVO newsrecomVO = this.newsrecomProc.readByNewsnoMemberno(map);
          
          this.newsrecomProc.delete(newsrecomVO.getNewsrecomno()); // 추천 삭제
          this.newsProc.decreaseRecom(newsno); // 카운트 감소
          
        } else {
          // 추천
          System.out.println("-> 추천: " + newsno + ' ' + memberno);
          
          NewsrecomVO newsrecomVO_new = new NewsrecomVO();
          newsrecomVO_new.setNewsno(newsno);
          newsrecomVO_new.setMemberno(memberno);
          
          this.newsrecomProc.create(newsrecomVO_new);
          this.newsProc.increaseRecom(newsno); // 카운트 증가
        }        
        
        int heartCnt = this.newsrecomProc.heartCnt(map);
        int recom = this.newsProc.read(newsno).getRecom();
        
        JSONObject result = new JSONObject();
        result.put("isMember", 1); // 로그인 1 , 비회원 0
        result.put("heartCnt", heartCnt);
        result.put("recom", recom);
        
        return result.toString();
        
    } else {
      JSONObject result = new JSONObject();
      result.put("isMember", 0); // 비회원 0 로그인 1
      
      return result.toString(); // GeyMapping(value = "/msg")
    }

  }
  
}