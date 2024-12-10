package dev.mvc.genre;

import java.util.ArrayList;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class GenreVOMenu {
  /**
   * 나라 및 회사(대분류)
   */
  private String type;
  /**
   * 장르 이름(중분류) 정치, 스포츠 등등
   */
  ArrayList<GenreVO> list_name;

}
