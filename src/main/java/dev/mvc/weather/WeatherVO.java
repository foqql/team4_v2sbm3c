package dev.mvc.weather;
  
import org.springframework.web.multipart.MultipartFile;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
  
@Getter @Setter @ToString
public class WeatherVO {

  
  /** 컨텐츠 번호 */
  private int weatherno;
  
  /** 관리자 권한의 회원 번호 */
  private int memberno;
  
  /** 장르 번호 */
  private int classifyno;
  
//  /** 대륙 */
//  private String continent = "";
//  
//  /** 국가 */
//  private String country = "";
//  
//  /** 도시 */
//  private String city = "";
  
  /** 날씨 */
  private String weather = "";
  
  /** 날짜 */
  private String rdate = "";
  
  /** 기온 */
  private String temp  = "";
  
  /** 체감온도 */
  private String windchill = "";
  
  /** 최저기온 */
  private String mintemp = "";
  
  /** 최고기온 */
  private String maxtemp = "";
  
  /** 습도 */
  private String humidity = "";
  
  /** 강수량 */
  private String rainfall = "";
  
  /** 강수확률 */
  private String pop = "";
  
  /** 풍속 */
  private String speed = "";
  
  /** 풍향 */
  private String direction = "";
  
  /** 최근 업데이트 시간 */
  private String udate = "";

  /** 지도 */
  private String map = "";
  
  /** Youtube */
  private String youtube = "";
  
  /** 패스워드 */
  private String passwd = "";
  
  
// 지역VO
 // -----------------------------------------------------------------------------------
 /**
  
  /** 지역 번호 */
  private int areano;
  
  /** 대륙 */
  private String continent = "";
  
  /** 국가 */
  private String country = "";
  
  /** 도시 */
  private String city;
  
  
  
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
