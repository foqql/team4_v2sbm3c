package dev.mvc.process;

import lombok.Getter;
import lombok.Setter;

@Setter @Getter
public class SummaryVO {
  
    private int summaryNo; // 요약된 기사 번호
    
    private String content; // 요약된 내용
    
    private int contentsNo; // 기사 번호
    
    private int translateNo; // 번역된 기사 번호


}
