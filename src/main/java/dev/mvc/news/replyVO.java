package dev.mvc.news;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

// countryVO.java
@Getter @Setter @ToString
public class replyVO {
    private int replyno; //댓글번호
    private String contents; //내용
    private int recom; //추천
    private String rdate; //등록일
    private int contentsno; //기사 번호
    private int memberno; //회원 번호

 }
