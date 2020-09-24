<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<jsp:include page="../header.jsp">
    <jsp:param name="pageTitle" value="Login"/>
</jsp:include>
<body>
<jsp:include page="../navigation.jsp"/>
</body>
<form action="login.do" method="POST">
    <input type="email" name="username" placeholder="Username"/>
    <input type="password" name="password" placeholder="Password"/>
    <input type="submit" value="Log in"/>
</form>
</html>
