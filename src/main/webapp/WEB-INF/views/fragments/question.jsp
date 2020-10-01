<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:useBean scope="request" id="question" type="ch.heigvd.amt.stack.application.question.dto.QuestionDTO"/>

<tr class="bg-white hover:bg-gray-100 border-b border-gray-200">
    <td class="px-4 py-4"><c:out value="${question.title}"/></td>
    <td class="px-4 py-4"><c:out value="${question.description}"/></td>
</tr>
