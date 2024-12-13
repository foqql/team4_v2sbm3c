package dev.mvc.classify;

import java.util.ArrayList;
import java.util.Map;

public interface ClassifyDAOInter {
  /**
   * 
   * @param classifyVO
   * @return
   */
  public int create(ClassifyVO classifyVO);

  public ArrayList<ClassifyVO> list_all();

  public ClassifyVO read(Integer classifyno);

  public int update(ClassifyVO classifyVO);

  public int delete(int classifyno);

  public int update_seqno_forward(int classifyno);

  public int update_seqno_backward(int classifyno);

  public int update_visible_y(int classifyno);

  public int update_visible_n(int classifyno);

  public ArrayList<ClassifyVO> list_all_categrp_y();

  /**
   * 
   * @param classify
   * @return
   */
  public ArrayList<ClassifyVO> list_all_cate_y(String classify);

  /**
   * 장르 목록
   * 
   * @return
   */
  public ArrayList<String> classifyset();

  public ArrayList<ClassifyVO> list_search(String word);

  public Integer list_search_count(String word);

  /**
   * 검색 + 페이징 <select id="list_search_paging" resultClassify="dev.mvc.classify.ClassifyVO"
   * parameterClassify="Map">
   * 
   * @param map
   * @return
   */
  public ArrayList<ClassifyVO> list_search_paging(Map<String, Object> map);
  
  public int update_classify_cnt();
  public int update_classify_genre_cnt();
  
  public Integer[] select_newsno(int classifyno);

}