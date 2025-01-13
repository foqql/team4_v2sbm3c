package dev.mvc.newsscore;

import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("dev.mvc.newsscore.NewsscoreProc")
public class NewsscoreProc implements NewsscoreProcInter {
  @Autowired
  NewsscoreDAOInter newsscoreDAO;
  


  @Override
  public ArrayList<NewsscoreVO> list_all() {
    ArrayList<NewsscoreVO> list = this.newsscoreDAO.list_all();    
    return list;
  }
  
  @Override
  public int delete(int newsscoreno) {
    int cnt = this.newsscoreDAO.delete(newsscoreno);
    return cnt;
  }
  
}