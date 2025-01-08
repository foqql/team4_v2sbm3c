package dev.mvc.survey;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Spring Boot가 자동 구현
 * @author soldesk
 *
 */
public interface SurveyDAOInter {
  public int create(SurveyVO surveyVO);
  
  
  public ArrayList<SurveyVO> list_by_classifyno_search_paging(HashMap<String, Object> map);
  
  public int list_by_classifyno_search_count(HashMap<String, Object> map);
  
  public SurveyVO read(int a);
  
  public SurveyVO read_item(Integer surveyno);
  public ArrayList<SurveyVO> read_item_list(Integer surveyno);
}