package dev.mvc.genre;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;


@Component("GenreProc")
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
  public ArrayList<GenreVOMenu> menu() {
    ArrayList<GenreVOMenu> menu = new ArrayList<GenreVOMenu>();

    ArrayList<GenreVO> type = this.genreDAO.list_all_categrp_y(); // 대분류 목록 추출
    for (GenreVO genreVO : type) {
//      System.out.println(genreVO.getType());
      GenreVOMenu genreVOMenu = new GenreVOMenu();
      genreVOMenu.setType(genreVO.getGenre()); // 대분류명 저장

      // 카테고리 그룹(대분류)에 해당하는 카테고리 목록(중분류) 로딩
      ArrayList<GenreVO> list_name = this.genreDAO.list_all_cate_y(genreVO.getGenre());
      genreVOMenu.setList_name(list_name);

      menu.add(genreVOMenu);
    }
    return menu;
  }
  
  
  @Override
  public ArrayList<String> typeset() {
    ArrayList<String> list = this.genreDAO.typeset();
    return list;
  }

  @Override
  public ArrayList<GenreVO> list_all_categrp_y() {
    ArrayList<GenreVO> list = this.genreDAO.list_all_categrp_y();
    return list;
  }

  @Override
  public ArrayList<GenreVO> list_all_cate_y(String type) {
    ArrayList<GenreVO> list = this.genreDAO.list_all_cate_y(type);
    return list;
  }

  @Override
  public int delete(int genreno) {
//    Integer[] supplementsno = this.genreDAO.select_supplementsno(genreno);
//    for(int i : supplementsno) {
//    System.out.println("  ++++ " + i);
//    // -------------------------------------------------------------------
//    // 파일 삭제 시작
//    // -------------------------------------------------------------------
//    // 삭제할 파일 정보를 읽어옴.
//    SupplementsVO supplementsVO_read = this.supplementsDAO.read(i);
//
//    String file1saved = supplementsVO_read.getFile1saved();
//    String thumb1 = supplementsVO_read.getThumb1();
//
//    String uploadDir = Supplements.getUploadDir();
//    Tool.deleteFile(uploadDir, file1saved); // 실제 저장된 파일삭제
//    Tool.deleteFile(uploadDir, thumb1); // preview 이미지 삭제
//    // -------------------------------------------------------------------
//    // 파일 삭제 종료
//    // -------------------------------------------------------------------
//    }
    int cnt = this.genreDAO.delete(genreno);
    return cnt;
  }

  @Override
  public GenreVO read(Integer genreno) {
    GenreVO genreVO = this.genreDAO.read(genreno);
    return genreVO;
  }

  }

