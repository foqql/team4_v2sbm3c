<%@ page contentType="text/html; charset=UTF-8" %>
 
<!DOCTYPE html> 
<html lang="ko"> 
<head> 
<meta charset="UTF-8"> 
<title></title> 
 
<link href="/css/style.css" rel="Stylesheet" type="text/css">
 
</head> 
<body>
 <DIV class='title_line'>쿠키 삭제</DIV>
<%
Cookie ck_email = new Cookie("email", "");
ck_email.setMaxAge(0);   // 수명을 0초로 지정
response.addCookie(ck_email); // 쿠키 전송 
%>
<br><br>
【<A href='./write.jsp'>쿠키 기록</A> 】
【<A href='./read.jsp'>쿠키 읽기</A> 】
【<A href='./delete.jsp'>쿠키 삭제</A> 】  
  
</body>
 
</html>
 
