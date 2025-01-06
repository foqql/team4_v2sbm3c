<%@ page contentType="text/html; charset=UTF-8" %>
 
<%
Cookie cookie = new Cookie("windowOpen", "close"); // 이름, 값
cookie.setMaxAge(30); // 쿠키 수명 30초
response.addCookie(cookie);  // 접속자의 브러우저 영역에 데이터 저장
%>
 
<!DOCTYPE html> 
<html lang="ko"> 
<head> 
<meta charset="UTF-8"> 
<title></title> 
</head> 
<body>

<script type="text/javascript">
  window.close(); // 현재 윈도우 닫기
</script>
 
</body>
 
</html>