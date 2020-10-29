<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:useBean scope="request" id="question" type="ch.heigvd.amt.stack.application.question.dto.QuestionDTO"/>

<div class="m-2 p-6 pb-1 rounded-lg
            bg-white hover:bg-gray-100
            border-b border-gray-200
            shadow hover:shadow-lg
            transition-all duration-200
            flex flex-col">
    <div class="flex flex-row items-center">
        <c:import url="fragments/shields.jsp"/>
        <a class="ml-4"
           href="${pageContext.request.contextPath}/question?id=<c:out value="${question.id.toString()}"/>"><c:out
                value="${question.title}"/></a>
    </div>
    <span class="text-gray-500 mt-2"><c:out value="${question.description}"/></span>
    <div class="flex flex-row mt-4 items-center">
        <span class="text-sm text-gray-500">0 comments</span>
        <c:if test="${question.deletionEnabled}">
            <form class="flex flex-row mx-2 mb-0" action="deleteQuestion.do" method="POST">
                <input type="hidden" name="question" value="<c:out value="${question.id.toString()}"/>"/>
                <input type="image" src="${pageContext.request.contextPath}/assets/delete.svg" alt="delete">
                <input class="ml-1 bg-transparent font-semibold text-red-500 hover:text-red-600 cursor-pointer" type="submit" value="Delete"/>
            </form>
        </c:if>
        <div class="flex-grow"></div>
        <c:import url="fragments/authorQuestion.jsp"/>
    </div>
</div>