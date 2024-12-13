package dev.mvc.classify;

import java.util.ArrayList;

public interface ClassifyProcInter {
  /**
   * <pre>
   * 등록
   * </pre>
   * 
   * @param ClassifyVO
   * @return
   */
  public int create(ClassifyVO classifyVO);

  /**
   * SQL -> CitaVO 레코드 수 만큼 생성 ArrayList<classifyVo> 객체 생성되어 ClassifyDAOInter로 리턴
   * <select id="list_all" resultClassify="dev.mvc.classify.ClassifyVO">
   * 
   * @return
   */
  public ArrayList<ClassifyVO> list_all();

  /**
   * 조회
   * 
   * @param classifyno
   * @return
   */
  public ClassifyVO read(Integer classifyno);

  /**
   * 수정
   * 
   * @param classifyVO
   * @return
   */
  public int update(ClassifyVO classifyVO);

  /**
   * 삭제
   * 
   * @param classifyVO
   * @return
   */
  public int delete(int classifyno);

  /**
   * 우선순위 상승
   * 
   * @param classifyno
   * @return
   */
  public int update_seqno_forward(int classifyno);

  /**
   * 우선순위 하락
   * 
   * @param classifyno
   * @return
   */
  public int update_seqno_backward(int classifyno);

  /**
   * 카테고리 출력
   * 
   * @param classifyno
   * @return
   */
  public int update_visible_y(int classifyno);

  /**
   * 카테고리 숨김
   * 
   * @param classifyno
   * @return
   */
  public int update_visible_n(int classifyno);

  public ArrayList<ClassifyVO> list_all_categrp_y();

  public ArrayList<ClassifyVO> list_all_cate_y(String type);

  /**
   * 화면 상단 메뉴
   * 
   * @return
   */
  public ArrayList<ClassifyVOMenu> menu();

  /**
   * 분류
   * 
   * @return
   */
  public ArrayList<String> classifyset();

  /**
   * 검색 결과
   * 
   * @param word
   * @return
   */
  public ArrayList<ClassifyVO> list_search(String word);

  /**
   * 검색 결과 개수
   * 
   * @param word
   * @return
   */
  public Integer list_search_count(String word);

  /**
   * 검색 + 페이징 <select id="list_search_paging" resultClassify="dev.mvc.classify.ClassifyVO"
   * parameterClassify="Map">
   * 
   * @param word            검색어
   * @param now_page        현재 페이지, 시작 페이지 : 1 ★
   * @param record_per_page 페이지당 출력할 레코드 수
   * @return
   */
  public ArrayList<ClassifyVO> list_search_paging(String word, int now_page, int record_per_page);

  /**
   * SPAN태그를 이용한 박스 모델의 지원, 1 페이지부터 시작 현재 페이지: 11 / 22 [이전] 11 12 13 14 15 16 17
   * 18 19 20 [다음]
   *
   * @param now_page        현재 페이지
   * @param word            검색어
   * @param list_file_name  목록 파일명
   * @param search_count    검색 레코드수
   * @param record_per_page 페이지당 레코드 수
   * @param page_per_block  블럭당 페이지 수
   * @return 페이징 생성 문자열
   */
  String pagingBox(int now_page, String word, String list_file_name, int search_count, int record_per_page,
      int page_per_block);
  
  public int update_classify_cnt();
  public int update_classify_genre_cnt();
}