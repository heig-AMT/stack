<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:useBean scope="request" id="rankings" type="ch.heigvd.amt.stack.application.rankings.RankingDTO"/>

<html>
<jsp:include page="fragments/head.jsp">
    <jsp:param name="pageTitle" value="Rankings"/>
</jsp:include>
<body class="bg-gradient-to-r from-teal-400 to-blue-500">
<jsp:include page="fragments/navigation.jsp"/>
<div class="pt-16"/>
<div class="max-w-4xl m-auto flex flex-col">
    <span class="text-lg px-4 py-2"><c:out value="${rankings.categoryName}"/></span>
    <c:forEach items="${rankings.rankings}" var="rank">
            <span><c:out value="${rank}"/></span>
    </c:forEach>
</div>
<form action="changeCategory.do" method="POST">
    <select name="newCategory">
        <!-- TODO: not hardcode categories -->
        <option value="answers">Answers</option>
        <option value="questions">Questions</option>
        <option value="comments">Comments</option>
    </select>
    <input type="submit" value="Search"/>
</form>

</body>
</html>
