<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:useBean scope="request" id="statistics" type="ch.heigvd.amt.stack.application.statistics.dto.UsageStatisticsDTO"/>

<html>
<jsp:include page="WEB-INF/views/fragments/head.jsp">
    <jsp:param name="pageTitle" value="Home"/>
</jsp:include>
<body class="bg-gradient-to-r from-teal-400 to-blue-500">
<jsp:include page="WEB-INF/views/fragments/navigation.jsp"/>
<div class="flex p-16 h-screen items-center justify-center text-center">
    <div>
        <h1 class="text-white text-6xl">
            stack<span class="font-bold">underflow</span>
        </h1>
        <h3 class="text-gray-800 font-extrabold text-2xl">
            Ask anything like our <c:out value="${statistics.questionCount}"/> code related questions, answer them like <c:out value="${statistics.answerCount}"/> others did, and get insulted by <c:out value="${statistics.userCount}"/> of your peers!
        </h3>
    </div>
</div>
</body>
</html>