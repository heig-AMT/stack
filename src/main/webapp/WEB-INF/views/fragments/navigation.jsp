<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:useBean scope="request" id="connected" type="ch.heigvd.amt.stack.application.authentication.dto.ConnectedDTO"/>

<nav class="w-full bg-white border-b border-grey-700">
    <div class="w-full items-center flex px-4 py-4">
        <div>
            <a class="hover:text-blue-500 text-lg px-4 py-2" href="${pageContext.request.contextPath}/">Home</a>
            <a class="hover:text-blue-500 text-lg px-4 py-2" href="${pageContext.request.contextPath}/questions">Questions</a>
        </div>

        <div class="flex-grow">
        </div>

        <c:choose>
            <c:when test="${connected.connected}">
                <a class="rounded bg-blue-600 p-2 text-white hover:bg-blue-500"
                   href="${pageContext.request.contextPath}/ask">Ask something</a>
                <form action="logout.do"
                      method="POST"
                      class="rounded border border-gray-400 bg-transparent p-2 mr-4 hover:bg-gray-100 hover:text-gray-700">
                    <input type="submit" value="Log out"/>
                </form>
            </c:when>
            <c:otherwise>
                <div>
                    <a class="rounded border border-gray-400 bg-white p-2 mr-4 hover:bg-gray-100 hover:text-gray-700"
                       href="${pageContext.request.contextPath}/login">Login</a>
                    <a class="rounded bg-blue-600 p-2 text-white hover:bg-blue-500"
                       href="${pageContext.request.contextPath}/register">Register</a>
                </div>
            </c:otherwise>
        </c:choose>
    </div>
</nav>