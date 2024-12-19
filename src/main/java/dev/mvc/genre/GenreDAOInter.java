package dev.mvc.genre;

import java.util.ArrayList;

public interface GenreDAOInter {
  public int create(GenreVO genreVO);

  public ArrayList<GenreVO> list_all();

  public GenreVO read(Integer genreno);
  
  public int update(GenreVO genreVO);
  
  public int delete(int genreno);

}