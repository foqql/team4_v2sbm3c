package dev.mvc.country;

public interface CountryDAOInter {
    /**
     * 대분류 생성
     * @param countryVO
     * @return 처리된 레코드 수
     */
    public int create(CountryVO countryVO);
}
