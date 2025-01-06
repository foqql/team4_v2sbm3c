package dev.mvc.exchange;
  
import org.springframework.web.multipart.MultipartFile;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
  
@Getter @Setter @ToString
public class ExchangeVO {

  /*
CREATE TABLE exchange (
  exchangeno  NUMBER(10)   NOT NULL PRIMARY KEY,
    classifyno NUMBER(10) NOT NULL,
  name  VARCHAR2(50)  NOT NULL,
  price NUMBER(10,2)  NOT NULL,
  krw NUMBER(10,2)  NOT NULL,
  value NUMBER(10,2)  NOT NULL,
  yesterday NUMBER(10,2)  NOT NULL,
  recent  DATE  NOT NULL,
  map VARCHAR2(1000)  NULL,
  file1 VARCHAR(100)  NULL,
  file1saved  VARCHAR(100)  NULL,
  thumb1  VARCHAR(100)  NULL,
  size1 NUMBER(10)  NULL
);
  */

  /** 컨텐츠 번호 */
  private int exchangeno;
  /** 장르 번호 */
  private int classifyno;
  /** 크롤링한 환율 번호 */
  private int crawlingno;
  /** 이름 */
  private String name = "";
  /** 가격 */
  private double price;
  /** 원당 가격 */
  private double krw;
  /** 변동 값 */
  private double value;
  /** 종가 변동 값*/
  private double yesterday;
  /** 최근 업데이트 날짜 */
  private String recent = "";
  /** 지도 */
  private String map = "";
  
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
  
/**
 * 화폐 코드 관련
 */
  /** 이름 */
  private String country = "";
  /** 이름 */
  private String currencyname = "";
  /** 이름 */
  private String currencycode = "";
  
}
  
  
