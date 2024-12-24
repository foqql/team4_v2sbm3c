package dev.mvc.translate;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter @Getter @ToString
public class TranslateVO {
  
    private int translateno; // 요약된 기사 번호
    
    private String title; // 요약된 내용
    private String content; // 요약된 내용
    

   
}
