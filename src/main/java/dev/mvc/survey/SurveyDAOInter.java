package dev.mvc.survey;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import dev.mvc.classify.ClassifyVO;

/**
 * Spring Boot가 자동 구현
 * 
 * @author soldesk
 *
 */
public interface SurveyDAOInter {
  public int create(SurveyVO surveyVO);

  public ArrayList<SurveyVO> list_search_paging(Map<String, Object> map);

  public ArrayList<SurveyVO> list_by_classifyno_search_paging(HashMap<String, Object> map);

  public int list_by_classifyno_search_count(HashMap<String, Object> map);

  public Integer list_search_count(String word);

  public SurveyVO read(int a);

  public SurveyVO read_item(Integer surveyno);

  public ArrayList<SurveyVO> read_item_list(Integer surveyno);

  public int update_pick_surveyitem(int surveyno);

  public int create_item(SurveyVO surveyVO);

  public int update(SurveyVO surveyVO);

  public int delete(SurveyVO surveyVO);

  public int update_item(SurveyVO surveyVO);

  public int delete_item(SurveyVO surveyVO);
}