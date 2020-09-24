<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<nav class="w-full bg-white border-b border-grey-700">
    <div class="w-full items-center flex px-4 py-4">
        <div>
            <a class="hover:text-blue-500 text-lg px-4 py-2" href="${pageContext.request.contextPath}/">Home</a>
            <a class="hover:text-blue-500 text-lg px-4 py-2" href="${pageContext.request.contextPath}/questions">Questions</a>
        </div>

        <div class="flex-grow">
        </div>

        <div>
            <a class="rounded border border-gray-400 bg-transparent p-2 mr-4 hover:bg-gray-100 hover:text-gray-700" href="${pageContext.request.contextPath}/login">Login</a>
            <a class="rounded bg-blue-600 p-2 text-white hover:bg-blue-500" href="${pageContext.request.contextPath}/register">Register</a>
        </div>
    </div>
</nav>