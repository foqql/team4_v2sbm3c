package dev.mvc.process;

import lombok.Getter;
import lombok.Setter;

@Setter @Getter
public class TranslateVO {
  
    private int translateNo; // 번역된 기사 번호
    
    private String title; // 번역된 제목
    
    private String content; // 번역된 내용
    
    private int newsCrawlingNo; // 뉴스 크롤링 번호
    
    private int contentsNo; // 기사 번호


}
