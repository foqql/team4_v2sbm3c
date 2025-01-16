package dev.mvc.gallerygood;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class GalleryGallerygoodMemberVO {

    /** 사진 추천 번호 */
    private int gallerygoodno;

    /** 등록일 */
    private String r_rdate;

    /** 사진 번호 */
    private int galleryrno;
    
    /** 회원 번호 */
    private int memberno;
    
    /** 날씨 */
    private String title = "";
    
    /** 아이디(이메일) */
    private String id = "";
    
    /** 회원 성명 */
    private String mname = "";
}