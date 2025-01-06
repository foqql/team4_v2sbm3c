<%@ page contentType="text/html; charset=UTF-8" %> 
 
<!DOCTYPE html>
<html> 
<head> 
<meta charset="UTF-8"> 
<title>http://localhost:9091/forward/fd_a1.jsp</title> 
<link href="../css/style.css" rel="Stylesheet" type="text/css">
</head> 
<body>
 
<form name='frm' method='post' action='./fd_a2.jsp'> 
  <DIV style='text-align: center;'>
    <IMG src='./images/spring09.jpg' style='width: 50%;'>
  </DIV> 
  <DIV style="width: 90%; margin: 10px auto;"> 
    영화 제목: <input type='text' name='title' value='킹덤'><br> 
    주연 배우: <input type='text' name='actor' size='30' value='전지현'><br> 
    개봉 연도: <input type='text' name='year' value='2020-01-01'><br> 
    <br> 
    <input type='submit' value='서버로 전송'>
  </DIV> 
</form> 
</body> 
</html>

