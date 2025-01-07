package dev.mvc.newsrecom;

import java.util.ArrayList;
import java.util.HashMap;

public interface NewsrecomProcInter {
  /**
   * 등록, 추상 메소드
   * @param contentsgoodVO
   * @return
   */
  public int create(NewsrecomVO newsrecomVO);
  
  /**
   * 전체 목록
   * @return
   */
  public ArrayList<NewsrecomVO> list_all();
  
  /**
   * 삭제
   * @param calendarno
   * @return 삭제된 레코드 갯수
   */
  public int delete(int newsrecomno);
  
  /**
   * 특정 컨텐츠의 특정 회원 추천 갯수 산출
   * @param map
   * @return 
   */
  public int heartCnt(HashMap<String, Object> map);
}