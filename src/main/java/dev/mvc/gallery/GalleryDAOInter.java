package dev.mvc.gallery;

import java.util.ArrayList;
import java.util.HashMap;

public interface GalleryDAOInter {

  
  /**
   * 등록, 추상 메소드
   * @param galleryVO
   * @return
   */
  public int create(GalleryVO galleryVO);
  
  /**
   * 모든 카테고리의 등록된 글목록
   * @return
   */
  public ArrayList<GalleryVO> list_all();
  
  /**
   * 카테고리별 등록된 글 목록
   * @param classifyno
   * @return
   */
  public ArrayList<GalleryVO> list_by_classifyno(int classifyno);
  
  /**
   * 조회
   * @param galleryno
   * @return
   */
  public GalleryVO read(int galleryno);
  
  
  /**
   * 사진 띄우기
   * @return
   */
  public ArrayList<GalleryVO> photolist();
  
  
  /**
   * 카테고리별 검색 목록
   * @param map
   * @return
   */
  public ArrayList<GalleryVO> list_by_classifyno_search(HashMap<String, Object> hashMap);
  
  
  /**
   * 카테고리별 검색된 레코드 갯수
   * @param map
   * @return
   */
  public int list_by_classifyno_search_count(HashMap<String, Object> hashMap);
  
  /**
   * 카테고리별 검색 목록 + 페이징
   * @param galleryVO
   * @return
   */
  public ArrayList<GalleryVO> list_by_classifyno_search_paging(HashMap<String, Object> map);
  
  /**
   * 글 정보 수정
   * @param galleryVO
   * @return 처리된 레코드 갯수
   */
  public int update_text(GalleryVO galleryVO);
  
  /**
   * 파일 정보 수정
   * @param galleryVO
   * @return 처리된 레코드 갯수
   */
  public int update_file(GalleryVO galleryVO);
  
  /**
   * 삭제
   * @param galleryno
   * @return 삭제된 레코드 갯수
   */
  public int delete(int galleryno);
  
  /**
   * FK classifyno 값이 같은 레코드 갯수 산출
   * @param classifyno
   * @return
   */
  public int count_by_classifyno(int classifyno);
  
  /**
   * FK memberno 값이 같은 레코드 갯수 산출
   * @param memberno
   * @return
   */
  public int count_by_memberno(int memberno);
  
  /**
   * 추천 수 증가
   * @param 
   * @return
   */ 
  public int increaseRecom(int galleryno);
 
  /**
   * 추천 수 감소
   * @param 
   * @return
   */   
  public int decreaseRecom(int galleryno);

}
