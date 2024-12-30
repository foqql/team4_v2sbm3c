package dev.mvc.exchange;

import java.util.HashMap;

/**
 * Spring Boot가 자동 구현
 * @author soldesk
 *
 */
public interface ExchangeDAOInter {
  
  /**
   * 조회
   * @param exchangeno
   * @return
   */
  public ExchangeVO reading(int classifyno);
  
  /**
   * map 등록, 수정, 삭제
   * @param map
   * @return 수정된 레코드 갯수
   */
  public int map(HashMap<String, Object> map);

  
}