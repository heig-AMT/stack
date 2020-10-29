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
    <div class="px-4 py-2 flex flex-col w-full
                rounded-lg bg-white hover:bg-gray-100 shadow hover:shadow-lg transition-all duration-200
                <c:if test="${answer.selected}"> border-4 border-green-600 </c:if>
                ">
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

            <c:if test="${answer.selectionEnabled}">
                <c:choose>
                    <c:when test="${!answer.selected}">
                        <form class="flex flex-row mx-2 mb-0" action="selectAnswer.do" method="POST">
                            <input type="hidden" name="question" value="<c:out value="${question.id.toString()}"/>"/>
                            <input type="hidden" name="answer" value="<c:out value="${answer.id.toString()}"/>"/>
                            <input type="image" src="${pageContext.request.contextPath}/assets/check_green.svg" alt="accept">
                            <input class="ml-1 bg-transparent font-semibold text-green-600 hover:text-green-700 cursor-pointer" type="submit" value="Accept"/>
                        </form>
                    </c:when>
                    <c:otherwise>
                        <form class="flex flex-row mx-2 mb-0" action="unselectAnswer.do" method="POST">
                            <input type="hidden" name="question" value="<c:out value="${question.id.toString()}"/>"/>
                            <input type="image" src="${pageContext.request.contextPath}/assets/check_gray.svg" alt="unaccept">
                            <input class="ml-1 bg-transparent font-semibold text-gray-500 hover:text-gray-600 cursor-pointer" type="submit" value="Unaccept"/>
                        </form>
                    </c:otherwise>
                </c:choose>
            </c:if>
            <div class="flex-grow"></div>
            <c:import url="fragments/authorAnswer.jsp"/>
        </div>
    </div>
</div>
<c:if test="${answer.comments.size() > 0}">
    <div class="ml-12 border-b-2 color-white">
        <span class="px-2 text-white font-semibold">${answer.comments.size()} comments</span>
    </div>

    <c:forEach items="${answer.comments}" var="comment">
        <c:set var="comment" value="${comment}" scope="request"/>
        <c:import url="fragments/comment.jsp"/>
    </c:forEach>
</c:if>
<c:import url="fragments/addComment.jsp"/>
