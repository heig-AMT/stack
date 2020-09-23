<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<ul>
    <c:forEach items="${questions}" var="question">
        <li>${question.getTitle()} : "${question.getDescription()}"</li>
    </c:forEach>
</ul>
</body>
</html>
