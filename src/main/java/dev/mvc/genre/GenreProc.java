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
    ArrayList<GenreVO> list = this.genreDAO.list_all();
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

  @Override
  public ArrayList<GenreVO> genre() {
    ArrayList<GenreVO> list = this.genreDAO.genre();
    return list;
  }

  @Override
  public ArrayList<GenreVOMenu> menu() {
    ArrayList<GenreVOMenu> menu = new ArrayList<GenreVOMenu>();

    // 대분류 목록을 추출
    ArrayList<GenreVO> type = this.genreDAO.genre();

    for (GenreVO genreVO : type) {
      GenreVOMenu genreVOMenu = new GenreVOMenu();
      genreVOMenu.setGenre(genreVO.getGenre());

      // 대분류에 해당하는 세부 항목 목록을 추출
//        ArrayList<GenreVO> list_name = this.genreDAO.list_all_genre_y(genreVO.getGenre());
//        genreVOMenu.setList_name(list_name);

      // 메뉴 리스트에 추가
      menu.add(genreVOMenu);
    }
//    System.out.println(menu);
    return menu;
  }

  @Override
  public int update_visible_y(int genreno) {
    int cnt = this.genreDAO.update_visible_y(genreno);
    return cnt;
  }

  @Override
  public int update_visible_n(int genreno) {
    int cnt = this.genreDAO.update_visible_n(genreno);
    return cnt;
  }

  @Override
  public int update_seqno_forward(int genreno) {
    int cnt = this.genreDAO.update_seqno_forward(genreno);
    return cnt;
  }

  @Override
  public int update_seqno_backward(int genreno) {
    int cnt = this.genreDAO.update_seqno_backward(genreno);
    return cnt;
  }

}
