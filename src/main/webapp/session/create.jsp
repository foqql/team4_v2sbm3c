<%@ page contentType="text/html; charset=UTF-8" %>

<%@ page import="java.util.Date" %>
 
<%
session.setAttribute("id", "user1@mail.com"); // 키, 값
session.setAttribute("passwd", "123");
session.setAttribute("date", new Date());  // class 객체도 저장 가능

response.sendRedirect("./session.jsp"); // 자동으로 주소 이동
%>
