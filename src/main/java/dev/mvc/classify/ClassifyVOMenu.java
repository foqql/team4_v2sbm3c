package dev.mvc.classify;

import java.util.ArrayList;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ClassifyVOMenu {
  /**
   *  종류(대분류)
   */
  private String type;
  /**
   *  이름(중분류)
   */
  ArrayList<ClassifyVO> list_name;

}
