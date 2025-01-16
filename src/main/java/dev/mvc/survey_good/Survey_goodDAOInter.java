package dev.mvc.survey_good;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Spring Boot가 자동 구현
 * 
 * @author soldesk
 *
 */
public interface Survey_goodDAOInter {

  public int create(Survey_goodVO survey_goodVO);

  public ArrayList<Survey_goodVO> list_all();

  public int delete(int surveygoodno);

  public int hartCnt(HashMap<String, Object> map);

  public Survey_goodVO read(int surveygoodno);

  public Survey_goodVO read_fk(HashMap<String, Object> map);

  public ArrayList<SSdMVO> list_all_join(HashMap<String, Object> map);

  public Integer list_search_count(String word);
}