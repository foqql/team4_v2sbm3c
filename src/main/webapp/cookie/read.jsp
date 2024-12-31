<%@ page contentType="text/html; charset=UTF-8" %>
 
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<title>http://localhost:9091/cookie/read.jsp</title>
<link href="/css/style.css" rel="Stylesheet" type="text/css">
</head>
<body>

<DIV class='title_line'>쿠키 읽기</DIV>

<%
Cookie[] cookies = request.getCookies(); // Cookie object array
Cookie cookie = null;
String email = "";

if (cookies != null) { 
  for (int index=0; index < cookies.length; index++) {
    cookie = cookies[index];  // 쿠키 목록에서 쿠키 추출
    if (cookie.getName().equals("email")) { // 이름 비교
      email = cookie.getValue();  // 쿠키 값
    }
  }
}
out.println("Email: " + email);
%>

<br><br>
【<A href='./write.jsp'>쿠키 기록</A> 】
【<A href='./read.jsp'>쿠키 읽기</A> 】
【<A href='./delete.jsp'>쿠키 삭제</A> 】  
 
</body>
</html>
