<%@ page contentType="text/html; charset=UTF-8" %>
 
<%
String windowOpen = "open"; // 이벤트등 공지사항 출력 여부를 결정

Cookie[] cookies = request.getCookies(); // 브러우저에 저장된 Cookie를 가져옴
Cookie cookie = null;

if (cookies != null) { // 쿠키가 존재 한다면
  for (int index=0; index < cookies.length; index++) { // 쿠키 갯수만큼 순환
    cookie = cookies[index];  // 쿠키 목록에서 쿠키 추출
    if (cookie.getName().equals("windowOpen")) { // 이름 비교
      windowOpen = cookie.getValue();  // 쿠키 값, close
    }
  }
}
%>
<!DOCTYPE html> 
<html lang="ko"> 
<head> 
<meta charset="UTF-8"> 
<title>http://localhost:9091/cookie/main.jsp</title> 
 
<link href="/css/style.css" rel="Stylesheet" type="text/css">
 
<script type="text/javascript">
  window.onload = function() {
    if ('<%=windowOpen %>' == 'open') {
      openNotice();  // 창 열기 
    }  
  }
  
  function openNotice(){
    var url = './notice.jsp';
    var win = window.open(url, '공지 사항', 'width=450px, height=450px');
    
    var x = (screen.width - 450) / 2;
    var y = (screen.height - 450) / 2;
    
    win.moveTo(x, y); // 화면 중앙으로 이동
  }

</script>
</head> 
 
<body>
  <div style="margin: 20px;">
    2024년 자격증 취득 안내<br><br>
    2024년 새해를 맞아 자격증 취득을 추천합니다.<br><br>
    【<A href='javascript: openNotice();'>자격증 정보 조회</A>】
  </div> 
 
</body>
 
</html>
