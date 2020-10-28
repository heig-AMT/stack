<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:useBean scope="request" id="answer" type="ch.heigvd.amt.stack.application.answer.dto.AnswerDTO"/>
<jsp:useBean scope="request" id="question" type="ch.heigvd.amt.stack.application.question.dto.QuestionDTO"/>

<div class="ml-12 mt-2 rounded-lg">
    <form action="comment.do"
          method="POST"
          class="flex flex-row">
        <div class="flex flex-row w-full">
            <textarea rows="1" id="body"
                      class="p-2 pb-0 block w-full bg-white text-gray-900 font-medium border border-gray-400 rounded-lg"
                      name="body" placeholder="Enter a comment" required></textarea>

            <input type="hidden" name="answer" value="${answer.id.toString()}"/>
            <input type="hidden" name="question" value="${question.id.toString()}"/>

            <c:choose>
                <c:when test="${connected.connected}">
                    <input class="ml-2 mt-2 place-self-start" type="image" src="${pageContext.request.contextPath}/assets/reply.svg" alt="submit">
                </c:when>
                <c:otherwise>
                    <a href="${pageContext.request.contextPath}/login">
                        <img class="h-6 ml-2 mt-2"
                             src="${pageContext.request.contextPath}/assets/reply.svg"
                             alt="comment"/>
                    </a>
                </c:otherwise>
            </c:choose>
        </div>
    </form>
</div>