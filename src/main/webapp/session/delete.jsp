<%@ page contentType="text/html; charset=UTF-8" %>
 
<%
// session.removeAttribute("id"); // id session 변수만 삭제
session.invalidate();  // 모든 session 변수 삭제, 로그아웃시 많이 이용

response.sendRedirect("./session.jsp");
%>
