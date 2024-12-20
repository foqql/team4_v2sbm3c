package dev.mvc.genre;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

// 알고리즘 구현
@Service("dev.mvc.genre.GenreProc")
public class GenreProc implements GenreProcInter {
  @Autowired
  private GenreDAOInter genreDAO;
  
  @Override
  public int create(GenreVO genreVO) {
    int cnt = this.genreDAO.create(genreVO);
    return cnt;
  }

  @Override
  public ArrayList<GenreVO> list_all() {
    ArrayList<GenreVO>list = this.genreDAO.list_all() ;
    return list;
  }

  @Override
  public GenreVO read(Integer genreno) {    
    GenreVO genreVO = this.genreDAO.read(genreno);
    return genreVO;
  }

  @Override
  public int update(GenreVO genreVO) {
    int cnt = this.genreDAO.update(genreVO);
    return cnt;
  }

  @Override
  public int delete(int genreno) {
    int cnt = this.genreDAO.delete(genreno);
    return cnt;
  }

}
