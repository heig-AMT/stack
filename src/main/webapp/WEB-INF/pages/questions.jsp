<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:useBean scope="request" id="questions" type="ch.heigvd.amt.stack.application.question.dto.QuestionListDTO"/>

<html>
<jsp:include page="../header.jsp">
    <jsp:param name="pageTitle" value="Questions"/>
</jsp:include>
<body>
<jsp:include page="../navigation.jsp"/>
<div class="bg-white w-full">
        <table class="w-full">
            <thead>
                <tr class="text-left text-gray-700 font-medium">
                    <th class="px-4 py-2 bg-gray-200">Title</th>
                    <th class="px-4 py-2 bg-gray-200">Description</th>
                </tr>
            </thead>
            <tbody class="text-gray-700">
                <c:forEach items="${questions.questions}" var="question">
                    <tr class="hover:bg-gray-100 border-b border-gray-200">
                        <td class="px-4 py-4">${question.title}</td>
                        <td class="px-4 py-4">${question.description}</td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
</div>
</body>
</html>
