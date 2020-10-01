<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:useBean scope="request" id="questions" type="ch.heigvd.amt.stack.application.question.dto.QuestionListDTO"/>

<html>
<jsp:include page="fragments/head.jsp">
    <jsp:param name="pageTitle" value="Questions"/>
</jsp:include>
<body class="bg-gradient-to-r from-teal-400 to-blue-500">
<jsp:include page="fragments/navigation.jsp"/>
<div class="pt-16"/>
<form
        action="${pageContext.request.contextPath}/questions"
        method="GET">
    <label for="search">Search</label>
    <input type="text" id="search" name="search" placeholder="..." required>
    <input type="submit" value="Filter">
</form>
<div class="max-w-4xl m-auto flex flex-col">
    <c:forEach items="${questions.questions}" var="question">
        <c:set var="question" value="${question}" scope="request"/>
        <c:import url="fragments/question.jsp"/>
    </c:forEach>
</div>
</body>
</html>
