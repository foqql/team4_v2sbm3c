package dev.mvc.areagood;
import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("dev.mvc.areagood.AreagoodProc")
public class AreagoodProc implements AreagoodProcInter {

@Autowired // ContentsDAOInter interface를 구현한 클래스의 객체를 만들어 자동으로 할당해라.
private AreagoodDAOInter areagoodDAO;
  
  @Override
  public int create(AreagoodVO areagoodVO) {
    int cnt = this.areagoodDAO.create(areagoodVO);
    return cnt;
  }

  @Override
  public ArrayList<AreagoodVO> list_all() {
    ArrayList<AreagoodVO> list = this.areagoodDAO.list_all();
    return list;
  }
  
  @Override
  public int delete(int areagoodno) {
    int cnt = this.areagoodDAO.delete(areagoodno);
    return cnt;
  
  }
  
  @Override
  public int hartCnt(HashMap<String, Object> map) {
    int cnt = this.areagoodDAO.hartCnt(map);
    return cnt;
  
  }

  
  @Override
  public AreagoodVO read(int areagoodno) {
    AreagoodVO areagoodVO = this.areagoodDAO.read(areagoodno);
    return areagoodVO;
  }

  @Override
  public AreagoodVO readByWeathernoMemberno(HashMap<String, Object> map) {
    AreagoodVO areagoodVO = this.areagoodDAO.readByWeathernoMemberno(map);
    return areagoodVO;
  }
  
  @Override
  public ArrayList<WeatherAreagoodMemberVO> list_all_join() {
    ArrayList<WeatherAreagoodMemberVO> list = this.areagoodDAO.list_all_join();
    return list;
  }
}

