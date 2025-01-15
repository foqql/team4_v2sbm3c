package dev.mvc.gallery;

import org.springframework.web.multipart.MultipartFile;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
public class GalleryVO {
  
  /** 컨텐츠 번호 */
  private int galleryno;
  
  /** 관리자 권한의 회원 번호 */
  private int memberno;
  
  /** 장르 번호 */
  private int classifyno;

  /** 제목 */
  private String title = "";
  
  /** 등록 날짜 */
  private String rdate = "";
  
  /** 추천 */
  private int recom;
  
  

  
  
  // 파일 업로드 관련
  // -----------------------------------------------------------------------------------
  /**
  이미지 파일
  <input type='file' class="form-control" name='file1MF' id='file1MF' 
             value='' placeholder="파일 선택">
  */
  private MultipartFile file1MF = null;
  /** 메인 이미지 크기 단위, 파일 크기 */
  private String size1_label = "";
  /** 메인 이미지 */
  private String file1 = "";
  /** 실제 저장된 메인 이미지 */
  private String file1saved = "";
  /** 메인 이미지 preview */
  private String thumb1 = "";
  /** 메인 이미지 크기 */
  private long size1 = 0;
  
}