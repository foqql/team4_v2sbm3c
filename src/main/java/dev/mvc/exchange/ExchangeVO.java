package dev.mvc.exchange;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ExchangeVO {
    private int exchangeno; // 환율 번호

    @NotEmpty(message = "제목은 필수 항목입니다.")
    @Size(max = 100, message = "제목은 100자를 넘을 수 없습니다.")
    private String title; // 제목

    @NotEmpty(message = "내용은 필수 항목입니다.")
    private String contents; // 내용 (CLOB type handled as String)

    private int cnt; // 조회수

    @NotEmpty(message = "검색어는 필수 항목입니다.")
    @Size(max = 200, message = "검색어는 200자를 넘을 수 없습니다.")
    private String word; // 검색어

    @NotNull(message = "등록일은 필수 항목입니다.")
    private LocalDate rdate; // 등록일

    @Size(max = 1000, message = "지도 URL은 1000자를 넘을 수 없습니다.")
    private String map; // 지도

    @Size(max = 1000, message = "유튜브 URL은 1000자를 넘을 수 없습니다.")
    private String youtube; // 유튜브

    @Size(max = 1000, message = "암호는 1000자를 넘을 수 없습니다.")
    private String passwd; // 암호
}
