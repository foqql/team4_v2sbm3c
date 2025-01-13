package dev.mvc.newsscore;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter @Getter @ToString
public class NewsscoreVO {
  
    private int newsscoreno; // 기사 평점 번호
    private int memberno; // FK 회원 번호
    private int newsno; // FK 뉴스 기사 번호
    private int jumsu; // 평점
    private String title; // 등록일
    private String rdate; // 등록일

   
}