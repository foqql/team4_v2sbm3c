<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="java.util.Date" %>

<%!
public String checkNull(Object str) {
  if (str == null) {
    str = "";
  } 
  
  return (String)str;
}
%> 
 
<!DOCTYPE html> 
<html lang="ko"> 
<head> 
<meta charset="UTF-8"> 
<title>http://localhost:9093/session/session.jsp</title> 
 
<link href="/css/style.css" rel="Stylesheet" type="text/css">

</head> 
 
<body>
<DIV class='title'>SESSION 테스트</DIV>
 
  <ul>
    <li>
      Tomcat이 자동 생성한 session 변수<br>
      고유한 세션 ID: <%=session.getId() %><br>
      session의 수명: <%= session.getMaxInactiveInterval() %> 초
      <hr>
    </li>

    <li class='li_none'>
      <button type='button' onclick="location.href='./create.jsp'">개발자가 세션 변수 생성</button>
      <button type='button' onclick="location.href='./delete.jsp'">세션 삭제(접속해제)</button>
      <button type='button' onclick="location.href='./send.jsp'">send.jsp 갔다오기</button>
    </li>
    <li>
      개발자가 생성한 session 변수<br>
      ID: <%=checkNull(session.getAttribute("id")) %> <br> 
      PASSWORD: <%=checkNull(session.getAttribute("passwd")) %><br>
      로그인 날짜:
      <%
      Object obj = session.getAttribute("date");
      if (obj != null) {
        Date date = (Date)obj; // Object -> 형변환 -> Date
        out.println(date.toLocaleString());
      }
      %>
    </li>
  </ul>
 
 
</body>
 
</html>

