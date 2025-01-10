package dev.mvc.survey_good;

import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import dev.mvc.tool.Security;

@Component("dev.mvc.survey_good.Survey_goodProc")
public class Survey_goodProc implements Survey_goodProcInter {
  @Autowired
  Security security;

  @Autowired
  private Survey_goodDAOInter survey_goodDAO;

  /**
   * 조회
   */
  @Override
  public int create(Survey_goodVO survey_goodVO) {
    int cnt = this.survey_goodDAO.create(survey_goodVO);
    return cnt;

  }

  @Override
  public ArrayList<Survey_goodVO> list_all() {
    ArrayList<Survey_goodVO> list = this.survey_goodDAO.list_all();
    return list;
  }

  @Override
  public int delete(int surveygoodno) {
    return this.survey_goodDAO.delete(surveygoodno);
  }

  @Override
  public int hartCnt(HashMap<String, Object> map) {
    return this.survey_goodDAO.hartCnt(map);
  }

  @Override
  public Survey_goodVO read(int surveygoodno) {
    return this.survey_goodDAO.read(surveygoodno);
  }

  @Override
  public Survey_goodVO read_fk(HashMap<String, Object> map) {
    Survey_goodVO survey_goodVO = this.survey_goodDAO.read_fk(map);
    return survey_goodVO;
  }

  @Override
  public ArrayList<SSdMVO> list_all_join() {
    ArrayList<SSdMVO> list = this.survey_goodDAO.list_all_join();
    return list;
  }
  
  
}
