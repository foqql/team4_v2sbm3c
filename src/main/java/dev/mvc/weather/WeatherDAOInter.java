package dev.mvc.weather;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import dev.mvc.classify.ClassifyVO;

/**
 * Spring Boot가 자동 구현
 * @author soldesk
 *
 */
public interface WeatherDAOInter {
  /**
   * 등록, 추상 메소드
   * @param weatherVO
   * @return
   */
  public int create(WeatherVO weatherVO);

  /**
   * 모든 카테고리의 등록된 글목록
   * @return
   */
  public ArrayList<WeatherVO> list_all();
  
  /**
   * 카테고리별 등록된 글 목록
   * @param classifyno
   * @return
   */
  public ArrayList<WeatherVO> list_by_classifyno(int classifyno);
  
  /**
   * 조회
   * @param weatherno
   * @return
   */
  public WeatherVO read(int weatherno);
  
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
  public ArrayList<WeatherVO> list_by_classifyno_search(HashMap<String, Object> hashMap);
  
  /**
   * 카테고리별 검색된 레코드 갯수
   * @param map
   * @return
   */
  public int list_by_classifyno_search_count(HashMap<String, Object> hashMap);
  
  /**
   * 카테고리별 검색 목록 + 페이징
   * @param weatherVO
   * @return
   */
  public ArrayList<WeatherVO> list_by_classifyno_search_paging(HashMap<String, Object> map);
  
  /**
   * 패스워드 검사
   * @param hashMap
   * @return
   */
  public int password_check(HashMap<String, Object> hashMap);
  
  /**
   * 글 정보 수정
   * @param weatherVO
   * @return 처리된 레코드 갯수
   */
  public int update_text(WeatherVO weatherVO);

  /**
   * 파일 정보 수정
   * @param weatherVO
   * @return 처리된 레코드 갯수
   */
  public int update_file(WeatherVO weatherVO);
 
  /**
   * 삭제
   * @param weatherno
   * @return 삭제된 레코드 갯수
   */
  public int delete(int weatherno);
  
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
  public int increaseReplycnt(int weatherno);
 
  /**
   * 글 수 감소
   * @param 
   * @return
   */   
  public int decreaseReplycnt(int weatherno);
}