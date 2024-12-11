package dev.mvc.summary;

public interface SummaryProcInter {

  /**
   * 요약 처리
   * @param summaryVO
   * @return 처리된 레코드 수
   */
  public int create(SummaryVO summaryVO);

}
