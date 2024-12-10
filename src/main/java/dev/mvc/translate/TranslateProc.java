package dev.mvc.translate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("TranslateProc")
public class TranslateProc implements TranslateProcInter {
  
  @Autowired
  private TranslateDAOInter translateDAO;
  
  @Override
  public int create(TranslateVO translateVO) {
    int cnt = this.translateDAO.create(translateVO); 
    return cnt;
  }

  }

