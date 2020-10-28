<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:useBean scope="request" id="comment" type="ch.heigvd.amt.stack.application.answer.dto.CommentDTO"/>

<div class="flex flex-row justify-center mt-4">
    <div class="px-6 py-2 w-full rounded-lg
            bg-white hover:bg-gray-100
            border-b border-gray-200
            shadow hover:shadow-lg
            transition-all duration-200
            flex flex-col">
        <span class="text-gray-500"><c:out value="${answer.body}"/></span>
        <div class="flex-grow"></div>
        <div class="flex flex-row">
            <c:if test="${answer.deletionEnabled}">
                <form class="flex flex-row mx-2 mb-0" action="deleteAnswer.do" method="POST">
                    <input type="hidden" name="answer" value="<c:out value="${answer.id.toString()}"/>"/>
                    <input type="image" src="${pageContext.request.contextPath}/assets/delete.svg" alt="delete">
                    <input class="ml-1 bg-transparent font-semibold text-red-500 hover:text-red-600 cursor-pointer" type="submit" value="Delete"/>
                </form>
            </c:if>
            <div class="flex-grow"></div>
            <c:import url="fragments/authorComment.jsp"/>
        </div>
    </div>
</div>