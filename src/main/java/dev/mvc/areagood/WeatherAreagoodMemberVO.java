package dev.mvc.areagood;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class WeatherAreagoodMemberVO {

    /** 지역 추천 번호 */
    private int areagoodno;

    /** 등록일 */
    private String r_rdate;

    /** 지역 번호 */
    private int weatherno;
    
    /** 회원 번호 */
    private int memberno;
    
    /** 날씨 */
    private String weather = "";
    
    /** 아이디(이메일) */
    private String id = "";
    
    /** 회원 성명 */
    private String mname = "";
}