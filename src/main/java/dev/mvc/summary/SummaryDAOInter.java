package dev.mvc.summary;

public interface SummaryDAOInter {
    /**
     * 요약 읽기
     * @param summaryVO
     * @return 처리된 레코드 수
     */
    public int select(SummaryVO summaryVO);
}
