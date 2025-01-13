package dev.mvc.newsrecom;

import java.util.ArrayList;
import java.util.HashMap;

public interface NewsrecomProcInter {
  /**
   * 등록, 추상 메소드
   * @param NewsscoreVO
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
   * @param newsrecomno
   * @return 삭제된 레코드 갯수
   */
  public int delete(int newsrecomno);
  
  /**
   * 특정 컨텐츠의 특정 회원 추천 갯수 산출
   * @param map
   * @return 
   */
  public int heartCnt(HashMap<String, Object> map);
  
  /**
   * 조회
   * @param newsrecomno
   * @return
   */
  public NewsrecomVO read(int newsrecomno);
  
  /**
   * newsno, memberno 조회
   * @param calendarno
   * @return
   */
  public NewsrecomVO readByNewsnoMemberno(HashMap<String, Object> map);
  
  /**
   * 전체 목록 join 3개
   * @return
   */
  public ArrayList<NewsNewsrecomMemberVO> list_all_join();
}