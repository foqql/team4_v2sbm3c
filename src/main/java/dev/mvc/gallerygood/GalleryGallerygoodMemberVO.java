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

    /** 사진번호 */
    private int galleryno;
    
    /** 회원 번호 */
    private int memberno;
    
    /** 사진 */
    private String thumb1 = "";
    
    /** 아이디(이메일) */
    private String id = "";
    
    /** 회원 성명 */
    private String mname = "";
}