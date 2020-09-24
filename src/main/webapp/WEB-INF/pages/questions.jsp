<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<jsp:include page="../header.jsp">
    <jsp:param name="pageTitle" value="Questions"/>
</jsp:include>
<body>
<ul>
    <c:forEach items="${questions}" var="question">
        <li>${question.getTitle()} : "${question.getDescription()}"</li>
    </c:forEach>
</ul>
</body>
</html>
