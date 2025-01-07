package dev.mvc.newsrecom;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter @Getter @ToString
public class NewsrecomVO {
  
    private int newsrecomno; // 기사 추천 번호
    private int newsno; // FK 뉴스 기사 번호
    private int memberno; // FK 회원 번호
    private String rdate; // 등록일
    

   
}