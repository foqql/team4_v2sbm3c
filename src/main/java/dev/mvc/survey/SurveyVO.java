package dev.mvc.survey;

import org.springframework.web.multipart.MultipartFile;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class SurveyVO {

  private Integer  surveyno; // pk
  private String topic;
  private String startdate;
  private String enddate;
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

  /**
   * 파일 업로드
   */
  private MultipartFile file1MF = null;
  private String size_label = "";
  private String poster = "";  /** 메인 이미지 */
  private String postersaved= "";  /** 실제 저장된 메인 이미지 */
  private String posterthumb= "";  /** 메인 이미지 preview */
  private long postersize = 0;/** 메인 이미지 크기 */
  
  
  
  private String mname = "";
  public SurveyVO() {
  }
}
