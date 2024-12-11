package dev.mvc.genre;

import java.util.ArrayList;

public interface GenreDAOInter {
    /**
     * 대분류 생성
     * @param genreVO
     * @return 처리된 레코드 수
     */
    public int create(GenreVO genreVO);
    
    /**
     * 전체 대분류 조회
     * @return
     */
    public ArrayList<GenreVO> list_all();
    
    public ArrayList<GenreVO> list_all_categrp_y();
    
    /**
     * 
     * @param type
     * @return
     */
    public ArrayList<GenreVO> list_all_cate_y(String type);
    
    /**
     * 화면 상단 메뉴
     * 
     * @return
     */
    public ArrayList<GenreVOMenu> menu();
    
    /**
     * 분류
     * 
     * @return
     */
    public ArrayList<String> typeset();
    
    public int delete(int genreno);
    
    /**
     * 조회
     * 
     * @param genreno
     * @return
     */
    public GenreVO read(Integer genreno);
    
    /**
     * 수정
     * 
     * @param genreVO
     * @return
     */
    public int update(GenreVO genreVO);
    
}
