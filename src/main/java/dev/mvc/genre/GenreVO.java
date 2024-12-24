package dev.mvc.genre;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class GenreVO {

    private int genreno; // 장르 번호
//
//    @NotEmpty(message = "장르는 필수 항목입니다.")
//    @Size(max = 30, message = "장르는 30자를 넘을 수 없습니다.")
    private String genre; // 장르
    private String rdate; // 장르
    
    private int cnt;  //관련 자료수
    private int seqno; //출력 순서
    @NotEmpty(message = "출력 모드는 필수 항목입니다.")
    @Pattern(regexp = "^[YN]$", message = "Y 또는 N만 입력 가능합니다.")
    private String visible; //출력 모드
    
}
