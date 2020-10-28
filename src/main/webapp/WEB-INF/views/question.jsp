<%@ page contentType="text/html;charset=UTF-8"%>
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
<div class="flex flex-col items-center">
    <div class="w-full p-8">
        <div class="p-6 rounded-lg
            bg-white hover:bg-gray-100
            border-b border-gray-200
            shadow hover:shadow-lg
            transition-all duration-200
            flex flex-col">
            <div class="flex flex-row items-center">
                <c:import url="fragments/shields.jsp"/>
                <span class="ml-4 text-2xl"><c:out value="${question.title}"/></span>
            </div>
            <span class="text-gray-500 mt-2"><c:out value="${question.description}"/></span>
            <div class="flex flex-row mt-2 items-center">
                <div class="flex-grow"></div>
                <c:import url="fragments/authorQuestion.jsp"/>
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

    <c:import url="fragments/addAnswer.jsp"/>
</div>
</body>
</html>
