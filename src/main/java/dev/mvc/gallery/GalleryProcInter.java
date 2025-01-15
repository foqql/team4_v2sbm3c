package dev.mvc.gallery;

import java.util.ArrayList;
import java.util.HashMap;

public interface GalleryProcInter {

  
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
   * SPAN태그를 이용한 박스 모델의 지원, 1 페이지부터 시작 
   * 현재 페이지: 11 / 22   [이전] 11 12 13 14 15 16 17 18 19 20 [다음] 
   *
   * @param cateno 카테고리 번호
   * @param now_page 현재 페이지
   * @param word 검색어
   * @param list_file 목록 파일명
   * @param search_count 검색 레코드수   
   * @param record_per_page 페이지당 레코드 수
   * @param page_per_block 블럭당 페이지 수
   * @return 페이징 생성 문자열
   */ 
  public String pagingBox(int classifyno, int now_page, String word, String list_file, int search_count, 
                                      int record_per_page, int page_per_block);   
  
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
}
