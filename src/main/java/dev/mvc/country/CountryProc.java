package dev.mvc.country;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("CountryProc")
public class CountryProc implements CountryProcInter {
  
  @Autowired
  private CountryDAOInter countryDAO;
  
  @Override
  public int create(CountryVO countryVO) {
    int cnt = this.countryDAO.create(countryVO); 
    return cnt;
  }

  }

