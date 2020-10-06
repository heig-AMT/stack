<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:useBean scope="request" id="connected" type="ch.heigvd.amt.stack.application.authentication.dto.ConnectedDTO"/>

<nav class="flex px-4 py-2 items-center w-full bg-white border-b border-grey-700 fixed top-0">
    <a href="${pageContext.request.contextPath}/">
        <img class="h-12 object-contain px-4 pb-2 pt-4"
             src="${pageContext.request.contextPath}/assets/logo.png"
             alt="Logo"/>
    </a>
    <a class="hover:text-blue-500 text-lg px-4 py-2" href="${pageContext.request.contextPath}/">Home</a>
    <a class="hover:text-blue-500 text-lg px-4 py-2" href="${pageContext.request.contextPath}/questions">Questions</a>

    <form
            action="${pageContext.request.contextPath}/questions"
            method="GET"
            class="flex flex-grow justify-between px-2 mr-4 rounded-full border border-gray-400">
        <input type="text" name="search" size="38" placeholder="Search by content in the title, description or tags !" required>
        <input type="image" src="${pageContext.request.contextPath}/assets/magnify.svg" alt="submit">
    </form>

    <c:choose>
        <c:when test="${connected.connected}">
            <a class="px-4 py-2 rounded bg-blue-600 text-white hover:bg-blue-500"
               href="${pageContext.request.contextPath}/ask">Ask something</a>
            <form action="logout.do"
                  class="m-0"
                  method="POST">
                <input type="submit" value="Log out"
                       class="mx-2 px-4 py-2 rounded border border-gray-400 bg-white text-black hover:bg-gray-100 hover:text-gray-700"/>
            </form>
        </c:when>
        <c:otherwise>
            <a class="mx-2 px-4 py-2 rounded border border-gray-400 bg-white text-black hover:bg-gray-100 hover:text-gray-700"
               href="${pageContext.request.contextPath}/login">Login</a>
            <a class="mx-2 px-4 py-2 rounded bg-blue-600 text-white hover:bg-blue-500"
               href="${pageContext.request.contextPath}/register">Register</a>
        </c:otherwise>
    </c:choose>
</nav>