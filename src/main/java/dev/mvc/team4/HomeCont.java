package dev.mvc.team4;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeCont {

  public HomeCont() {
    System.out.println("-> HomeCnont created.");
  }

  // http://localhost:9092
  // http://localhost:9092/index.do
  @GetMapping(value = { "/", "/index.do" })
  public String home(Model model) {
    return "index"; // /templates/index.html

  }
}
