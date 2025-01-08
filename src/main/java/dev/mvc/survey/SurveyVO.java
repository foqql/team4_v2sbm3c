package dev.mvc.survey;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class SurveyVO {

  private int surveyno; // pk
  private String topic;
  private String startdate;
  private String enddate;
  private String poster;
  private String postersaved;
  private String posterthumb;
  private Long postersize;
  private int cnt;
  private String continueyn;
  private int recom;

  /**
   * item
   */
  private int surveyitemno; // pk
  private int itemseq = 1;
  private String item;
  private int itemcnt;
  private Integer pick; // 'Intger' -> 'Integer'로 수정

  /**
   * member
   */
  private int surveymemberno; // pk
  private String rdate;
  private int memberno; // fk키

  public SurveyVO() {
  }
}
