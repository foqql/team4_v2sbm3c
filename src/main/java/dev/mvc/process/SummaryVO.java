package dev.mvc.process;
// SummaryVO.java
public class SummaryVO {
    private int summaryNo; // 요약된 기사 번호
    private String content; // 요약된 내용
    private int contentsNo; // 기사 번호
    private int translateNo; // 번역된 기사 번호

    // Getters and Setters
    public int getSummaryNo() { return summaryNo; }
    public void setSummaryNo(int summaryNo) { this.summaryNo = summaryNo; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public int getContentsNo() { return contentsNo; }
    public void setContentsNo(int contentsNo) { this.contentsNo = contentsNo; }

    public int getTranslateNo() { return translateNo; }
    public void setTranslateNo(int translateNo) { this.translateNo = translateNo; }
}
