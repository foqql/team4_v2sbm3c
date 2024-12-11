package dev.mvc.process;

import lombok.Getter;
import lombok.Setter;

@Setter @Getter
public class SummaryVO {
  
    private int summaryno; // 요약된 기사 번호
    
    private String content; // 요약된 내용
    
    private int newno; // 기사 번호
    
    private int translateno; // 번역된 기사 번호


}
