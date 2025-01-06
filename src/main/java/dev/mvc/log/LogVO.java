package dev.mvc.log;

import java.util.Date;

public class LogVO {
    private int logno;
    private String ip;
    private Date logdate;
    private int memberno;

    // 기본 생성자
    public LogVO() {}

    // 필드를 초기화하는 생성자
    public LogVO(int logno, String ip, Date logdate, int memberno) {
        this.logno = logno;
        this.ip = ip;
        this.logdate = logdate;
        this.memberno = memberno;
    }

    // Getter 및 Setter 메서드
    public int getLogno() {
        return logno;
    }

    public void setLogno(int logno) {
        this.logno = logno;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Date getLogdate() {
        return logdate;
    }

    public void setLogdate(Date logdate) {
        this.logdate = logdate;
    }

    public int getMemberno() {
        return memberno;
    }

    public void setMemberno(int memberno) {
        this.memberno = memberno;
    }
}
