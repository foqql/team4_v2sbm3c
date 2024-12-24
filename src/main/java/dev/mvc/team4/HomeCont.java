package dev.mvc.team4;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import dev.mvc.classify.ClassifyProc;
import dev.mvc.classify.ClassifyProcInter;
import dev.mvc.classify.ClassifyVOMenu;

@Controller
public class HomeCont {

  @Autowired
  @Qualifier("dev.mvc.classify.ClassifyProc")
  private ClassifyProcInter classifyProc;

  
  public HomeCont() {
    System.out.println("-> HomeCnont created.");
  }

  // http://localhost:9092
  // http://localhost:9092/index.do
  @GetMapping(value = { "/", "/index.do" })
  public String home(Model model) {

     ArrayList<ClassifyVOMenu> menu = this.classifyProc.menu();
     model.addAttribute("menu", menu);

    return "index"; // /templates/index.html

  }
}
