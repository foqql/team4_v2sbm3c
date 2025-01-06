package dev.mvc.survey;

import java.util.ArrayList;
import java.util.HashMap;

public interface SurveyProcInter {
  public int create(SurveyVO surveyVO);
  
  

  public ArrayList<SurveyVO> list_by_classifyno_search_paging(HashMap<String, Object> map);



  String pagingBox(int now_page, String word, String list_file, int search_count, int record_per_page,
      int page_per_block);



  public int list_by_classifyno_search_count(HashMap<String, Object> map);
  
  
  public SurveyVO read_item(Integer surveyno);



  public SurveyVO read(int a);

  public ArrayList<SurveyVO> read_item_list(Integer surveyno);



  
}

