package dev.mvc.weather;

import java.util.Map;
import org.springframework.web.multipart.MultipartFile;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class WeatherVO {

    /** 컨텐츠 번호 */
    private int weatherno;

    /** 관리자 권한의 회원 번호 */
    private int memberno;

    /** 장르 번호 */
    private int classifyno;

    /** 날씨 */
    private String weather = "";

    /** 날짜 */
    private String rdate = "";

    /** 기온 */
    private String temp = "";

    /** 체감온도 */
    private String windchill = "";

    /** 최저기온 */
    private String mintemp = "";

    /** 최고기온 */
    private String maxtemp = "";

    /** 습도 */
    private String humidity = "";

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
    
    /** 추천 */
    private int recom;

    // 지역VO
    
    /** 지역 번호 */
    private int areano;

    /** 대륙 */
    private String continent = "";

    /** 국가 */
    private String country = "";

    /** 도시 */
    private String city = "";
    

    // 대륙 코드로 대륙 이름 설정
    public void setContinentName(String continentCode) {
        if (CONTINENT_MAP.containsKey(continentCode)) {
            this.continent = CONTINENT_MAP.get(continentCode);
        }
    }

    // 대륙 코드와 국가 코드로 국가 이름 설정
    public void setCountryName(String continentCode, String countryCode) {
        if (COUNTRY_MAP.containsKey(continentCode) && COUNTRY_MAP.get(continentCode).containsKey(countryCode)) {
            this.country = COUNTRY_MAP.get(continentCode).get(countryCode);
        }
    }

    // 대륙 코드와 도시 코드로 도시 이름 설정
    public void setCityName(String continentCode, String cityCode) {
        if (CITY_MAP.containsKey(continentCode) && CITY_MAP.get(continentCode).containsKey(cityCode)) {
            this.city = CITY_MAP.get(continentCode).get(cityCode);
        }
    }

    // Getter and Setter
    public String getContinent() {
        return continent;
    }

    public void setContinent(String continent) {
        this.continent = continent;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
    
    // 매핑 데이터
    private static final Map<String, String> CONTINENT_MAP = Map.of(
        "1", "아시아",
        "2", "유럽",
        "3", "아프리카",
        "4", "북아메리카",
        "5", "남아메리카",
        "6", "오세아니아"
    );

    private static final Map<String, Map<String, String>> COUNTRY_MAP = Map.of(
        "4", Map.of(
            "1", "미국",
            "2", "캐나다"
        )
    );

    private static final Map<String, Map<String, String>> CITY_MAP = Map.of(
        "4", Map.of(
            "1", "뉴욕",
            "2", "댈러스",
            "3", "덴버",
            "4", "라스베이거스",
            "5", "로스앤젤레스",
            "6", "마이애미"
        )
    );



    // 파일 업로드 관련
    private MultipartFile file1MF = null;
    private String size1_label = "";
    private String file1 = "";
    private String file1saved = "";
    private String thumb1 = "";
    private long size1 = 0;

}