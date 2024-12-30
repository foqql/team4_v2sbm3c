package dev.mvc.chat;

import java.util.Date;

public class ChatVO {
    private int chatno;
    private String message;
    private int recom;
    private Date rdate;
    private int memberno;
    private String id; // 아이디 추가
    private int grade; // 회원 등급 추가
    private String imageUrl; // 이미지 URL 추가

    // Getter & Setter
    public int getChatno() { return chatno; }
    public void setChatno(int chatno) { this.chatno = chatno; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public int getRecom() { return recom; }
    public void setRecom(int recom) { this.recom = recom; }

    public Date getRdate() { return rdate; }
    public void setRdate(Date rdate) { this.rdate = rdate; }

    public int getMemberno() { return memberno; }
    public void setMemberno(int memberno) { this.memberno = memberno; }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public int getGrade() { return grade; }
    public void setGrade(int grade) { this.grade = grade; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
}
