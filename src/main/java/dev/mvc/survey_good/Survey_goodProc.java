package dev.mvc.survey_good;

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
    int cnt  = this.survey_goodDAO.create(survey_goodVO);
    return cnt;

  }
}
