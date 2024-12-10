package dev.mvc.translate;

public interface TranslateProcInter {

  /**
   * 번역 생성
   * @param translateVO
   * @return 처리된 레코드 수
   */
  public int create(TranslateVO translateVO);

}
