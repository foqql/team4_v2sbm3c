package dev.mvc.news;

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
  private int contentsno;
  /** 파일 번호 */
  private int fileno;
  /** 장르 번호 */
  private int classifyno;
  /** 제목 */
  private String title = "";
  /** 내용 */
  private String content = "";
  /** 추천수 */
  private int recom;
  /** 조회수 */
  private int cnt = 0;
  /** 댓글수 */
  private int replycnt = 0;
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
  /** mp4 */
  private String mp4 = "";
}