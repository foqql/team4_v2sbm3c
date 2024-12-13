package dev.mvc.summary;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter @Getter @ToString
public class SummaryVO {
  
    private int summaryno; // 요약된 기사 번호
    
    private String content; // 요약된 내용
    
    private int translateno; // 번역된 기사 번호
    

   
}
