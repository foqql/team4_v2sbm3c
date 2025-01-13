package dev.mvc.newsrecom;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

//SELECT c.newsrecomno, c.rdate, c.newsno, n.title as n_title, c.memberno, m.id, m.mname
//FROM news n, newsrecom c, member m
//WHERE n.newsno = c.newsno and c.memberno = m.memberno
//ORDER BY newsrecomno DESC;

@Setter @Getter @ToString
public class NewsNewsrecomMemberVO {
  
    private int newsrecomno; // 기사 추천 번호
    private int newsno; // FK 뉴스 기사 번호
    private int memberno; // FK 회원 번호
    private String rdate; // 등록일
    private String n_title; // 제목
    private String id; // 아이디
    private String mname; // 이름
    private int classifyno;
   
}