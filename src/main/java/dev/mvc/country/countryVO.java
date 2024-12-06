package dev.mvc.country;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

// countryVO.java
@Getter @Setter @ToString
public class countryVO {
    private int countryno; //국가별 방송사 번호
    private String company; //방송사
    private int seqno; //출력순서
    private int visible; //출력 모드
    private String rdate; //등록일

 }
