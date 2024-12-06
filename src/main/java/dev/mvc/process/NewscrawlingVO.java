package dev.mvc.process;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter @Getter @ToString
public class NewscrawlingVO {
  /*
  newscrawlingno   NUMBER(10)      NOT NULL    PRIMARY KEY,
  fileno                  NUMBER(10)      NOT NULL,   --FK
  title                     VARCHAR2(100) NOT NULL,
  content               CLOB                  NOT NULL,
  rdate                   DATE                  NOT NULL,
  source                 VARCHAR2(30)   NOT NULL,
  */

    /** 뉴스 크롤링 번호 */
    private int newscrawlingno;
    /** 파일 번호 - FK */
    private int fileno;
    /** 제목 */
    private String title;
    /** 내용 */
    private String content = "";
    /** 등록일 */
    private String rdate = "";
    /** 해당 기사 방송사 */
    private String source = "";
}