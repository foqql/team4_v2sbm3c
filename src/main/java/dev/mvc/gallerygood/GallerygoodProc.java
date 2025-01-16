package dev.mvc.gallerygood;
import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("dev.mvc.gallerygood.GallerygoodProc")
public class GallerygoodProc implements GallerygoodProcInter {

@Autowired // ContentsDAOInter interface를 구현한 클래스의 객체를 만들어 자동으로 할당해라.
private GallerygoodDAOInter gallerygoodDAO;
  
  @Override
  public int create(GallerygoodVO gallerygoodVO) {
    int cnt = this.gallerygoodDAO.create(gallerygoodVO);
    return cnt;
  }

  @Override
  public ArrayList<GallerygoodVO> list_all() {
    ArrayList<GallerygoodVO> list = this.gallerygoodDAO.list_all();
    return list;
  }
  
  @Override
  public int delete(int gallerygoodno) {
    int cnt = this.gallerygoodDAO.delete(gallerygoodno);
    return cnt;
  
  }
  
  @Override
  public int hartCnt(HashMap<String, Object> map) {
    int cnt = this.gallerygoodDAO.hartCnt(map);
    return cnt;
  
  }

  
  @Override
  public GallerygoodVO read(int gallerygoodno) {
    GallerygoodVO gallerygoodVO = this.gallerygoodDAO.read(gallerygoodno);
    return gallerygoodVO;
  }

  @Override
  public GallerygoodVO readByGallerynoMemberno(HashMap<String, Object> map) {
    GallerygoodVO gallerygoodVO = this.gallerygoodDAO.readByGallerynoMemberno(map);
    return gallerygoodVO;
  }
  
  @Override
  public ArrayList<GalleryGallerygoodMemberVO> list_all_join() {
    ArrayList<GalleryGallerygoodMemberVO> list = this.gallerygoodDAO.list_all_join();
    return list;
  }

}

