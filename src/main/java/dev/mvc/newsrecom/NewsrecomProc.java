package dev.mvc.newsrecom;

import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("dev.mvc.newsrecom.NewsrecomProc")
public class NewsrecomProc implements NewsrecomProcInter {
  @Autowired
  NewsrecomDAOInter newsrecomDAO;
  
  @Override
  public int create(NewsrecomVO newsrecomVO) {
  int cnt = this.newsrecomDAO.create(newsrecomVO);
  return cnt;
  }

  @Override
  public ArrayList<NewsrecomVO> list_all() {
    ArrayList<NewsrecomVO> list = this.newsrecomDAO.list_all();    
    return list;
  }
  
  @Override
  public int delete(int newsrecomno) {
    int cnt = this.newsrecomDAO.delete(newsrecomno);
    return cnt;
  }
  
  @Override
  public int heartCnt(HashMap<String, Object> map) {
    int cnt = this.newsrecomDAO.heartCnt(map);
    return cnt;
  }
  
  @Override
  public NewsrecomVO read(int newsrecomno) {
    NewsrecomVO newsrecomVO = this.newsrecomDAO.read(newsrecomno);
    return newsrecomVO;
  }

  @Override
  public NewsrecomVO readByNewsnoMemberno(HashMap<String, Object> map) {
    NewsrecomVO newsrecomVO = this.newsrecomDAO.readByNewsnoMemberno(map);
    return newsrecomVO;
  }

  @Override
  public ArrayList<NewsNewsrecomMemberVO> list_all_join() {
    ArrayList<NewsNewsrecomMemberVO> list = this.newsrecomDAO.list_all_join();
    return list;
  }
}