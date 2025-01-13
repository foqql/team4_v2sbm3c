package dev.mvc.team4;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import dev.mvc.classify.ClassifyProcInter;
import dev.mvc.classify.ClassifyVOMenu;
import dev.mvc.genre.GenreProcInter;
import dev.mvc.genre.GenreVOMenu;

@Controller
public class HomeCont {

  @Autowired
  @Qualifier("dev.mvc.classify.ClassifyProc")
  private ClassifyProcInter classifyProc;

  @Autowired
  @Qualifier("dev.mvc.genre.GenreProc")
  private GenreProcInter genreProc;

  public HomeCont() {
    System.out.println("-> HomeCnont created.");
  }

  // http://localhost:9092
  // http://localhost:9092/index.do
  @GetMapping(value = { "/", "/index.do" })
  public String home(Model model) {

    ArrayList<ClassifyVOMenu> menu = this.classifyProc.menu(); // 중분류
    model.addAttribute("menu", menu);
    ArrayList<GenreVOMenu> menu1 = this.genreProc.menu(); // 대분류
    model.addAttribute("menu1", menu1);
    
//    for (ClassifyVOMenu i : menu) {
//      System.out.println(i.getBigcla());
//      System.out.println(i.getList_name().get(0).getClassify().toString());
//      System.out.println(i.toString());
//    }



    return "index"; // /templates/index.html

  }
}
