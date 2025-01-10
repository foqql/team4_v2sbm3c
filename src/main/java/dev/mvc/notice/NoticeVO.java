package dev.mvc.notice;

import java.sql.Timestamp;

public class NoticeVO {
    private int notino;
    private String title;
    private String content;
    private Timestamp nodate;  // Timestamp 타입으로 변경

    // getters and setters
    public int getNotino() {
        return notino;
    }

    public void setNotino(int notino) {
        this.notino = notino;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Timestamp getNodate() {
        return nodate;
    }

    public void setNodate(Timestamp nodate) {
        this.nodate = nodate;
    }

    @Override
    public String toString() {
        return "NoticeVO [notino=" + notino + ", title=" + title + ", content=" + content + ", nodate=" + nodate + "]";
    }
}
