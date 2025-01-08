package dev.mvc.areagood;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class AreagoodVO {

    /** 지역 추천 번호 */
    private int areagoodno;

    /** 등록일 */
    private String rdate;

    /** 지역 번호 */
    private int weatherno;
    
    /** 회원 번호 */
    private int memberno;
    
}