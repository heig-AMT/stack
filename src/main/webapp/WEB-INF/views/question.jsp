<%@ page import="java.time.format.DateTimeFormatter" %>
<%@ page import="java.time.format.FormatStyle" %>
<%@ page import="java.util.Locale" %>
<%@ page import="java.time.ZoneId" %>
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
<div class="flex flex-col pt-16 h-screen
            items-center">

    <div class="w-full p-8">
        <div class="m-2 p-6 rounded-lg
            bg-white hover:bg-gray-100
            border-b border-gray-200
            shadow hover:shadow-lg
            transition-all duration-200
            flex flex-col">
            <div class="flex flex-row items-center">
                <c:choose>
                    <c:when test="${question.status eq 'New'}">
                        <div class="border-2 rounded-full px-4 border-blue-500 text-blue-500">New</div>
                    </c:when>
                    <c:when test="${question.status eq 'Resolved'}">
                        <div class="border-2 rounded-full px-4 border-green-500 text-green-500">Resolved</div>
                    </c:when>
                    <c:otherwise>
                        <div class="border-2 rounded-full px-4 border-gray-500 text-gray-500">Open</div>
                    </c:otherwise>
                </c:choose>
                <span class="ml-4 text-2xl"><c:out value="${question.title}"/></span>
            </div>
            <span class="text-gray-500 mt-2"><c:out value="${question.description}"/></span>
            <div class="flex flex-row mt-4 items-center">
                <div class="flex-grow"></div>
                <div class="flex flex-col items-end">
            <span class="text-sm text-gray-500">
                <%=
                DateTimeFormatter.ofLocalizedDateTime( FormatStyle.SHORT )
                        .withLocale(Locale.FRANCE)
                        .withZone(ZoneId.systemDefault()).format(question.getCreation())
                %>
            </span>
                    <span class="text-sm text-gray-500">by <c:out value="${question.author}"/></span>
                </div>
            </div>
        </div>
    </div>

    <div class="px-16 w-full flex flex-col">
        <div class="border-b-2 color-white">
            <span class="px-2 text-white font-semibold">${answers.answers.size()} comments</span>
        </div>

            <c:forEach items="${answers.answers}" var="answer">
                <c:set var="answer" value="${answer}" scope="request"/>
                <c:import url="fragments/answer.jsp"/>
            </c:forEach>
    </div>

    <div class="flex-end mt-4 p-6 max-w-xl w-full bg-white rounded-lg shadow-md">
        <form action="answer.do"
              method="POST"
              class="w-full">
            <div class="flex flex-col -mx-3 mb-6">
                <div class="md:w-full px-3">
                    <label for="body" class="pb-4 block uppercase text-lg font-bold text-gray-700">Add your answer</label>
                    <textarea rows="6" id="body"
                              class="p-3 block w-full bg-white text-gray-900 font-medium border border-gray-400 rounded-lg"
                              type="text" name="body" placeholder="Enter an answer" required></textarea>
                </div>
                <input type="hidden" name="question" value="${pageContext.request.getParameter("id")}"/>

            </div>
            <div class="w-full px-3">
                <input class="p-3 block w-full bg-blue-600 text-gray-100 font-bold border border-gray-200 rounded-lg hover:bg-blue-500 focus:bg-white focus:border-gray-500"
                       type="submit" value="Answer question"/>
            </div>
        </form>
    </div>
</div>
</body>
</html>