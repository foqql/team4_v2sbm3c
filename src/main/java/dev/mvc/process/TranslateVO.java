package dev.mvc.process;
// TranslateVO.java
public class TranslateVO {
    private int translateNo; // 번역된 기사 번호
    private String title; // 번역된 제목
    private String content; // 번역된 내용
    private int newsCrawlingNo; // 뉴스 크롤링 번호
    private int contentsNo; // 기사 번호

    // Getters and Setters
    public int getTranslateNo() { return translateNo; }
    public void setTranslateNo(int translateNo) { this.translateNo = translateNo; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public int getNewsCrawlingNo() { return newsCrawlingNo; }
    public void setNewsCrawlingNo(int newsCrawlingNo) { this.newsCrawlingNo = newsCrawlingNo; }

    public int getContentsNo() { return contentsNo; }
    public void setContentsNo(int contentsNo) { this.contentsNo = contentsNo; }
}
