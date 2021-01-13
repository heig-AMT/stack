<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:useBean scope="request" id="rankings"
             type="ch.heigvd.amt.stack.application.rankings.RankingDTO"/>

<html>
<jsp:include page="fragments/head.jsp">
    <jsp:param name="pageTitle" value="Rankings"/>
</jsp:include>
<body class="bg-gradient-to-r from-teal-400 to-blue-500">
<jsp:include page="fragments/navigation.jsp"/>
<div class="pt-16"/>
<div class="max-w-4xl m-auto flex flex-col">
    <div class="max-w-4xl m-auto flex flex-row mt-8">
        <form action="changeCategory.do" method="POST">
            <div class="max-w-4xl justify-between">
            <!-- TODO: not hardcode categories if possible -->
            <input class="flex-1 py-2 px-4"
                    name="newCategory" type="submit" value="Answers">
            <input class="flex-1 py-2 px-4"
                    name="newCategory" type="submit" value="Questions">
            <input class="flex-1 py-2 px-4"
                    name="newCategory" type="submit" value="Comments">
            </div>
        </form>
    </div>
    <div>
        <c:forEach items="${rankings.rankings}" var="rank">
            <div><c:out value="${rank}"/></div>
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
