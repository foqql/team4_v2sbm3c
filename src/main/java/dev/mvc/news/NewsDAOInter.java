package dev.mvc.news;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dev.mvc.classify.ClassifyVO;

/**
 * Spring Boot가 자동 구현
 * @author soldesk
 *
 */
public interface NewsDAOInter {
  /**
   * 등록, 추상 메소드
   * @param newsVO
   * @return
   */
  public int create(NewsVO newsVO);

  /**
   * 모든 카테고리의 등록된 글목록
   * @return
   */
  public ArrayList<NewsVO> list_all();


  /**
   * 카테고리별 등록된 글 목록
   * @param classifyno
   * @return
   */
  public ArrayList<NewsVO> list_by_classifyno(int classifyno);
  
  /**
   * 조회
   * @param newsno
   * @return
   */
  public NewsVO read(int newsno);
  
  /**
   * map 등록, 수정, 삭제
   * @param map
   * @return 수정된 레코드 갯수
   */
  public int map(HashMap<String, Object> map);

  /**
   * youtube 등록, 수정, 삭제
   * @param youtube
   * @return 수정된 레코드 갯수
   */
  public int youtube(HashMap<String, Object> map);
  
  /**
   * 카테고리별 검색 목록
   * @param map
   * @return
   */
  public ArrayList<NewsVO> list_by_classifyno_search(HashMap<String, Object> hashMap);
  
  /**
   * 카테고리별 검색된 레코드 갯수
   * @param map
   * @return
   */
  public int list_by_classifyno_search_count(HashMap<String, Object> hashMap);
  
  /**
   * 카테고리별 검색 목록 + 페이징
   * @param newsVO
   * @return
   */
  public ArrayList<NewsVO> list_by_classifyno_search_paging(HashMap<String, Object> map);
  
  /**
   * 패스워드 검사
   * @param hashMap
   * @return
   */
  public int password_check(HashMap<String, Object> hashMap);
  
  /**
   * 글 정보 수정
   * @param newsVO
   * @return 처리된 레코드 갯수
   */
  public int update_text(NewsVO newsVO);

  /**
   * 파일 정보 수정
   * @param newsVO
   * @return 처리된 레코드 갯수
   */
  public int update_file(NewsVO newsVO);
 
  /**
   * 삭제
   * @param newsno
   * @return 삭제된 레코드 갯수
   */
  public int delete(int newsno);
  
  /**
   * FK classifyno 값이 같은 레코드 갯수 산출
   * @param classifyno
   * @return
   */
  public int count_by_classifyno(int classifyno);
 
  /**
   * 특정 카테고리에 속한 모든 레코드 삭제
   * @param classifyno
   * @return 삭제된 레코드 갯수
   */
  public int delete_by_classifyno(int classifyno);
  
  /**
   * FK memberno 값이 같은 레코드 갯수 산출
   * @param memberno
   * @return
   */
  public int count_by_memberno(int memberno);
 
  /**
   * 특정 카테고리에 속한 모든 레코드 삭제
   * @param memberno
   * @return 삭제된 레코드 갯수
   */
  public int delete_by_memberno(int memberno);
  
  /**
   * 글 수 증가
   * @param 
   * @return
   */ 
  public int increaseReplycnt(int newsno);
 
  /**
   * 글 수 감소
   * @param 
   * @return
   */   
  public int decreaseReplycnt(int newsno);
  
//  public ArrayList<NewsVO> newsgenre(String newsgenre);
  
//  /**
//   * 검색 + 페이징 목록
//   * select id="list_search_paging" resultType="dev.mvc.cate.CateVO" parameterType="Map" 
//   * @param word 검색어
//   * @param now_page 현재 페이지, 시작 페이지 번호: 1 ★
//   * @param record_per_page 페이지당 출력할 레코드 수
//   * @return
//   */
//  public ArrayList<NewsVO> list_search_paging(Map<String, Object> map);
//
//  /**
//   * 검색 갯수
//   * @param word
//   * @return
//   */
//  public Integer list_search_count(String newsgenre);
  
  /**
   * 번역,요약 조회
   * @param newsno
   * @return
   */
  public NewsVO trans_sum(int newsno);
  
  /**
   * 추천 수 증가
   * @param 
   * @return
   */ 
  public int increaseRecom(int newsrecomno);
  
  /**
   * 추천 수 감소
   * @param 
   * @return
   */  
  public int decreaseRecom(int newsrecomno);
  
}