<%@ page contentType="text/html; charset=UTF-8" %> 
 
<% request.setCharacterEncoding("utf-8"); %>
 
<!DOCTYPE html>
<html> 
<head> 
<meta charset="UTF-8"> 
<title>http://localhost:9091/redirect/rd_a3.jsp</title>
<link href="../css/style.css" rel="Stylesheet" type="text/css">
</head>
 
<body>
  <DIV style='text-align: center;'>
    <IMG src='./images/spring11.jpg' style='width: 50%;'>
  </DIV> 
  <DIV style="width: 90%; margin: 10px auto;">
    --> rd_a3.jsp<br><br>
    제목: <%=request.getParameter("title") %><br>
    배우: <%=request.getParameter("actor") %><br>
    연도: <%=request.getParameter("year") %><br>
  </DIV>  
</body>
</html>
