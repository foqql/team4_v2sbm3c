package dev.mvc.genre;

import java.util.ArrayList;

public interface GenreDAOInter {
  public int create(GenreVO genreVO);

  public ArrayList<GenreVO> list_all();

  public GenreVO read(Integer genreno);
  
  public ArrayList<GenreVO> genre();
  
  public int update(GenreVO genreVO);
  
  public int delete(int genreno);

  public int update_visible_y(int genreyno);
  
  public int update_visible_n(int genreyno);
  
  public int update_seqno_forward(int genreyno);
  
  public int update_seqno_backward(int genreyno);
}