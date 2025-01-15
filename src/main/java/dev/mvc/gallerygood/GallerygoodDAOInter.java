package dev.mvc.gallerygood;

import java.util.ArrayList;
import java.util.HashMap;

public interface GallerygoodDAOInter {
  /**
   * 등록, 추상 메소드
   * @param calendarVO
   * @return
   */
  public int create(GallerygoodVO areagoodVO);
  
  
  /**
   * 전체 목록
   * @return
   */
  public ArrayList<GallerygoodVO> list_all();
  
  /**
   * 삭제
   * @param calendarno
   * @return 삭제된 레코드 갯수
   */
  public int delete(int areagoodno);
  
  /**
   * 특정 컨텐츠의 특정 회원 추천 갯수 산출
   * @param calendarno
   * @return 삭제된 레코드 갯수
   */
  public int hartCnt(HashMap<String, Object> map);
  
  
  
  /**
   * 조회
   * @param areagoodno
   * @return
   */
  public GallerygoodVO read(int areagoodno);
  
  
  
  /**
   * weatherno, memberno로 조회
   * @param areagoodno
   * @return
   */
  public GallerygoodVO readByGallerynoMemberno( HashMap<String, Object> map);

  /**
   * 모든 목록, 테이블 3개 join
   * @return
   */
  public ArrayList<GalleryGallerygoodMemberVO> list_all_join();
  
}