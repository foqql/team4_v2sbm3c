package dev.mvc.news;

import org.springframework.web.multipart.MultipartFile;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter @Getter @ToString
public class NewsVO {
  /*
  newsno  NUMBER(10)                  NOT NULL    PRIMARY KEY,
  classifyno     NUMBER(10)                  NOT NULL, --FK
  fileno    NUMBER(10)                  NOT NULL, --FK
  title     VARCHAR2(100)             NOT NULL,
  content     CLOB                      NOT NULL,
  recom     NUMBER(7)     DEFAULT 0   NOT NULL,
  cnt         NUMBER(7)     DEFAULT 0   NOT NULL,
  replycnt  NUMBER(7)     DEFAULT 0   NOT NULL,
  passwd      VARCHAR2(100)             NOT NULL,
  word      VARCHAR2(200)             NOT NULL,
  rdate     DATE                      NOT NULL,
  map         VARCHAR2(1000)              NULL,
  youtube     VARCHAR2(1000)              NULL,
  mp4         VARCHAR2(100)             NULL
  */

  /** 컨텐츠 번호 */
  private int newsno;
  /** 관리자 권한의 회원 번호 */
  private int memberno;
  /** 장르 번호 */
  private int classifyno;
  /** 요약된 기사 번호 */
  private int summaryno;
  /** 제목 */
  private String title = "";
  /** 내용 */
  private String content = "";
  /** 추천수 */
  private int recom;
  /** 조회수 */
  private int cnt = 0;
  /** 패스워드 */
  private String passwd = "";
  /** 검색어 */
  private String word = "";
  /** 등록 날짜 */
  private String rdate = "";
  /** 지도 */
  private String map = "";
  /** Youtube */
  private String youtube = "";
  /** mp3 */
  private String mp3 = "";
  
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