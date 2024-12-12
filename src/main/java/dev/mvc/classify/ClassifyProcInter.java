package dev.mvc.classify;

import java.util.ArrayList;

public interface ClassifyProcInter {
  /**
   * 대분류 생성
   * 
   * @param classifyVO
   * @return 처리된 레코드 수
   */
  public int create(ClassifyVO classifyVO);

  /**
   * 전체 대분류 조회
   * 
   * @return
   */
  public ArrayList<ClassifyVO> list_all();

  public ArrayList<ClassifyVO> list_all_categrp_y();

  /**
   * 
   * @param type
   * @return
   */
  public ArrayList<ClassifyVO> list_all_cate_y(String type);

  /**
   * 화면 상단 메뉴
   * 
   * @return
   */
  public ArrayList<ClassifyVOMenu> menu();

  /**
   * 분류
   * 
   * @return
   */
  public ArrayList<String> typeset();

  public int delete(int classifyno);
  
  /**
   * 조회
   * 
   * @param classifyno
   * @return
   */
  public ClassifyVO read(Integer classifyno);
  
  
  /**
   * 수정
   * 
   * @param classifyVO
   * @return
   */
  public int update(ClassifyVO classifyVO);
  
  
}
