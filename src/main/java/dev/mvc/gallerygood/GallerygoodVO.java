package dev.mvc.gallerygood;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class GallerygoodVO {

    /** 사진 추천 번호 */
    private int areagoodno;

    /** 등록일 */
    private String rdate;

    /** 갤러리 번호 */
    private int galleryno;
    
    /** 회원 번호 */
    private int memberno;
    
}