package dev.mvc.weather;
  
import org.springframework.web.multipart.MultipartFile;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
  
@Getter @Setter @ToString
public class WeatherVO {

  /*
CREATE TABLE weather (
  weatherno         NUMBER(10)          NOT NULL,
  title                 VARCHAR2(100)     NOT NULL,
  content             CLOB                      NOT NULL,
  cnt                 NUMBER(7)         NOT NULL,
  word                  VARCHAR2(200)     NOT NULL,
  rdate                 DATE                      NOT NULL,
  map                 VARCHAR2(1000)  NULL,
  youtube             VARCHAR2(1000)  NULL,
  passwd              VARCHAR2(100)     NULL
);
  */

  /** 컨텐츠 번호 */
  private int weatherno;
  /** 관리자 권한의 회원 번호 */
  private int memberno;
  /** 장르 번호 */
  private int classifyno;
  /** 크롤링한 날씨 번호 */
  private int wcrawlingno;
  /** 제목 */
  private String title = "";
  /** 내용 */
  private String content = "";
  /** 검색어 */
  private String word = "";
  /** 등록 날짜 */
  private String rdate = "";
  /** 지도 */
  private String map = "";
  /** Youtube */
  private String youtube = "";
  /** 암호 */
  private String passwd = "";
  
  // 파일 업로드 관련
  // -----------------------------------------------------------------------------------
  /**
  이미지 파일
  <input type='file' class="form-control" name='file1MF' id='file1MF' 
             value='' placeholder="파일 선택">
  */
  private MultipartFile file1MF = null;
  /** 메인 이미지 크기 단위, 파일 크기 */
  private String size1_label = "";
  /** 메인 이미지 */
  private String file1 = "";
  /** 실제 저장된 메인 이미지 */
  private String file1saved = "";
  /** 메인 이미지 preview */
  private String thumb1 = "";
  /** 메인 이미지 크기 */
  private long size1 = 0;

}