<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<jsp:include page="../header.jsp">
    <jsp:param name="pageTitle" value="Register"/>
</jsp:include>
<body>
<form action="register.do"
      method="POST"
      class="bg-white m-auto shadow p-8">
    <input type="email" name="username" placeholder="Username"/>
    <input type="password" name="password" placeholder="Password"/>
    <input type="submit" value="Register"/>
</form>
</body>
</html>
