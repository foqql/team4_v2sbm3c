package dev.mvc.news;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter @Getter @ToString
public class NewscrawlingVO {
  
    private int newscrawlingno; // 크롤링 기사 번호
    private String title; // 크롤링 제목
    private String content; // 크롤링 내용
    private String rdate; // 크롤링 시간
    private String source; // 크롤링 방송사
    private String url; // 크롤링 주소
    private String genre; // 크롤링 분류
    

   
}
