package dev.mvc.exchange;

import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import dev.mvc.classify.ClassifyProcInter;
import dev.mvc.classify.ClassifyVO;
import dev.mvc.classify.ClassifyVOMenu;
import dev.mvc.genre.GenreProcInter;
import dev.mvc.member.MemberProcInter;
import dev.mvc.tool.Tool;
import dev.mvc.tool.Upload;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@RequestMapping(value = "/exchange")
@Controller
public class ExchangeCont {
  @Autowired
  @Qualifier("dev.mvc.member.MemberProc") // @Service("dev.mvc.member.MemberProc")
  private MemberProcInter memberProc;

  @Autowired
  @Qualifier("dev.mvc.classify.ClassifyProc") // @Component("dev.mvc.classify.ClassifyProc")
  private ClassifyProcInter classifyProc;

  @Autowired
  @Qualifier("dev.mvc.exchange.ExchangeProc") // @Component("dev.mvc.exchange.ExchangeProc")
  private ExchangeProcInter exchangeProc;
  @Autowired
  @Qualifier("dev.mvc.genre.GenreProc") // @Component("dev.mvc.exchange.ExchangeProc")
  private GenreProcInter genreProc;

  public ExchangeCont() {
    System.out.println("-> ExchangeCont created.");
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

  // 등록 폼, exchange 테이블은 FK로 classifyno를 사용함.
  // http://localhost:9091/exchange/create X
  // http://localhost:9091/exchange/create?classifyno=1 // classifyno 변수값을 보내는 목적
  // http://localhost:9091/exchange/create?classifyno=2
  // http://localhost:9091/exchange/create?classifyno=5
  @GetMapping(value = "/create")
  public String create(
      Model model, 
      @ModelAttribute("exchangeVO") ExchangeVO exchangeVO, 
      @RequestParam(name="classifyno", defaultValue="0") int classifyno) {
    ArrayList<ClassifyVOMenu> menu = this.classifyProc.menu();
    model.addAttribute("menu", menu);
    ClassifyVO classifyVO = this.classifyProc.read(classifyno); // 카테고리 정보를 출력하기위한 목적
    model.addAttribute("classifyVO", classifyVO);
    System.out.println(classifyVO);

    return "/exchange/create"; // /templates/exchange/create.html
  }

  /**
   * 등록 처리 http://localhost:9091/exchange/create
   * 
   * @return
   */
  @PostMapping(value = "/create")
  public String create(
      HttpServletRequest request, 
      HttpSession session, 
      Model model, 
      @ModelAttribute("exchangeVO") ExchangeVO exchangeVO,
      RedirectAttributes ra) {

    if (memberProc.isMemberAdmin(session)) { // 관리자로 로그인한경우
      // ------------------------------------------------------------------------------
      // 파일 전송 코드 시작
      // ------------------------------------------------------------------------------
      String file1 = ""; // 원본 파일명 image
      String file1saved = ""; // 저장된 파일명, image
      String thumb1 = ""; // preview image

      String upDir = Exchange.getUploadDir(); // 파일을 업로드할 폴더 준비
      // upDir = upDir + "/" + 한글을 제외한 카테고리 이름
      System.out.println("-> upDir: " + upDir);

      // 전송 파일이 없어도 file1MF 객체가 생성됨.
      // <input type='file' class="form-control" name='file1MF' id='file1MF'
      // value='' placeholder="파일 선택">
      MultipartFile mf = exchangeVO.getFile1MF();

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

          exchangeVO.setFile1(file1); // 순수 원본 파일명
          exchangeVO.setFile1saved(file1saved); // 저장된 파일명(파일명 중복 처리)
          exchangeVO.setThumb1(thumb1); // 원본이미지 축소판
          exchangeVO.setSize1(size1); // 파일 크기

        } else { // 전송 못하는 파일 형식
          ra.addFlashAttribute("code", "check_upload_file_fail"); // 업로드 할 수 없는 파일
          ra.addFlashAttribute("cnt", 0); // 업로드 실패
          ra.addFlashAttribute("url", "/exchange/msg"); // msg.html, redirect parameter 적용
          return "redirect:/exchange/msg"; // Post -> Get - param...
        }
      } else { // 글만 등록하는 경우
        System.out.println("-> 글만 등록");
      }

      // ------------------------------------------------------------------------------
      // 파일 전송 코드 종료
      // ------------------------------------------------------------------------------

      // Call By Reference: 메모리 공유, Hashcode 전달
    //  int memberno = (int) session.getAttribute("memberno"); // memberno FK
//      exchangeVO.setMemberno(memberno);
      int cnt = this.exchangeProc.create(exchangeVO);

      // ------------------------------------------------------------------------------
      // PK의 return
      // ------------------------------------------------------------------------------
      // System.out.println("--> exchangeno: " + exchangeVO.getExchangeno());
      // mav.addObject("exchangeno", exchangeVO.getExchangeno()); // redirect
      // parameter 적용
      // ------------------------------------------------------------------------------

      if (cnt == 1) {
        // type 1, 재업로드 발생
        // return "<h1>파일 업로드 성공</h1>"; // 연속 파일 업로드 발생

        // type 2, 재업로드 발생
        // model.addAttribute("cnt", cnt);
        // model.addAttribute("code", "create_success");
        // return "exchange/msg";

        // type 3 권장
        // return "redirect:/exchange/list_all"; // /templates/exchange/list_all.html

        // System.out.println("-> exchangeVO.getClassifyno(): " + exchangeVO.getClassifyno());
        // ra.addFlashAttribute("classifyno", exchangeVO.getClassifyno()); // controller ->
        // controller: X

        ra.addAttribute("classifyno", exchangeVO.getClassifyno()); // controller -> controller: O
        return "redirect:/exchange/list_by_classifyno";

        // return "redirect:/exchange/list_by_classifyno?classifyno=" + exchangeVO.getClassifyno();
        // // /templates/exchange/list_by_classifyno.html
      } else {
        ra.addFlashAttribute("code", "create_fail"); // DBMS 등록 실패
        ra.addFlashAttribute("cnt", 0); // 업로드 실패
        ra.addFlashAttribute("url", "/exchange/msg"); // msg.html, redirect parameter 적용
        return "redirect:/exchange/msg"; // Post -> Get - param...
      }
    } else { // 로그인 실패 한 경우
      return "redirect:/member/login_cookie_need"; // /member/login_cookie_need.html
    }
  }

  /**
   * 전체 목록, 관리자만 사용 가능 http://localhost:9091/exchange/list_all
   * 
   * @return
   */
  @GetMapping(value = "/list_all")
  public String list_all(HttpSession session, Model model) {
    // System.out.println("-> list_all");
    ArrayList<ClassifyVOMenu> menu = this.classifyProc.menu();
    model.addAttribute("menu", menu);

    if (this.memberProc.isMemberAdmin(session)) { // 관리자만 조회 가능
      ArrayList<ExchangeVO> list = this.exchangeProc.list_all(); // 모든 목록

      // Thymeleaf는 CSRF(크로스사이트) 스크립팅 해킹 방지 자동 지원
      // for문을 사용하여 객체를 추출, Call By Reference 기반의 원본 객체 값 변경
//      for (ExchangeVO exchangeVO : list) {
//        String title = exchangeVO.getTitle();
//        String content = exchangeVO.getContent();
//        
//        title = Tool.convertChar(title);  // 특수 문자 처리
//        content = Tool.convertChar(content); 
//        
//        exchangeVO.setTitle(title);
//        exchangeVO.setContent(content);  
//
//      }

      model.addAttribute("list", list);
      return "/exchange/list_all";

    } else {
      return "redirect:/member/login_cookie_need";

    }

  }

  /**
   * 유형 3
   * 카테고리별 목록 + 검색 + 페이징 http://localhost:9091/exchange/list_by_classifyno?classifyno=5
   * http://localhost:9091/exchange/list_by_classifyno?classifyno=6
   * 
   * @return
   */
  @GetMapping(value = "/list_by_classifyno1")
  public String list_by_classifyno_search_paging1(
      HttpSession session, 
      Model model, 
      @RequestParam(name = "classifyno", defaultValue = "1") int classifyno,
      @RequestParam(name = "word", defaultValue = "") String word,
      @RequestParam(name = "now_page", defaultValue = "1") int now_page) {

    // System.out.println("-> classifyno: " + classifyno);

    ArrayList<ClassifyVOMenu> menu = this.classifyProc.menu();
    model.addAttribute("menu", menu);

    ClassifyVO classifyVO = this.classifyProc.read(classifyno);
    ExchangeVO exchangeVO = this.exchangeProc.reading(classifyno);
    model.addAttribute("classifyVO", classifyVO);
    model.addAttribute("exchangeVO", exchangeVO);

    word = Tool.checkNull(word).trim();

    HashMap<String, Object> map = new HashMap<>();
    map.put("classifyno", classifyno);
    map.put("word", word);
    map.put("now_page", now_page);

    ArrayList<ExchangeVO> list = this.exchangeProc.list_by_classifyno_search_paging(map);
    model.addAttribute("list", list);

    // System.out.println("-> size: " + list.size());
    model.addAttribute("word", word);

    int search_count = this.exchangeProc.list_by_classifyno_search_count(map);
    String paging = this.exchangeProc.pagingBox(classifyno, now_page, word, "/exchange/list_by_classifyno", search_count,
        Exchange.RECORD_PER_PAGE, Exchange.PAGE_PER_BLOCK);
    model.addAttribute("paging", paging);
    model.addAttribute("now_page", now_page);

    model.addAttribute("search_count", search_count);

    // 일련 변호 생성: 레코드 갯수 - ((현재 페이지수 -1) * 페이지당 레코드 수)
    int no = search_count - ((now_page - 1) * Exchange.RECORD_PER_PAGE);
    model.addAttribute("no", no);

    return "/exchange/list_by_classifyno_search_paging"; // /templates/exchange/list_by_classifyno_search_paging.html
//    return "/exchange/read"; // /templates/exchange/list_by_classifyno_search_paging.html
  }

  
  /**
   * 유형 3
   * 카테고리별 목록 + 검색 + 페이징 http://localhost:9091/exchange/list_by_classifyno?classifyno=5
   * http://localhost:9091/exchange/list_by_classifyno?classifyno=6
   * 
   * @return
   */
  @GetMapping(value = "/list_by_classifyno")
  public String list_by_classifyno_search_paging(
      HttpSession session, 
      Model model, 
      @RequestParam(name = "classifyno", defaultValue = "1") int classifyno) {

    // System.out.println("-> classifyno: " + classifyno);

    ArrayList<ClassifyVOMenu> menu = this.classifyProc.menu();
    model.addAttribute("menu", menu);

    ClassifyVO classifyVO = this.classifyProc.read(classifyno);
    model.addAttribute("classifyVO", classifyVO);
    ExchangeVO exchangeVO = this.exchangeProc.reading(classifyno);
    model.addAttribute("exchangeVO", exchangeVO);

    HashMap<String, Object> map = new HashMap<>();
    map.put("classifyno", classifyno);
    return "/exchange/read"; // /templates/exchange/list_by_classifyno_search_paging.html
  }

  /**
   * 카테고리별 목록 + 검색 + 페이징 + Grid
   * http://localhost:9091/exchange/list_by_classifyno?classifyno=5
   * http://localhost:9091/exchange/list_by_classifyno?classifyno=6
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

    ArrayList<ClassifyVOMenu> menu = this.classifyProc.menu();
    model.addAttribute("menu", menu);

    ClassifyVO classifyVO = this.classifyProc.read(classifyno);
    model.addAttribute("classifyVO", classifyVO);

    word = Tool.checkNull(word).trim();

    HashMap<String, Object> map = new HashMap<>();
    map.put("classifyno", classifyno);
    map.put("word", word);
    map.put("now_page", now_page);

    ArrayList<ExchangeVO> list = this.exchangeProc.list_by_classifyno_search_paging(map);
    model.addAttribute("list", list);

    // System.out.println("-> size: " + list.size());
    model.addAttribute("word", word);

    int search_count = this.exchangeProc.list_by_classifyno_search_count(map);
    String paging = this.exchangeProc.pagingBox(classifyno, now_page, word, "/exchange/list_by_classifyno_grid", search_count,
        Exchange.RECORD_PER_PAGE, Exchange.PAGE_PER_BLOCK);
    model.addAttribute("paging", paging);
    model.addAttribute("now_page", now_page);

    model.addAttribute("search_count", search_count);

    // 일련 변호 생성: 레코드 갯수 - ((현재 페이지수 -1) * 페이지당 레코드 수)
    int no = search_count - ((now_page - 1) * Exchange.RECORD_PER_PAGE);
    model.addAttribute("no", no);

    // /templates/exchange/list_by_classifyno_search_paging_grid.html
    return "/exchange/list_by_classifyno_search_paging_grid";
  }

  /**
   * 조회 http://localhost:9091/exchange/read?exchangeno=17
   * 
   * @return
   */
  @GetMapping(value = "/read")
  public String read(Model model, 
      @RequestParam(name="exchangeno", defaultValue = "0") int exchangeno, 
      @RequestParam(name="word", defaultValue = "") String word, 
      @RequestParam(name="now_page", defaultValue = "1") int now_page) {
    
    ArrayList<ClassifyVOMenu> menu = this.classifyProc.menu();
    model.addAttribute("menu", menu);

    ExchangeVO exchangeVO = this.exchangeProc.read(exchangeno);

//    String title = exchangeVO.getTitle();
//    String content = exchangeVO.getContent();
//    
//    title = Tool.convertChar(title);  // 특수 문자 처리
//    content = Tool.convertChar(content); 
//    
//    exchangeVO.setTitle(title);
//    exchangeVO.setContent(content);  

    long size1 = exchangeVO.getSize1();
    String size1_label = Tool.unit(size1);
    exchangeVO.setSize1_label(size1_label);

    model.addAttribute("exchangeVO", exchangeVO);

    ClassifyVO classifyVO = this.classifyProc.read(exchangeVO.getClassifyno());
    model.addAttribute("classifyVO", classifyVO);

    // 조회에서 화면 하단에 출력
    // ArrayList<ReplyVO> reply_list = this.replyProc.list_exchange(exchangeno);
    // mav.addObject("reply_list", reply_list);

    model.addAttribute("word", word);
    model.addAttribute("now_page", now_page);

    return "/exchange/read";
  }

  /**
   * 조회 http://localhost:9093/exchange/read?classifyno=34
   * 조회 http://localhost:9093/exchange/read?exchangeno=17
   * 
   * @return
   */
  @GetMapping(value = "/reading")
  public String reading(Model model, 
      @RequestParam(name = "classifyno", defaultValue = "1") int classifyno) {
    
    ArrayList<ClassifyVOMenu> menu = this.classifyProc.menu();
    model.addAttribute("menu", menu);

    ExchangeVO exchangeVO = this.exchangeProc.reading(classifyno);

    long size1 = exchangeVO.getSize1();
    String size1_label = Tool.unit(size1);
    exchangeVO.setSize1_label(size1_label);

    model.addAttribute("exchangeVO", exchangeVO);

    ClassifyVO classifyVO = this.classifyProc.read(exchangeVO.getClassifyno());
    model.addAttribute("classifyVO", classifyVO);


    return "/exchange/read";
  }

  
  /**
   * 맵 등록/수정/삭제 폼 http://localhost:9091/exchange/map?exchangeno=19
   * 
   * @return
   */
  @GetMapping(value = "/map")
  public String map(Model model, 
                            @RequestParam(name="exchangeno", defaultValue="0") int exchangeno,
                            @RequestParam(name="classifyno", defaultValue="0") int classifyno
                            ) {
    ArrayList<ClassifyVOMenu> menu = this.classifyProc.menu();
    model.addAttribute("menu", menu);

//    ExchangeVO exchangeVO = this.exchangeProc.read(exchangeno); // map 정보 읽어 오기
    ExchangeVO exchangeVO = this.exchangeProc.reading(classifyno); // map 정보 읽어 오기
    model.addAttribute("exchangeVO", exchangeVO); // request.setAttribute("exchangeVO", exchangeVO);

    ClassifyVO classifyVO = this.classifyProc.read(exchangeVO.getClassifyno()); // 그룹 정보 읽기
    model.addAttribute("classifyVO", classifyVO);
System.out.println("겟 맵 확인");
    return "/exchange/map"; // //templates/exchange/map.html
  }


  /**
   * MAP 등록/수정/삭제 처리 http://localhost:9091/exchange/map
   * 
   * @param exchangeVO
   * @return
   */
  @PostMapping(value = "/map")
  public String map_update(Model model, 
      @RequestParam(name="classifyno", defaultValue = "0") int classifyno, 
      @RequestParam(name="exchangeno", defaultValue = "0") int exchangeno, 
      @RequestParam(name="map", defaultValue = "") String map) {
    
    HashMap<String, Object> hashMap = new HashMap<String, Object>();
    hashMap.put("classifyno", classifyno);
    hashMap.put("exchangeno", exchangeno);
    hashMap.put("map", map);
    
    ClassifyVO classifyVO = this.classifyProc.read(classifyno);
    model.addAttribute("classifyVO", classifyVO);
    ExchangeVO exchangeVO = this.exchangeProc.reading(classifyno);
    model.addAttribute("exchangeVO", exchangeVO);
    this.exchangeProc.map(hashMap);
    return "redirect:/exchange/list_by_classifyno?classifyno=" + classifyno;
  }

 
  /**
   * 수정 폼 http:// localhost:9091/exchange/update_text?exchangeno=1
   *
   */
  @GetMapping(value = "/update_text")
  public String update_text(
      HttpSession session, 
      Model model,  
      RedirectAttributes ra,
      @RequestParam(name="exchangeno", defaultValue = "0") int exchangeno,
      @RequestParam(name="word", defaultValue = "") String word,
      @RequestParam(name="now_page", defaultValue = "0") int now_page) {
    
    ArrayList<ClassifyVOMenu> menu = this.classifyProc.menu();
    model.addAttribute("menu", menu);

    model.addAttribute("word", word);
    model.addAttribute("now_page", now_page);

    if (this.memberProc.isMemberAdmin(session)) { // 관리자로 로그인한경우
      ExchangeVO exchangeVO = this.exchangeProc.read(exchangeno);
      model.addAttribute("exchangeVO", exchangeVO);

      ClassifyVO classifyVO = this.classifyProc.read(exchangeVO.getClassifyno());
      model.addAttribute("classifyVO", classifyVO);

      return "/exchange/update_text"; // /templates/exchange/update_text.html
      // String content = "장소:\n인원:\n준비물:\n비용:\n기타:\n";
      // model.addAttribute("content", content);

    } else {
      // ra.addAttribute("url", "/member/login_cookie_need"); // /templates/member/login_cookie_need.html
      // return "redirect:/exchange/msg"; // @GetMapping(value = "/msg")
      return "/member/login_cookie_need"; // /templates/member/login_cookie_need.html
    }

  }

  /**
   * 수정 처리 http://localhost:9091/exchange/update_text?exchangeno=1
   * 
   * @return
   */
  @PostMapping(value = "/update_text")
  public String update_text(
      HttpSession session, 
      Model model, 
      RedirectAttributes ra,
      @ModelAttribute("exchangeVO") ExchangeVO exchangeVO, 
      @RequestParam(name="search_word", defaultValue = "") String search_word, // exchangeVO.word와 구분 필요
      @RequestParam(name="now_page", defaultValue = "0") int now_page) {
    
    ra.addAttribute("word", search_word);
    ra.addAttribute("now_page", now_page);

    if (this.memberProc.isMemberAdmin(session)) { // 관리자 로그인 확인
      HashMap<String, Object> map = new HashMap<String, Object>();
      map.put("exchangeno", exchangeVO.getExchangeno());
//      map.put("passwd", exchangeVO.getPasswd());

      if (this.exchangeProc.password_check(map) == 1) { // 패스워드 일치
        this.exchangeProc.update_text(exchangeVO); // 글수정

        // mav 객체 이용
        ra.addAttribute("exchangeno", exchangeVO.getExchangeno());
        ra.addAttribute("classifyno", exchangeVO.getClassifyno());
        return "redirect:/exchange/read"; // @GetMapping(value = "/read")

      } else { // 패스워드 불일치
        ra.addFlashAttribute("code", "passwd_fail"); // redirect -> forward -> html
        ra.addFlashAttribute("cnt", 0);
        ra.addAttribute("url", "/exchange/msg"); // msg.html, redirect parameter 적용

        return "redirect:/exchange/post2get"; // @GetMapping(value = "/msg")
      }
    } else { // 정상적인 로그인이 아닌 경우 로그인 유도
      ra.addAttribute("url", "/member/login_cookie_need"); // /templates/member/login_cookie_need.html
      return "redirect:/exchange/post2get"; // @GetMapping(value = "/msg")
    }

  }

  /**
   * 파일 수정 폼 http://localhost:9091/exchange/update_file?exchangeno=1
   * 
   * @return
   */
  @GetMapping(value = "/update_file")
  public String update_file(
      HttpSession session, Model model, 
     @RequestParam(name="exchangeno", defaultValue = "0") int exchangeno,
     @RequestParam(name="word", defaultValue = "") String word, 
     @RequestParam(name="now_page", defaultValue = "1") int now_page) {
    ArrayList<ClassifyVOMenu> menu = this.classifyProc.menu();
    model.addAttribute("menu", menu);
    
    model.addAttribute("word", word);
    model.addAttribute("now_page", now_page);
    
    ExchangeVO exchangeVO = this.exchangeProc.read(exchangeno);
    model.addAttribute("exchangeVO", exchangeVO);

    ClassifyVO classifyVO = this.classifyProc.read(exchangeVO.getClassifyno());
    model.addAttribute("classifyVO", classifyVO);

    return "/exchange/update_file";

  }

  /**
   * 파일 수정 처리 http://localhost:9091/exchange/update_file
   * 
   * @return
   */
  @PostMapping(value = "/update_file")
  public String update_file(
      HttpSession session, Model model, RedirectAttributes ra,
       @ModelAttribute("exchangeVO") ExchangeVO exchangeVO,
       @RequestParam(name="word", defaultValue = "") String word, 
       @RequestParam(name="now_page", defaultValue = "1") int now_page) {
    if (this.memberProc.isMemberAdmin(session)) {
      // 삭제할 파일 정보를 읽어옴, 기존에 등록된 레코드 저장용
      ExchangeVO exchangeVO_old = exchangeProc.read(exchangeVO.getExchangeno());

      // -------------------------------------------------------------------
      // 파일 삭제 시작
      // -------------------------------------------------------------------
      String file1saved = exchangeVO_old.getFile1saved(); // 실제 저장된 파일명
      String thumb1 = exchangeVO_old.getThumb1(); // 실제 저장된 preview 이미지 파일명
      long size1 = 0;

      String upDir = Exchange.getUploadDir(); // C:/kd/deploy/resort_v4sbm3c/exchange/storage/

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
      MultipartFile mf = exchangeVO.getFile1MF();

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

      exchangeVO.setFile1(file1);
      exchangeVO.setFile1saved(file1saved);
      exchangeVO.setThumb1(thumb1);
      exchangeVO.setSize1(size1);
      // -------------------------------------------------------------------
      // 파일 전송 코드 종료
      // -------------------------------------------------------------------

      this.exchangeProc.update_file(exchangeVO); // Oracle 처리
      ra.addAttribute ("exchangeno", exchangeVO.getExchangeno());
      ra.addAttribute("classifyno", exchangeVO.getClassifyno());
      ra.addAttribute("word", word);
      ra.addAttribute("now_page", now_page);
      
      return "redirect:/exchange/read";
    } else {
      ra.addAttribute("url", "/member/login_cookie_need"); 
      return "redirect:/exchange/msg"; // GET
    }
  }

  /**
   * 파일 삭제 폼
   * http://localhost:9091/exchange/delete?exchangeno=1
   * 
   * @return
   */
  @GetMapping(value = "/delete")
  public String delete(
      HttpSession session, Model model, RedirectAttributes ra,
      @RequestParam(name="classifyno", defaultValue = "0") int classifyno,
      @RequestParam(name="exchangeno", defaultValue = "0") int exchangeno,
      @RequestParam(name="word", defaultValue = "") String word, 
      @RequestParam(name="now_page", defaultValue = "1") int now_page) {
    if (this.memberProc.isMemberAdmin(session)) { // 관리자로 로그인한경우
      model.addAttribute("classifyno", classifyno);
      model.addAttribute("word", word);
      model.addAttribute("now_page", now_page);
      
      ArrayList<ClassifyVOMenu> menu = this.classifyProc.menu();
      model.addAttribute("menu", menu);
      
      ExchangeVO exchangeVO = this.exchangeProc.read(exchangeno);
      model.addAttribute("exchangeVO", exchangeVO);
      
      ClassifyVO classifyVO = this.classifyProc.read(exchangeVO.getClassifyno());
      model.addAttribute("classifyVO", classifyVO);
      
      return "/exchange/delete"; // forward
      
    } else {
      ra.addAttribute("url", "/admin/login_cookie_need");
      return "redirect:/exchange/msg"; 
    }

  }
  
  /**
   * 삭제 처리 http://localhost:9091/exchange/delete
   * 
   * @return
   */
  @PostMapping(value = "/delete")
  public String delete(RedirectAttributes ra,
      @RequestParam(name="classifyno", defaultValue = "0") int classifyno,
      @RequestParam(name="exchangeno", defaultValue = "0") int exchangeno,
      @RequestParam(name="word", defaultValue = "") String word, 
      @RequestParam(name="now_page", defaultValue = "1") int now_page) {
    
    // -------------------------------------------------------------------
    // 파일 삭제 시작
    // -------------------------------------------------------------------
    // 삭제할 파일 정보를 읽어옴.
    ExchangeVO exchangeVO_read = exchangeProc.read(exchangeno);
        
    String file1saved = exchangeVO_read.getFile1saved();
    String thumb1 = exchangeVO_read.getThumb1();
    
    String uploadDir = Exchange.getUploadDir();
    Tool.deleteFile(uploadDir, file1saved);  // 실제 저장된 파일삭제
    Tool.deleteFile(uploadDir, thumb1);     // preview 이미지 삭제
    // -------------------------------------------------------------------
    // 파일 삭제 종료
    // -------------------------------------------------------------------
        
    this.exchangeProc.delete(exchangeno); // DBMS 글 삭제
        
    // -------------------------------------------------------------------------------------
    // 마지막 페이지의 마지막 레코드 삭제시의 페이지 번호 -1 처리
    // -------------------------------------------------------------------------------------    
    // 마지막 페이지의 마지막 10번째 레코드를 삭제후
    // 하나의 페이지가 3개의 레코드로 구성되는 경우 현재 9개의 레코드가 남아 있으면
    // 페이지수를 4 -> 3으로 감소 시켜야함, 마지막 페이지의 마지막 레코드 삭제시 나머지는 0 발생
    
    HashMap<String, Object> map = new HashMap<String, Object>();
    map.put("classifyno", classifyno);
    map.put("word", word);
    
    if (this.exchangeProc.list_by_classifyno_search_count(map) % Exchange.RECORD_PER_PAGE == 0) {
      now_page = now_page - 1; // 삭제시 DBMS는 바로 적용되나 크롬은 새로고침등의 필요로 단계가 작동 해야함.
      if (now_page < 1) {
        now_page = 1; // 시작 페이지
      }
    }
    // -------------------------------------------------------------------------------------

    ra.addAttribute("classifyno", classifyno);
    ra.addAttribute("word", word);
    ra.addAttribute("now_page", now_page);
    
    return "redirect:/exchange/list_by_classifyno";    
    
  }   
   
  
  /**
   * 우선 순위 높임, 10 등 -> 1 등, http://localhost:9091/cate/update_seqno_forward/1
   * 
   * @param model Controller -> Thymeleaf HTML로 데이터 전송에 사용
   * @return
   */
  @GetMapping(value = "/update_seqno_forward/{classifyno}")
  public String update_seqno_forward(Model model, @PathVariable("classifyno") Integer classifyno,
      @RequestParam(name = "word", defaultValue = "") String word, RedirectAttributes ra,
      @RequestParam(name = "now_page", defaultValue = "1") int now_page) {
    ra.addAttribute("now_page", now_page);
    this.classifyProc.update_seqno_forward(classifyno);

    ra.addAttribute("word", word);
    return "redirect:/classify/list_search"; // @GetMapping(value="/list_all")
  }

  /**
   * 우선 순위 낮춤, 1 등 -> 10 등, http://localhost:9091/cate/update_seqno_backward/1
   * 
   * @param model Controller -> Thymeleaf HTML로 데이터 전송에 사용
   * @return
   */
  @GetMapping(value = "/update_seqno_backward/{classifyno}")
  public String update_seqno_backward(Model model, @PathVariable("classifyno") Integer classifyno,
      @RequestParam(name = "word", defaultValue = "") String word, RedirectAttributes ra,
      @RequestParam(name = "now_page", defaultValue = "1") int now_page) {
    ra.addAttribute("now_page", now_page);
    this.classifyProc.update_seqno_backward(classifyno);

    ra.addAttribute("word", word);
    return "redirect:/classify/list_search"; // @GetMapping(value="/list_all")
  }

  /**
   * 카테고리 공개 설정, http://localhost:9091/cate/update_visible_y/1
   * 
   * @param model Controller -> Thymeleaf HTML로 데이터 전송에 사용
   * @return
   */
  @GetMapping(value = "/update_visible_y/{classifyno}")
  public String update_visible_y(Model model, @PathVariable("classifyno") Integer classifyno,
      @RequestParam(name = "word", defaultValue = "") String word, RedirectAttributes ra,
      @RequestParam(name = "now_page", defaultValue = "1") int now_page) {
    ra.addAttribute("now_page", now_page);
    this.classifyProc.update_visible_y(classifyno);

    ra.addAttribute("word", word);
    return "redirect:/classify/list_search"; // @GetMapping(value="/list_all")
  }

  /**
   * 카테고리 비공개 설정, http://localhost:9091/cate/update_visible_n/1
   * 
   * @param model Controller -> Thymeleaf HTML로 데이터 전송에 사용
   * @return
   */
  @GetMapping(value = "/update_visible_n/{classifyno}")
  public String update_visible_n(Model model, @PathVariable("classifyno") Integer classifyno,
      @RequestParam(name = "word", defaultValue = "") String word, RedirectAttributes ra,
      @RequestParam(name = "now_page", defaultValue = "1") int now_page) {
    ra.addAttribute("now_page", now_page);
    this.classifyProc.update_visible_n(classifyno);

    ra.addAttribute("word", word);
    return "redirect:/classify/list_search"; // @GetMapping(value="/list_all")
  }

 
}