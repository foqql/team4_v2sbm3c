package dev.mvc.genre;

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
}
