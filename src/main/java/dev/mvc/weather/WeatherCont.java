package dev.mvc.weather;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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

import dev.mvc.areagood.AreagoodProcInter;
import dev.mvc.areagood.AreagoodVO;
import dev.mvc.classify.ClassifyProcInter;
import dev.mvc.classify.ClassifyVO;
import dev.mvc.classify.ClassifyVOMenu;
import dev.mvc.gallery.GalleryProcInter;
import dev.mvc.gallery.GalleryVO;
import dev.mvc.genre.GenreProcInter;
import dev.mvc.genre.GenreVOMenu;
import dev.mvc.member.MemberProcInter;
import dev.mvc.tool.Tool;
import dev.mvc.tool.Upload;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@RequestMapping(value = "/weather")
@Controller
public class WeatherCont {
  @Autowired
  @Qualifier("dev.mvc.member.MemberProc") // @Service("dev.mvc.member.MemberProc")
  private MemberProcInter memberProc;

  @Autowired
  @Qualifier("dev.mvc.classify.ClassifyProc") // @Component("dev.mvc.classify.ClassifyProc")
  private ClassifyProcInter classifyProc;

  @Autowired
  @Qualifier("dev.mvc.weather.WeatherProc") // @Component("dev.mvc.weather.WeatherProc")
  private WeatherProcInter weatherProc;

  @Autowired
  @Qualifier("dev.mvc.genre.GenreProc") // @Component("dev.mvc.exchange.ExchangeProc")
  private GenreProcInter genreProc;

  @Autowired
  @Qualifier("dev.mvc.areagood.AreagoodProc") // @Component("dev.mvc.weather.WeatherProc")
  private AreagoodProcInter areagoodProc;
  
  @Autowired
  @Qualifier("dev.mvc.gallery.GalleryProc") // @Component("dev.mvc.weather.WeatherProc")
  private GalleryProcInter galleryProc;

  public WeatherCont() {
    System.out.println("-> WeatherCont created.");
  }

  /**
   * POST 요청시 새로고침 방지, POST 요청 처리 완료 → redirect → url → GET → forward -> html 데이터
   * 전송
   * 
   * @return
   */
  @GetMapping(value = "/post2get")
  public String post2get(Model model, @RequestParam(name = "url", defaultValue = "") String url) {
    ArrayList<ClassifyVOMenu> menu = this.classifyProc.menu();
    model.addAttribute("menu", menu);

    return url; // forward, /templates/...
  }

  // 등록 폼, weather 테이블은 FK로 classifyno를 사용함.
  // http://localhost:9091/weather/create X
  // http://localhost:9091/weather/create?classifyno=1 // classifyno 변수값을 보내는 목적
  // http://localhost:9091/weather/create?classifyno=2
  // http://localhost:9091/weather/create?classifyno=5
  @GetMapping(value = "/create")
  public String create(Model model, @ModelAttribute("weatherVO") WeatherVO weatherVO,
      @RequestParam(name = "classifyno", defaultValue = "0") int classifyno) {
    ArrayList<ClassifyVOMenu> menu = this.classifyProc.menu();
    model.addAttribute("menu", menu);
    
    ArrayList<GenreVOMenu> menu1 = this.genreProc.menu(); // 대분류
    model.addAttribute("menu1", menu1);

    ClassifyVO classifyVO = this.classifyProc.read(classifyno); // 카테고리 정보를 출력하기위한 목적
    model.addAttribute("classifyVO", classifyVO);

    return "/weather/create"; // /templates/weather/create.html
  }

  /**
   * 등록 처리 http://localhost:9091/weather/create
   * 
   * @return
   */
  @PostMapping(value = "/create")
  public String create(HttpServletRequest request, HttpSession session, Model model,
      @ModelAttribute("weatherVO") WeatherVO weatherVO, @RequestParam("continent") String continent,
      @RequestParam("country") String country, @RequestParam("city") String city, RedirectAttributes ra) {

    System.out.println("대륙 코드: " + continent);
    System.out.println("국가 코드: " + country);
    System.out.println("도시 코드: " + city);

    if (memberProc.isMemberAdmin(session)) {
      System.out.println("-> JSONContGradle created.");

      // ------------------------------------------------------------------------------
      // 파일 전송 코드 시작
      // ------------------------------------------------------------------------------
//      String file1 = ""; // 원본 파일명 image
//      String file1saved = ""; // 저장된 파일명, image
//      String thumb1 = ""; // preview image
//
//      String upDir = Weather.getUploadDir(); // 파일을 업로드할 폴더 준비
//      // upDir = upDir + "/" + 한글을 제외한 카테고리 이름
//      System.out.println("-> upDir: " + upDir);
//
//      // 전송 파일이 없어도 file1MF 객체가 생성됨.
//      // <input type='file' class="form-control" name='file1MF' id='file1MF'
//      // value='' placeholder="파일 선택">
//      MultipartFile mf = weatherVO.getFile1MF();
//
//      file1 = mf.getOriginalFilename(); // 원본 파일명 산출, 01.jpg
//      System.out.println("-> 원본 파일명 산출 file1: " + file1);
//
//      long size1 = mf.getSize(); // 파일 크기
//      if (size1 > 0) { // 파일 크기 체크, 파일을 올리는 경우
//        if (Tool.checkUploadFile(file1) == true) { // 업로드 가능한 파일인지 검사
//          // 파일 저장 후 업로드된 파일명이 리턴됨, spring.jsp, spring_1.jpg, spring_2.jpg...
//          file1saved = Upload.saveFileSpring(mf, upDir);
//
//          if (Tool.isImage(file1saved)) { // 이미지인지 검사
//            // thumb 이미지 생성후 파일명 리턴됨, width: 200, height: 150
//            thumb1 = Tool.preview(upDir, file1saved, 200, 150);
//          }
//
//          weatherVO.setFile1(file1); // 순수 원본 파일명
//          weatherVO.setFile1saved(file1saved); // 저장된 파일명(파일명 중복 처리)
//          weatherVO.setThumb1(thumb1); // 원본이미지 축소판
//          weatherVO.setSize1(size1); // 파일 크기
//
//        } else { // 전송 못하는 파일 형식
//          ra.addFlashAttribute("code", "check_upload_file_fail"); // 업로드 할 수 없는 파일
//          ra.addFlashAttribute("cnt", 0); // 업로드 실패
//          ra.addFlashAttribute("url", "/weather/msg"); // msg.html, redirect parameter 적용
//          return "redirect:/weather/msg"; // Post -> Get - param...
//        }
//      } else { // 글만 등록하는 경우
//        System.out.println("-> 글만 등록");
//      }

      // ------------------------------------------------------------------------------
      // 파일 전송 코드 종료
      // ------------------------------------------------------------------------------

      int memberno = (int) session.getAttribute("memberno"); // memberno FK
      weatherVO.setMemberno(memberno);
      int cnt = this.weatherProc.create(weatherVO);

      weatherVO.getAreano();

      // ------------------------------------------------------------------------------
      // 파이썬 실행 코드
      // ------------------------------------------------------------------------------
      try {

        String pythonScriptPath = "C:/kd/ws_python/team4/wea.py";
        System.out.println("Running Python script at: " + pythonScriptPath);

        ProcessBuilder processBuilder = new ProcessBuilder("python", pythonScriptPath, continent, country, city);
        processBuilder.redirectErrorStream(true);

        Process process = processBuilder.start();

        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line;
        while ((line = reader.readLine()) != null) {
          System.out.println(line);
        }

        int exitCode = process.waitFor();
        System.out.println("Python script finished with exit code: " + exitCode);
      } catch (IOException | InterruptedException e) {
        e.printStackTrace();
      }

      if (cnt == 1) {

        // controller: X

        ra.addAttribute("classifyno", weatherVO.getClassifyno()); // controller -> controller: O
        return "redirect:/weather/list_by_classifyno";

        // return "redirect:/weather/list_by_classifyno?classifyno=" +
        // weatherVO.getClassifyno();
        // // /templates/weather/list_by_classifyno.html

      } else {
        ra.addFlashAttribute("code", "create_fail"); // DBMS 등록 실패
        ra.addFlashAttribute("cnt", 0); // 업로드 실패
        ra.addFlashAttribute("url", "/weather/msg"); // msg.html, redirect parameter 적용
        return "redirect:/weather/msg"; // Post -> Get - param...
      }
    } else { // 로그인 실패 한 경우
      return "redirect:/member/login_cookie_need"; // /member/login_cookie_need.html
    }
  }

  /**
   * 전체 목록, 관리자만 사용 가능 http://localhost:9091/weather/list_all
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
      ArrayList<WeatherVO> list = this.weatherProc.list_all(); // 모든 목록

      model.addAttribute("list", list);
      return "/weather/list_all";

    } else {
      return "redirect:/member/login_cookie_need";

    }

  }
  
  
//  /**
//   * 날씨 목록에 사진 http://localhost:9091/gallery/list_all
//   * 
//   * @return
//   */
//  @GetMapping(value = "/photos")
//  public String photos(HttpSession session, Model model) {
//    
//      ArrayList<GalleryVO> list = this.galleryProc.photos(); // 모든 목록
//      model.addAttribute("list", list);
//      
//      return "/gallery/list_by_classifyno_search_paging";
//    
//  }
  

  /**
   * 유형 3 카테고리별 목록 + 검색 + 페이징
   * http://localhost:9091/weather/list_by_classifyno?classifyno=5
   * http://localhost:9091/weather/list_by_classifyno?classifyno=6
   * 
   * @return
   */
  @GetMapping(value = "/list_by_classifyno")
  public String list_by_classifyno_search_paging(HttpSession session, Model model,
      @RequestParam(name = "classifyno", defaultValue = "1") int classifyno,
      @RequestParam(name = "word", defaultValue = "") String word,
      @RequestParam(name = "now_page", defaultValue = "1") int now_page) {

    // 메뉴 데이터 추가
    ArrayList<ClassifyVOMenu> menu = this.classifyProc.menu();
    model.addAttribute("menu", menu);
    
    ArrayList<GenreVOMenu> menu1 = this.genreProc.menu(); // 대분류
    model.addAttribute("menu1", menu1);
    
    ArrayList<GalleryVO> photolist = this.galleryProc.photolist(); // 모든 목록
    model.addAttribute("photolist", photolist);
    
   // System.out.println("-> photolist: " + photolist);

    // classifyno가 9일 경우에만 arealist를 추가
    if (classifyno == 9) {
      List<WeatherVO> arealist = this.weatherProc.arealist();
      model.addAttribute("arealist", arealist);
    }
    
   

    // 분류 정보 가져오기
    ClassifyVO classifyVO = this.classifyProc.read(classifyno);
    model.addAttribute("classifyVO", classifyVO);
    word = Tool.checkNull(word).trim();

    // 검색 및 페이징 처리
    HashMap<String, Object> map = new HashMap<>();
    map.put("classifyno", classifyno);
    map.put("word", word);
    map.put("now_page", now_page);

    ArrayList<WeatherVO> list = this.weatherProc.list_by_classifyno_search_paging(map);
    model.addAttribute("list", list);

    // System.out.println("-> list: " + list);
    
    model.addAttribute("word", word);

    int search_count = this.weatherProc.list_by_classifyno_search_count(map);
    String paging = this.weatherProc.pagingBox(classifyno, now_page, word, "/weather/list_by_classifyno", search_count,
        Weather.RECORD_PER_PAGE, Weather.PAGE_PER_BLOCK);
    model.addAttribute("paging", paging);
    model.addAttribute("now_page", now_page);

    model.addAttribute("search_count", search_count);

    // 일련 변호 생성: 레코드 갯수 - ((현재 페이지수 -1) * 페이지당 레코드 수)
    int no = search_count - ((now_page - 1) * Weather.RECORD_PER_PAGE);
    model.addAttribute("no", no);

    return "/weather/list_by_classifyno_search_paging"; // /templates/weather/list_by_classifyno_search_paging.html
  }

  /**
   * 카테고리별 목록 + 검색 + 페이징 + Grid
   * http://localhost:9091/weather/list_by_classifyno?classifyno=5
   * http://localhost:9091/weather/list_by_classifyno?classifyno=6
   * 
   * @return
   */
  @GetMapping(value = "/list_by_classifyno_grid")
  public String list_by_classifyno_search_paging_grid(HttpSession session, Model model,
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

    word = Tool.checkNull(word).trim();

    HashMap<String, Object> map = new HashMap<>();
    map.put("classifyno", classifyno);
    map.put("word", word);
    map.put("now_page", now_page);

    ArrayList<WeatherVO> list = this.weatherProc.list_by_classifyno_search_paging(map);
    model.addAttribute("list", list);

    // System.out.println("-> size: " + list.size());
    model.addAttribute("word", word);

    int search_count = this.weatherProc.list_by_classifyno_search_count(map);
    String paging = this.weatherProc.pagingBox(classifyno, now_page, word, "/weather/list_by_classifyno_grid",
        search_count, Weather.RECORD_PER_PAGE, Weather.PAGE_PER_BLOCK);
    model.addAttribute("paging", paging);
    model.addAttribute("now_page", now_page);

    model.addAttribute("search_count", search_count);

    // 일련 변호 생성: 레코드 갯수 - ((현재 페이지수 -1) * 페이지당 레코드 수)
    int no = search_count - ((now_page - 1) * Weather.RECORD_PER_PAGE);
    model.addAttribute("no", no);

    // /templates/weather/list_by_classifyno_search_paging_grid.html
    return "/weather/list_by_classifyno_search_paging_grid";
  }

  /**
   * 조회 http://localhost:9091/weather/read?weatherno=17
   * 
   * @return
   */
  @GetMapping(value = "/read")
  public String read(HttpSession session, Model model,
      @RequestParam(name = "weatherno", defaultValue = "0") int weatherno,
      @RequestParam(name = "word", defaultValue = "") String word,
      @RequestParam(name = "now_page", defaultValue = "1") int now_page) {

    ArrayList<ClassifyVOMenu> menu = this.classifyProc.menu();
    model.addAttribute("menu", menu);

    ArrayList<GenreVOMenu> menu1 = this.genreProc.menu(); // 대분류
    model.addAttribute("menu1", menu1);

    WeatherVO weatherVO = this.weatherProc.read(weatherno);

    long size1 = weatherVO.getSize1();
    String size1_label = Tool.unit(size1);
    weatherVO.setSize1_label(size1_label);

    model.addAttribute("weatherVO", weatherVO);

    ClassifyVO classifyVO = this.classifyProc.read(weatherVO.getClassifyno());
    model.addAttribute("classifyVO", classifyVO);

    model.addAttribute("word", word);
    model.addAttribute("now_page", now_page);

    // -------------------------------------------------------------------------------------------
    // 추천 관련
    // -------------------------------------------------------------------------------------------
    HashMap<String, Object> map = new HashMap<String, Object>();
    map.put("weatherno", weatherno);

    int hartCnt = 0;
    if (session.getAttribute("memberno") != null) { // 회원인 경우만 카운트 처리
      int memberno = (int) session.getAttribute("memberno");
      map.put("memberno", memberno);

      System.out.println("->recom: " + weatherVO.getRecom());


      hartCnt = this.areagoodProc.hartCnt(map);

    }
    model.addAttribute("hartCnt", hartCnt);
    // ---------------------------------------------------------------

    return "/weather/read";
  }

  /**
   * 맵 등록/수정/삭제 폼 http://localhost:9091/weather/map?weatherno=19
   * 
   * @return
   */
  @GetMapping(value = "/map")
  public String map(Model model, @RequestParam(name = "weatherno", defaultValue = "0") int weatherno) {
    ArrayList<ClassifyVOMenu> menu = this.classifyProc.menu();
    model.addAttribute("menu", menu);
    
    ArrayList<GenreVOMenu> menu1 = this.genreProc.menu(); // 대분류
    model.addAttribute("menu1", menu1);

    WeatherVO weatherVO = this.weatherProc.read(weatherno); // map 정보 읽어 오기
    model.addAttribute("weatherVO", weatherVO); // request.setAttribute("weatherVO", weatherVO);

    ClassifyVO classifyVO = this.classifyProc.read(weatherVO.getClassifyno()); // 그룹 정보 읽기
    model.addAttribute("classifyVO", classifyVO);

    return "/weather/map"; // //templates/weather/map.html
  }

  /**
   * MAP 등록/수정/삭제 처리 http://localhost:9091/weather/map
   * 
   * @param weatherVO
   * @return
   */
  @PostMapping(value = "/map")
  public String map_update(Model model, @RequestParam(name = "weatherno", defaultValue = "0") int weatherno,
      @RequestParam(name = "map", defaultValue = "") String map) {
    HashMap<String, Object> hashMap = new HashMap<String, Object>();
    hashMap.put("weatherno", weatherno);
    hashMap.put("map", map);

    this.weatherProc.map(hashMap);

    return "redirect:/weather/read?weatherno=" + weatherno;
  }

  /**
   * Youtube 등록/수정/삭제 폼 http://localhost:9091/weather/youtube?weatherno=1
   * 
   * @return
   */
  @GetMapping(value = "/youtube")
  public String youtube(Model model, @RequestParam(name = "weatherno", defaultValue = "0") int weatherno,
      @RequestParam(name = "word", defaultValue = "") String word,
      @RequestParam(name = "now_page", defaultValue = "0") int now_page) {
    ArrayList<ClassifyVOMenu> menu = this.classifyProc.menu();
    model.addAttribute("menu", menu);
    
    ArrayList<GenreVOMenu> menu1 = this.genreProc.menu(); // 대분류
    model.addAttribute("menu1", menu1);

    WeatherVO weatherVO = this.weatherProc.read(weatherno); // map 정보 읽어 오기
    model.addAttribute("weatherVO", weatherVO); // request.setAttribute("weatherVO", weatherVO);

    ClassifyVO classifyVO = this.classifyProc.read(weatherVO.getClassifyno()); // 그룹 정보 읽기
    model.addAttribute("classifyVO", classifyVO);

    model.addAttribute("word", word);
    model.addAttribute("now_page", now_page);

    return "/weather/youtube"; // forward, /templates/weather/youtube.html
  }

  /**
   * Youtube 등록/수정/삭제 처리 http://localhost:9091/weather/youtube
   * 
   * @param weatherVO
   * @return
   */
  @PostMapping(value = "/youtube")
  public String youtube_update(Model model, RedirectAttributes ra,
      @RequestParam(name = "weatherno", defaultValue = "0") int weatherno,
      @RequestParam(name = "youtube", defaultValue = "") String youtube,
      @RequestParam(name = "word", defaultValue = "") String word,
      @RequestParam(name = "now_page", defaultValue = "0") int now_page) {

    if (youtube.trim().length() > 0) { // 삭제 중인지 확인, 삭제가 아니면 youtube 크기 변경
      youtube = Tool.youtubeResize(youtube, 640); // youtube 영상의 크기를 width 기준 640 px로 변경
    }

    HashMap<String, Object> hashMap = new HashMap<String, Object>();
    hashMap.put("weatherno", weatherno);
    hashMap.put("youtube", youtube);

    this.weatherProc.youtube(hashMap);

    ra.addAttribute("weatherno", weatherno);
    ra.addAttribute("word", word);
    ra.addAttribute("now_page", now_page);

    // return "redirect:/weather/read?weatherno=" + weatherno;
    return "redirect:/weather/read";
  }

  /**
   * 수정 폼 http:// localhost:9091/weather/update_text?weatherno=1
   *
   */
  @GetMapping(value = "/update_text")
  public String update_text(HttpSession session, Model model, RedirectAttributes ra,
      @RequestParam(name = "weatherno", defaultValue = "0") int weatherno,
      @RequestParam(name = "word", defaultValue = "") String word,
      @RequestParam(name = "now_page", defaultValue = "0") int now_page) {

    ArrayList<ClassifyVOMenu> menu = this.classifyProc.menu();
    model.addAttribute("menu", menu);
    
    ArrayList<GenreVOMenu> menu1 = this.genreProc.menu(); // 대분류
    model.addAttribute("menu1", menu1);

    model.addAttribute("word", word);
    model.addAttribute("now_page", now_page);

    if (this.memberProc.isMemberAdmin(session)) { // 관리자로 로그인한경우
      WeatherVO weatherVO = this.weatherProc.read(weatherno);
      model.addAttribute("weatherVO", weatherVO);

      ClassifyVO classifyVO = this.classifyProc.read(weatherVO.getClassifyno());
      model.addAttribute("classifyVO", classifyVO);

      return "/weather/update_text"; // /templates/weather/update_text.html

    } else {

      return "/member/login_cookie_need"; // /templates/member/login_cookie_need.html
    }

  }

  /**
   * 수정 처리 http://localhost:9091/weather/update_text?weatherno=1
   * 
   * @return
   */
  @PostMapping(value = "/update_text")
  public String update_text(HttpSession session, Model model, RedirectAttributes ra,
      @ModelAttribute("weatherVO") WeatherVO weatherVO,
      @RequestParam(name = "search_word", defaultValue = "") String search_word, // weatherVO.word와 구분 필요
      @RequestParam(name = "now_page", defaultValue = "0") int now_page) {

    ra.addAttribute("word", search_word);
    ra.addAttribute("now_page", now_page);

//    System.out.println("-> passwd: " +weatherVO.getPasswd());
//    System.out.println("-> 날씨: " +weatherVO.getWeather());
//    System.out.println("-> 기온: " +weatherVO.getTemp());

    if (this.memberProc.isMemberAdmin(session)) { // 관리자 로그인 확인
      HashMap<String, Object> map = new HashMap<String, Object>();
      map.put("weatherno", weatherVO.getWeatherno());
      map.put("passwd", weatherVO.getPasswd());

      // System.out.println("-> this.weatherProc.password_check(map):" +
      // this.weatherProc.password_check(map));

      if (this.weatherProc.password_check(map) == 1) { // 패스워드 일치
        this.weatherProc.update_text(weatherVO); // 글수정

        // mav 객체 이용
        ra.addAttribute("weatherno", weatherVO.getWeatherno());
        ra.addAttribute("classifyno", weatherVO.getClassifyno());
        return "redirect:/weather/read"; // @GetMapping(value = "/read")

      } else { // 패스워드 불일치
        System.out.println("-> 불일치");
        ra.addFlashAttribute("code", "passwd_fail"); // redirect -> forward -> html
        ra.addFlashAttribute("cnt", 0);
        ra.addAttribute("url", "/weather/msg"); // msg.html, redirect parameter 적용

        return "redirect:/weather/post2get"; // @GetMapping(value = "/msg")
      }
    } else { // 정상적인 로그인이 아닌 경우 로그인 유도
      ra.addAttribute("url", "/member/login_cookie_need"); // /templates/member/login_cookie_need.html
      return "redirect:/weather/post2get"; // @GetMapping(value = "/msg")
    }

  }

  /**
   * 파일 수정 폼 http://localhost:9091/weather/update_file?weatherno=1
   * 
   * @return
   */
  @GetMapping(value = "/update_file")
  public String update_file(HttpSession session, Model model,
      @RequestParam(name = "weatherno", defaultValue = "0") int weatherno,
      @RequestParam(name = "word", defaultValue = "") String word,
      @RequestParam(name = "now_page", defaultValue = "1") int now_page) {
    ArrayList<ClassifyVOMenu> menu = this.classifyProc.menu();
    model.addAttribute("menu", menu);
    
    ArrayList<GenreVOMenu> menu1 = this.genreProc.menu(); // 대분류
    model.addAttribute("menu1", menu1);

    model.addAttribute("word", word);
    model.addAttribute("now_page", now_page);

    WeatherVO weatherVO = this.weatherProc.read(weatherno);
    model.addAttribute("weatherVO", weatherVO);

    ClassifyVO classifyVO = this.classifyProc.read(weatherVO.getClassifyno());
    model.addAttribute("classifyVO", classifyVO);

    return "/weather/update_file";

  }

  /**
   * 파일 수정 처리 http://localhost:9091/weather/update_file
   * 
   * @return
   */
  @PostMapping(value = "/update_file")
  public String update_file(HttpSession session, Model model, RedirectAttributes ra,
      @ModelAttribute("weatherVO") WeatherVO weatherVO, @RequestParam(name = "word", defaultValue = "") String word,
      @RequestParam(name = "now_page", defaultValue = "1") int now_page) {
    if (this.memberProc.isMemberAdmin(session)) {
      // 삭제할 파일 정보를 읽어옴, 기존에 등록된 레코드 저장용
      WeatherVO weatherVO_old = weatherProc.read(weatherVO.getWeatherno());

      // -------------------------------------------------------------------
      // 파일 삭제 시작
      // -------------------------------------------------------------------
      String file1saved = weatherVO_old.getFile1saved(); // 실제 저장된 파일명
      String thumb1 = weatherVO_old.getThumb1(); // 실제 저장된 preview 이미지 파일명
      long size1 = 0;

      String upDir = Weather.getUploadDir(); // C:/kd/deploy/resort_v4sbm3c/weather/storage/

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
      MultipartFile mf = weatherVO.getFile1MF();

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

      weatherVO.setFile1(file1);
      weatherVO.setFile1saved(file1saved);
      weatherVO.setThumb1(thumb1);
      weatherVO.setSize1(size1);
      // -------------------------------------------------------------------
      // 파일 전송 코드 종료
      // -------------------------------------------------------------------

      this.weatherProc.update_file(weatherVO); // Oracle 처리
      ra.addAttribute("weatherno", weatherVO.getWeatherno());
      ra.addAttribute("classifyno", weatherVO.getClassifyno());
      ra.addAttribute("word", word);
      ra.addAttribute("now_page", now_page);

      return "redirect:/weather/read";
    } else {
      ra.addAttribute("url", "/member/login_cookie_need");
      return "redirect:/weather/msg"; // GET
    }
  }

  /**
   * 파일 삭제 폼 http://localhost:9091/weather/delete?weatherno=1
   * 
   * @return
   */
  @GetMapping(value = "/delete")
  public String delete(HttpSession session, Model model, RedirectAttributes ra,
      @RequestParam(name = "classifyno", defaultValue = "0") int classifyno,
      @RequestParam(name = "weatherno", defaultValue = "0") int weatherno,
      @RequestParam(name = "word", defaultValue = "") String word,
      @RequestParam(name = "now_page", defaultValue = "1") int now_page) {
    if (this.memberProc.isMemberAdmin(session)) { // 관리자로 로그인한경우
      model.addAttribute("classifyno", classifyno);
      model.addAttribute("word", word);
      model.addAttribute("now_page", now_page);

      ArrayList<ClassifyVOMenu> menu = this.classifyProc.menu();
      model.addAttribute("menu", menu);
      
      ArrayList<GenreVOMenu> menu1 = this.genreProc.menu(); // 대분류
      model.addAttribute("menu1", menu1);

      WeatherVO weatherVO = this.weatherProc.read(weatherno);
      model.addAttribute("weatherVO", weatherVO);

      ClassifyVO classifyVO = this.classifyProc.read(weatherVO.getClassifyno());
      model.addAttribute("classifyVO", classifyVO);

      return "/weather/delete"; // forward

    } else {
      ra.addAttribute("url", "/admin/login_cookie_need");
      return "redirect:/weather/msg";
    }

  }

  /**
   * 삭제 처리 http://localhost:9091/weather/delete
   * 
   * @return
   */
  @PostMapping(value = "/delete")
  public String delete(RedirectAttributes ra, @RequestParam(name = "classifyno", defaultValue = "0") int classifyno,
      @RequestParam(name = "weatherno", defaultValue = "0") int weatherno,
      @RequestParam(name = "word", defaultValue = "") String word,
      @RequestParam(name = "now_page", defaultValue = "1") int now_page) {

    // -------------------------------------------------------------------
    // 파일 삭제 시작
    // -------------------------------------------------------------------
    // 삭제할 파일 정보를 읽어옴.
    WeatherVO weatherVO_read = weatherProc.read(weatherno);

    String file1saved = weatherVO_read.getFile1saved();
    String thumb1 = weatherVO_read.getThumb1();

    String uploadDir = Weather.getUploadDir();
    Tool.deleteFile(uploadDir, file1saved); // 실제 저장된 파일삭제
    Tool.deleteFile(uploadDir, thumb1); // preview 이미지 삭제
    // -------------------------------------------------------------------
    // 파일 삭제 종료
    // -------------------------------------------------------------------

    this.weatherProc.delete(weatherno); // DBMS 글 삭제

    // -------------------------------------------------------------------------------------
    // 마지막 페이지의 마지막 레코드 삭제시의 페이지 번호 -1 처리
    // -------------------------------------------------------------------------------------
    // 마지막 페이지의 마지막 10번째 레코드를 삭제후
    // 하나의 페이지가 3개의 레코드로 구성되는 경우 현재 9개의 레코드가 남아 있으면
    // 페이지수를 4 -> 3으로 감소 시켜야함, 마지막 페이지의 마지막 레코드 삭제시 나머지는 0 발생

    HashMap<String, Object> map = new HashMap<String, Object>();
    map.put("classifyno", classifyno);
    map.put("word", word);

    if (this.weatherProc.list_by_classifyno_search_count(map) % Weather.RECORD_PER_PAGE == 0) {
      now_page = now_page - 1; // 삭제시 DBMS는 바로 적용되나 크롬은 새로고침등의 필요로 단계가 작동 해야함.
      if (now_page < 1) {
        now_page = 1; // 시작 페이지
      }
    }
    // -------------------------------------------------------------------------------------

    ra.addAttribute("classifyno", classifyno);
    ra.addAttribute("word", word);
    ra.addAttribute("now_page", now_page);

    return "redirect:/weather/list_by_classifyno";
  }

  /**
   * 추천 처리 http://localhost:9091/contents/good
   * 
   * @return
   */
  @PostMapping(value = "/good")
  @ResponseBody
  public String good(HttpSession session, Model model, @RequestBody String json_src) {
    System.out.println("-> json_src: " + json_src); // json_src: {"weatherno":"5"}

    JSONObject src = new JSONObject(json_src); // String -> JSON
    int weatherno = (int) src.get("weatherno"); // 값 가져오기
    System.out.println("-> weatherno: " + weatherno);

    if (this.memberProc.isMember(session)) { // 회원 로그인 확인
      // 추천을 한 상태인지 확인
      int memberno = (int) session.getAttribute("memberno");

      HashMap<String, Object> map = new HashMap<String, Object>();
      map.put("weatherno", weatherno);
      map.put("memberno", memberno);

      int good_cnt = this.areagoodProc.hartCnt(map);
      System.out.println("-> good_cnt: " + good_cnt);

      if (good_cnt == 1) {
        System.out.println("-> 추천 해제: " + weatherno + ' ' + memberno);

        AreagoodVO areagoodVO = this.areagoodProc.readByWeathernoMemberno(map);

        this.areagoodProc.delete(areagoodVO.getAreagoodno()); // 추천 삭제
        this.weatherProc.decreaseRecom(weatherno); // 카운트 감소
      } else {
        System.out.println("-> 추천: " + weatherno + ' ' + memberno);

        AreagoodVO areagoodVO_new = new AreagoodVO();
        areagoodVO_new.setWeatherno(weatherno);
        areagoodVO_new.setMemberno(memberno);

        this.areagoodProc.create(areagoodVO_new);
        this.weatherProc.increaseRecom(weatherno); // 카운트 증가
      }

      // 추천 여부가 변경되어 다시 새로운 값을 읽어옴.
      int hartCnt = this.areagoodProc.hartCnt(map);
      int recom = this.weatherProc.read(weatherno).getRecom();

      JSONObject result = new JSONObject();
      result.put("isMember", 1); // 로그인: 1, 비회원: 0
      result.put("hartCnt", hartCnt); // 추천 여부, 추천:1, 비추천: 0
      result.put("recom", recom); // 추천인수

      System.out.println("-> result.toString(): " + result.toString());
      return result.toString();

    } else { // 정상적인 로그인이 아닌 경우 로그인 유도
      JSONObject result = new JSONObject();
      result.put("isMember", 0); // 로그인: 1, 비회원: 0

      System.out.println("-> result.toString(): " + result.toString());
      return result.toString();
    }

  }

}