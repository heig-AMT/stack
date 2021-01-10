<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:useBean scope="request" id="rankings" type="ch.heigvd.amt.stack.application.rankings.RankingListDTO"/>

<html>
<jsp:include page="fragments/head.jsp">
    <jsp:param name="pageTitle" value="Rankings"/>
</jsp:include>
<body class="bg-gradient-to-r from-teal-400 to-blue-500">
<jsp:include page="fragments/navigation.jsp"/>
<div class="pt-16"/>
<div class="max-w-4xl m-auto flex flex-col">
    <c:forEach items="${rankings.rankingDTOS}" var="rank">
        <span><c:out value="${rank}"/></span>
    </c:forEach>
</div>

</body>
</html>
