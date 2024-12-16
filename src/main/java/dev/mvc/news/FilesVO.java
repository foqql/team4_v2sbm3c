package dev.mvc.news;

import org.springframework.web.multipart.MultipartFile;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter @Getter @ToString
public class FilesVO {
  /*
  fileno           NUMBER(10)      NOT NULL    PRIMARY KEY, 
  file1             VARCHAR2(100) NULL,
  file1saved     VARCHAR2(100) NULL,
  thumb1        VARCHAR2(100) NULL,
  size1            NUMBER(10)      DEFAULT 0   NULL,
  url               VARCHAR2(200) NOT NULL
  */
    /** 파일 번호 */
    private int filesno;
    /** 뉴스 크롤링 번호 */
    private int newscrawlingno;
    /** 메인 이미지 */
    private String file1 = "";
    /** 실제 저장된 메인 이미지 */
    private String file1saved = "";
    /** 메인 이미지 preview */
    private String thumb1 = "";
    /** 메인 이미지 크기 */
    private long size1 = 0;
    /**
    이미지 파일
    <input type='file' class="form-control" name='file1MF' id='file1MF' 
               value='' placeholder="파일 선택">
    */
    private MultipartFile file1MF = null;
    /** 메인 이미지 크기 단위, 파일 크기 */
    private String size1_label = "";
    
}