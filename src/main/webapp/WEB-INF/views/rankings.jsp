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
    <div class="w-full space-x-12 my-8 justify-evenly flex flex-row">
        <a class="py-2 px-4 text-center center flex-grow rounded bg-white <c:if test = "${rankings.leaderboard == 'Answers'}">border-2 border-gray-700</c:if>"
           href="${pageContext.request.contextPath}/rankings/answers">Answers</a>
        <a class="py-2 px-4 text-center center flex-grow rounded bg-white <c:if test = "${rankings.leaderboard == 'Questions'}">border-2 border-gray-700</c:if>"
           href="${pageContext.request.contextPath}/rankings/questions">Questions</a>
        <a class="py-2 px-4 text-center center flex-grow rounded bg-white <c:if test = "${rankings.leaderboard == 'Comments'}">border-2 border-gray-700</c:if>"
           href="${pageContext.request.contextPath}/rankings/comments">Comments</a>
    </div>
    <div class="space-y-1">
        <c:forEach items="${rankings.rankings}" var="rank">
            <div class="bg-white rounded py-1 px-2 justify-between flex flex-row text-gray-500">
                <span>#<c:out value="${rank.rank}"/> | <c:out value="${rank.username}"/></span>
                <span>| <c:out value="${rank.points}"/></span>
            </div>
        </c:forEach>
    </div>
    <div class="p-5 flex justify-center">
        <div class="flex rounded border border-gray-400 bg-white text-gray-700">

            <c:if test="${rankings.hasPreviousPage}">
                <a class="px-3 py-1 hover:bg-gray-200 hover:text-gray-700 focus:bg-gray-300 focus:border-gray-400"
                   href="${pageContext.request.contextPath}${pageContext.request.getAttribute("prevPageUrl")}">Previous</a>
            </c:if>

            <div class="border border-gray-400"></div>

            <span class="px-3 py-1 text-blue-600 hover:bg-gray-200 hover:text-blue-800 focus:bg-gray-300 focus:border-gray-400">${rankings.page + 1}</span>

            <div class="border border-gray-400"></div>

            <c:if test="${rankings.hasFollowingPage}">
                <a class="px-6 py-1 hover:bg-gray-200 hover:text-gray-700 focus:bg-gray-300 focus:border-gray-400"
                   href="${pageContext.request.contextPath}${pageContext.request.getAttribute("nextPageUrl")}">Next</a>
            </c:if>
        </div>
    </div>
</div>

</body>
</html>
