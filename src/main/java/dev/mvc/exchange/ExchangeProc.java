package dev.mvc.exchange;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import dev.mvc.tool.Security;
import dev.mvc.tool.Tool;

@Component("dev.mvc.exchange.ExchangeProc")
public class ExchangeProc implements ExchangeProcInter {
  @Autowired
  Security security;
  
  @Autowired // ExchangeDAOInter interface를 구현한 클래스의 객체를 만들어 자동으로 할당해라.
  private ExchangeDAOInter exchangeDAO;


  /**
   * 조회
   */

  @Override
  public ExchangeVO reading(int classifyno) {
    ExchangeVO exchangeVO = this.exchangeDAO.reading(classifyno);
    return exchangeVO;
  }
  @Override
  public int map(HashMap<String, Object> map) {
    int cnt = this.exchangeDAO.map(map);
    return cnt;
  }





  
  
}