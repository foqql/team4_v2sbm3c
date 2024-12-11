package dev.mvc.summary;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("SummaryProc")
public class SummaryProc implements SummaryProcInter {
  
  @Autowired
  private SummaryDAOInter summaryDAO;
  
  @Override
  public int create(SummaryVO summaryVO) {
    int cnt = this.summaryDAO.select(summaryVO); 
    return cnt;
  }

  }

