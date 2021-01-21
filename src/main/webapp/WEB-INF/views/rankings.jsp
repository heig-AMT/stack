<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:useBean scope="request" id="rankings"
             type="ch.heigvd.amt.stack.application.rankings.dto.LeaderboardDTO"/>

<html>
<jsp:include page="fragments/head.jsp">
    <jsp:param name="pageTitle" value="Rankings"/>
</jsp:include>
<body class="bg-gradient-to-r from-teal-400 to-blue-500">
<jsp:include page="fragments/navigation.jsp"/>
<div class="pt-16"/>
<div class="max-w-4xl m-auto flex flex-col">
        <form class="w-full justify-around flex flex-row space-x-4 mt-8"
                action="changeCategory.do" method="POST">
            <!-- TODO: not hardcode categories if possible -->
            <input class="flex-1 py-2 px-4 rounded"
                    name="newCategory" type="submit" value="Answers">
            <input class="flex-1 py-2 px-4 rounded"
                    name="newCategory" type="submit" value="Questions">
            <input class="flex-1 py-2 px-4 rounded"
                    name="newCategory" type="submit" value="Comments">
        </form>
    <div class="space-y-1">
        <c:forEach items="${rankings.rankings}" var="rank">
            <div class="bg-white rounded py-1 px-2 justify-between flex flex-row text-gray-500">
                <span>#<c:out value="${rank.rank}"/> | <c:out value="${rank.username}"/></span>
                <span>| <c:out value="${rank.points}"/></span>
            </div>
        </c:forEach>
    </div>
</div>

<!--<form action="changePage.do" method="post">
    <input name="pageNav" type="submit" value="<<">
    <input name="pageNav" type="submit" value="1">
    <input name="pageNav" type="submit" value=">>">
</form>-->

</body>
</html>
