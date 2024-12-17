package dev.mvc.exchange;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

// 알고리즘 구현
@Service("ExchangeProc")
public class ExchangeProc implements ExchangeProcInter {
  @Autowired
  private ExchangeDAOInter exchangeDAO;

}
