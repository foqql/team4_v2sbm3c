package dev.mvc.country;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

// countryVO.java
@Getter @Setter @ToString
public class genreVO {
    private int genreno; // 장르번호
    private String genre;  //장르 이름
    private int cnt;  //관련 자료수
    private int seqno; //출력 순서
    private int visible; //출력 모드
    private String rdate; //등록일
    private int countryno; //국가별 방송사 번호(FK)

 }
