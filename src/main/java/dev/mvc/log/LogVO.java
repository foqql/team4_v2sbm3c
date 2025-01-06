package dev.mvc.log;

import java.util.Date;

public class LogVO {
    private int logno;
    private String ip;
    private Date logdate;
    private int memberno;
    private String mname;

    // 기본 생성자 추가
    public LogVO() {}

    // 기존 생성자
    public LogVO(int logno, String ip, Date logdate, int memberno, String mname) {
        this.logno = logno;
        this.ip = ip;
        this.logdate = logdate;
        this.memberno = memberno;
        this.mname = mname;
    }

    // Getter와 Setter 메서드들
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

    public String getMname() {
        return mname;
    }

    public void setMname(String mname) {
        this.mname = mname;
    }

    @Override
    public String toString() {
        return "LogVO{" +
                "logno=" + logno +
                ", ip='" + ip + '\'' +
                ", logdate=" + logdate +
                ", memberno=" + memberno +
                ", mname='" + mname + '\'' +
                '}';
    }
}
