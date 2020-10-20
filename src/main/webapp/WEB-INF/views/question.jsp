<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:useBean scope="request" id="question" type="ch.heigvd.amt.stack.application.question.dto.QuestionDTO"/>
<jsp:useBean scope="request" id="answers" type="ch.heigvd.amt.stack.application.answer.dto.AnswerListDTO"/>

<html>
<jsp:include page="fragments/head.jsp">
    <jsp:param name="pageTitle" value="Question"/>
</jsp:include>
<body class="bg-gradient-to-r from-teal-400 to-blue-500">
<jsp:include page="fragments/navigation.jsp"/>
<div class="pt-16"/>
<div class="max-w-4xl m-auto flex flex-col">
Hello, world
</div>
</body>
</html>
