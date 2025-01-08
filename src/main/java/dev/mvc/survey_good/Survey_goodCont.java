package dev.mvc.survey_good;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import dev.mvc.classify.ClassifyProcInter;
import dev.mvc.classify.ClassifyVO;
import dev.mvc.classify.ClassifyVOMenu;
import dev.mvc.genre.GenreProcInter;
import dev.mvc.genre.GenreVOMenu;
import dev.mvc.member.MemberProcInter;
import jakarta.servlet.http.HttpSession;

@RequestMapping(value = "/survey_good")
@Controller
public class Survey_goodCont {
  @Autowired
  @Qualifier("dev.mvc.member.MemberProc") // @Service("dev.mvc.member.MemberProc")
  private MemberProcInter memberProc;

  @Autowired
  @Qualifier("dev.mvc.classify.ClassifyProc") // @Component("dev.mvc.classify.ClassifyProc")
  private ClassifyProcInter classifyProc;

  @Autowired
  @Qualifier("dev.mvc.survey_good.Survey_goodProc") // @Component("dev.mvc.survey_good.Survey_goodProc")
  private Survey_goodProcInter survey_goodProc;
  @Autowired
  @Qualifier("dev.mvc.genre.GenreProc") // @Component("dev.mvc.survey_good.Survey_goodProc")
  private GenreProcInter genreProc;

  public Survey_goodCont() {
    System.out.println("-> Survey_goodCont created.");
  }

}