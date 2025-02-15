package dev.mvc.classify;

import java.util.ArrayList;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class ClassifyVOMenu {
  /**
   *  종류(대분류)
   */
  private String bigcla;
  /**
   *  이름(중분류)
   */
  ArrayList<ClassifyVO> list_name;

}
