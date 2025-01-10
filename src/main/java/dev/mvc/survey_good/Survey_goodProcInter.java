package dev.mvc.survey_good;

import java.util.ArrayList;
import java.util.HashMap;

import dev.mvc.survey.SurveyVO;

public interface Survey_goodProcInter {
  /**
   * 조회
   */
  public int create(Survey_goodVO survey_goodVO);

  public ArrayList<Survey_goodVO> list_all();

  public int delete(int surveygoodno);

  public int hartCnt(HashMap<String, Object> map);

  public Survey_goodVO read(int surveygoodno);

  public Survey_goodVO read_fk(HashMap<String, Object> map);

  public ArrayList<SSdMVO> list_all_join();
}
