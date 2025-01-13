package dev.mvc.newsscore;

import java.util.ArrayList;

public interface NewsscoreDAOInter {

  
  /**
   * 전체 목록
   * @return
   */
  public ArrayList<NewsscoreVO> list_all();
  
  /**
   * 삭제
   * @param calendarno
   * @return 삭제된 레코드 갯수
   */
  public int delete(int newsscoreno);

}
