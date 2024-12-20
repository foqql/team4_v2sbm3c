package dev.mvc.classify;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

// ClassifyVO.java
@Getter @Setter @ToString
public class ClassifyVO {
    private int classifyno; // 장르번호
    private String classify;  //장르 
//    private String name;  //이름
    private int cnt;  //관련 자료수
    private int seqno; //출력 순서
    private int genreno;
    
    @NotEmpty(message = "출력 모드는 필수 항목입니다.")
    @Pattern(regexp = "^[YN]$", message = "Y 또는 N만 입력 가능합니다.")
    private String visible; //출력 모드
    private String rdate; //등록일
//    private int countryno; //국가별 방송사 번호(FK) 241210이후 제거

    private String bigcla;  //장르 
//    private GenreVO genreVO;
 }

