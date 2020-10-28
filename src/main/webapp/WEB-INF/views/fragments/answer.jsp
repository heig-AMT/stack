<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:useBean scope="request" id="answer" type="ch.heigvd.amt.stack.application.answer.dto.AnswerDTO"/>

<div class="flex flex-row justify-center mt-4">
    <div class="mx-2 flex flex-col voteBox">
        <form class="mt-2 mb-0 self-center" action="vote.do" method="POST">
            <input type="hidden" name="type" value="upvote"/>
            <input type="hidden" name="answer" value="<c:out value="${answer.id.toString()}"/>"/>
            <c:choose>
                <c:when test="${answer.hasPositiveVote}">
                    <img src="${pageContext.request.contextPath}/assets/upvote_green.svg" alt="upvote">
                </c:when>
                <c:when test="${connected.connected}">
                    <input type="image" src="${pageContext.request.contextPath}/assets/vote.svg" alt="upvote">
                </c:when>
                <c:otherwise>
                    <img src="${pageContext.request.contextPath}/assets/vote.svg" alt="upvote">
                </c:otherwise>
            </c:choose>
        </form>
        <c:choose>
            <c:when test="${answer.hasPositiveVote}">
                <span class="text-green-700 text-center"><c:out value="${answer.positiveVotesCount}"/></span>
            </c:when>
            <c:otherwise>
                <span class="text-white text-center"><c:out value="${answer.positiveVotesCount}"/></span>
            </c:otherwise>
        </c:choose>
        <div class="border-b-2 color-white"></div>
        <c:choose>
            <c:when test="${answer.hasNegativeVote}">
                <span class="text-red-500 text-center"><c:out value="${answer.negativeVotesCount}"/></span>
            </c:when>
            <c:otherwise>
                <span class="text-white text-center"><c:out value="${answer.negativeVotesCount}"/></span>
            </c:otherwise>
        </c:choose>
        <form class="mb-2 self-center" action="vote.do" method="POST">
            <input type="hidden" name="type" value="downvote"/>
            <input type="hidden" name="answer" value="<c:out value="${answer.id.toString()}"/>"/>
            <c:choose>
                <c:when test="${answer.hasNegativeVote}">
                    <img src="${pageContext.request.contextPath}/assets/downvote_red.svg" alt="downvote">
                </c:when>
                <c:when test="${connected.connected}">
                    <input class="transform rotate-180" type="image" src="${pageContext.request.contextPath}/assets/vote.svg" alt="downvote">
                </c:when>
                <c:otherwise>
                    <img class="transform rotate-180" src="${pageContext.request.contextPath}/assets/vote.svg" alt="downvote">
                </c:otherwise>
            </c:choose>
        </form>
    </div>
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
                <form class="flex items-end mb-0" action="deleteAnswer.do" method="POST">
                    <input type="hidden" name="answer" value="<c:out value="${answer.id.toString()}"/>"/>
                    <div class="flex flex-row">
                        <input type="image" src="${pageContext.request.contextPath}/assets/delete.svg" alt="delete">
                        <input class="ml-1 bg-transparent font-semibold text-red-500 hover:text-red-600 cursor-pointer" type="submit" value="Delete"/>
                    </div>
                </form>
            </c:if>
            <div class="flex-grow"></div>
            <c:import url="fragments/authorAnswer.jsp"/>
        </div>
    </div>
</div>
