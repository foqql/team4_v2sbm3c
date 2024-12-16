  package dev.mvc.weather;
  
  import java.util.List;
  
  public interface WeatherDAOInter {
  
      /**
       * 새로운 날씨 데이터를 삽입
       *
       * @param weatherVO 삽입할 WeatherVO 객체
       */
      void insertWeather(WeatherVO weatherVO);
  
      /**
       * 모든 날씨 데이터를 조회
       *
       * @return WeatherVO 객체의 리스트
       */
      List<WeatherVO> selectAllWeather();
  
      /**
       * 특정 ID를 기반으로 날씨 데이터를 조회
       *
       * @param weather 조회할 weather ID
       * @return 조회된 WeatherVO 객체
       */
      WeatherVO selectWeatherById(int weather);
  
      /**
       * 날씨 데이터를 업데이트
       *
       * @param weatherVO 업데이트할 WeatherVO 객체
       */
      void updateWeather(WeatherVO weatherVO);
  
      /**
       * 특정 ID를 기반으로 날씨 데이터를 삭제
       *
       * @param weather 삭제할 weather ID
       */
      void deleteWeather(int weather);
  }
