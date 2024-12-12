package dev.mvc.classify;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;


@Component("ClassifyProc")
public class ClassifyProc implements ClassifyProcInter {
  
  @Autowired
  private ClassifyDAOInter classifyDAO;
  
  @Override
  public int create(ClassifyVO classifyVO) {
    int cnt = this.classifyDAO.create(classifyVO); 
    return cnt;
  }

  @Override
  public ArrayList<ClassifyVO> list_all() {
    ArrayList<ClassifyVO> list = this.classifyDAO.list_all();
    return list;
  }
  
  @Override
  public ArrayList<ClassifyVOMenu> menu() {
    ArrayList<ClassifyVOMenu> menu = new ArrayList<ClassifyVOMenu>();

    ArrayList<ClassifyVO> type = this.classifyDAO.list_all_categrp_y(); // 대분류 목록 추출
    for (ClassifyVO classifyVO : type) {
//      System.out.println(classifyVO.getType());
      ClassifyVOMenu classifyVOMenu = new ClassifyVOMenu();
      classifyVOMenu.setType(classifyVO.getClassify()); // 대분류명 저장

      // 카테고리 그룹(대분류)에 해당하는 카테고리 목록(중분류) 로딩
      ArrayList<ClassifyVO> list_name = this.classifyDAO.list_all_cate_y(classifyVO.getClassify());
      classifyVOMenu.setList_name(list_name);

      menu.add(classifyVOMenu);
    }
    return menu;
  }
  
  
  @Override
  public ArrayList<String> typeset() {
    ArrayList<String> list = this.classifyDAO.typeset();
    return list;
  }

  @Override
  public ArrayList<ClassifyVO> list_all_categrp_y() {
    ArrayList<ClassifyVO> list = this.classifyDAO.list_all_categrp_y();
    return list;
  }

  @Override
  public ArrayList<ClassifyVO> list_all_cate_y(String type) {
    ArrayList<ClassifyVO> list = this.classifyDAO.list_all_cate_y(type);
    return list;
  }

  @Override
  public int delete(int classifyno) {
//    Integer[] supplementsno = this.classifyDAO.select_supplementsno(classifyno);
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
    int cnt = this.classifyDAO.delete(classifyno);
    return cnt;
  }

  @Override
  public ClassifyVO read(Integer classifyno) {
    ClassifyVO classifyVO = this.classifyDAO.read(classifyno);
    return classifyVO;
  }

  @Override
  public int update(ClassifyVO classifyVO) {
    int cnt = this.classifyDAO.update(classifyVO);
    return cnt;
  }

  }

