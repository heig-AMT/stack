<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:useBean scope="request" id="question" type="ch.heigvd.amt.stack.application.question.dto.QuestionDTO"/>

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
                <c:out value="${question.status}"/>
                <div class="border-2 rounded-full px-4 border-gray-500 text-gray-500">Open</div>
            </c:otherwise>
        </c:choose>
        <a class="ml-4" href="${pageContext.request.contextPath}/question?<c:out value="${question.id.id.toString()}"/>"><c:out value="${question.title}"/></a>
    </div>
    <span class="text-gray-500 mt-2"><c:out value="${question.description}"/></span>
    <div class="flex flex-row mt-4 items-center">
        <span class="text-sm text-gray-500">0 comments</span>
        <div class="flex-grow"></div>
        <span class="text-sm text-gray-500">by <c:out value="${question.author}"/></span>
    </div>
</div>
