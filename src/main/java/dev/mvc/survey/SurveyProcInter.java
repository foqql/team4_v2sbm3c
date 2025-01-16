package dev.mvc.survey;

import java.util.ArrayList;
import java.util.HashMap;

public interface SurveyProcInter {
  public int create(SurveyVO surveyVO);

  public ArrayList<SurveyVO> list_by_classifyno_search_paging(HashMap<String, Object> map);

  public String pagingBox(int now_page, String word, String list_file, int search_count, int record_per_page,
      int page_per_block);

  public int list_by_classifyno_search_count(HashMap<String, Object> map);

  public Integer list_search_count(String word);

  public SurveyVO read_item(Integer surveyno);

  public SurveyVO read(int a);

  public ArrayList<SurveyVO> read_item_list(Integer surveyno);

  public ArrayList<SurveyVO> list_search_paging(String word, int now_page, int record_per_page);

  public int update_pick_surveyitem(int surveyitemno);

  public int update_unpick_surveyitem(int surveyitemno);

  public int create_item(SurveyVO surveyVO);

  public int update(SurveyVO surveyVO);

  public int delete(SurveyVO surveyVO);

  public int update_item(SurveyVO surveyVO);

  public int delete_item(int surveyitemno);

  public int increaseRecom(int surveyno);

  public int decreaseRecom(int surveyno);

  public int good(int surveyno);

  public int update_file(SurveyVO surveyVO);

  public int cnt_sm(HashMap<String, Object> map);

  public ArrayList<SurveyVO> list_all(HashMap<String, Object> map);

  public int create_sm(HashMap<String, Object> map);

  public int delete_sm(int surveyno);

  public SurveyVO read_sm(HashMap<String, Object> map);
}
