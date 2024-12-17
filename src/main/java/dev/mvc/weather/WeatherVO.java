  package dev.mvc.weather;
  
  import java.util.Date;
  import lombok.Getter;
  import lombok.Setter;
  import lombok.ToString;
  
  @Getter @Setter @ToString
  public class WeatherVO {
  
      private int weather;        // 날씨
      
      private String title;       // 제목
      
      private String content;     // 내용
      
      private int cnt;            // 조회수
      
      private String word;        // 검색어
      
      private Date rdate;         // 날짜
      
      private String map;         // 지도
      
      private String youtube;     // 유튜브
      
      private String passwd;      // 암호
      
  }
