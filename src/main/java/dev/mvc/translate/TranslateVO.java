package dev.mvc.translate;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter @Getter @ToString
public class TranslateVO {
  
    private int translateno; // 번역된 기사 번호
    
    private String title; // 번역된 제목
    
    private String content; // 번역된 내용
    
    private int newscrawlingno; // 뉴스 크롤링 번호
    

}
