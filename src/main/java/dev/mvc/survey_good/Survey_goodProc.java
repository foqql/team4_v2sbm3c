package dev.mvc.survey_good;

import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import dev.mvc.survey.Survey;
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
  public ArrayList<SSdMVO> list_all_join(HashMap<String, Object> map) {
    /*
     * 예) 페이지당 10개의 레코드 출력 1 page: WHERE r >= 1 AND r <= 10 2 page: WHERE r >= 11
     * AND r <= 20 3 page: WHERE r >= 21 AND r <= 30
     * 
     * 페이지에서 출력할 시작 레코드 번호 계산 기준값, nowPage는 1부터 시작 1 페이지 시작 rownum: now_page = 1, (1
     * - 1) * 10 --> 0 2 페이지 시작 rownum: now_page = 2, (2 - 1) * 10 --> 10 3 페이지 시작
     * rownum: now_page = 3, (3 - 1) * 10 --> 20
     */
    int begin_of_page = ((int)map.get("now_page") - 1) * Survey.RECORD_PER_PAGE;

    // 시작 rownum 결정
    // 1 페이지 = 0 + 1: 1
    // 2 페이지 = 10 + 1: 11
    // 3 페이지 = 20 + 1: 21
    int start_num = begin_of_page + 1;

    // 종료 rownum
    // 1 페이지 = 0 + 10: 10
    // 2 페이지 = 10 + 10: 20
    // 3 페이지 = 20 + 10: 30
    int end_num = begin_of_page + Survey.RECORD_PER_PAGE;
    /*
     * 1 페이지: WHERE r >= 1 AND r <= 10 2 페이지: WHERE r >= 11 AND r <= 20 3 페이지: WHERE
     * r >= 21 AND r <= 30
     */

    // System.out.println("begin_of_page: " + begin_of_page);
    // System.out.println("WHERE r >= "+start_num+" AND r <= " + end_num);

    map.put("start_num", start_num);
    map.put("end_num", end_num);

    ArrayList<SSdMVO> list = this.survey_goodDAO.list_all_join(map);

    return list;
  }
  
  public Integer list_search_count(String word) {
    int cnt = this.survey_goodDAO.list_search_count(word);
//    System.out.println("Proc cnt : "+ cnt);
    return cnt;
  }
  
}
