<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:choose>
    <c:when test="${question.status eq 'New'}">
        <div class="border-2 rounded-full px-4 border-blue-500 text-blue-500">New</div>
    </c:when>
    <c:when test="${question.status eq 'Resolved'}">
        <div class="border-2 rounded-full px-4 border-green-500 text-green-500">Resolved</div>
    </c:when>
    <c:otherwise>
        <div class="border-2 rounded-full px-4 border-gray-500 text-gray-500">Open</div>
    </c:otherwise>
</c:choose>