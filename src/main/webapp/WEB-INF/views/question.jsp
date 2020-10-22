<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:useBean scope="request" id="answers" type="ch.heigvd.amt.stack.application.answer.dto.AnswerListDTO"/>

<html>
<jsp:include page="fragments/head.jsp">
    <jsp:param name="pageTitle" value="Question"/>
</jsp:include>
<body class="bg-gradient-to-r from-teal-400 to-blue-500">
<jsp:include page="fragments/navigation.jsp"/>
<div class="flex flex-col pt-16 h-screen
            items-center">

    <div class="px-16 pt-4 w-full flex flex-col">
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
                    <label for="body" class="mb-2 block uppercase text-xs font-bold text-gray-700">Answer</label>
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