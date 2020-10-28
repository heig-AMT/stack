<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:useBean scope="request" id="question" type="ch.heigvd.amt.stack.application.question.dto.QuestionDTO"/>

<div class="flex-end my-16 p-6 max-w-xl w-full bg-white rounded-lg shadow-md">
    <form action="answer.do"
          method="POST"
          class="w-full">
        <div class="flex flex-col -mx-3 mb-6">
            <div class="md:w-full px-3">
                <label for="body" class="pb-4 block uppercase text-lg font-bold text-gray-700">Add your answer</label>
                <textarea rows="6" id="body"
                          class="p-3 block w-full bg-white text-gray-900 font-medium border border-gray-400 rounded-lg"
                          name="body" placeholder="Enter an answer" required></textarea>
            </div>
            <input type="hidden" name="question" value="${question.id.toString()}"/>

        </div>
        <div class="w-full px-3">
            <c:choose>
                <c:when test="${connected.connected}">
                    <input class="p-3 block w-full bg-blue-600 text-gray-100 font-bold border border-gray-200 rounded-lg hover:bg-blue-500 focus:bg-blue-800 focus:border-gray-500"
                           type="submit" value="Answer question">
                </c:when>
                <c:otherwise>
                    <a class="text-center p-3 block w-full bg-gray-500 text-white font-bold border border-gray-200 rounded-lg hover:bg-gray-700 focus:bg-blue-800 focus:border-gray-500"
                       href="${pageContext.request.contextPath}/login">
                        To answer, please login
                    </a>
                </c:otherwise>
            </c:choose>
        </div>
    </form>
</div>