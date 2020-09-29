<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<jsp:include page="WEB-INF/views/fragments/header.jsp">
    <jsp:param name="pageTitle" value="Home"/>
</jsp:include>
<body>
<jsp:include page="WEB-INF/views/fragments/navigation.jsp"/>
    <div class="flex h-screen bg-gray-300 items-center justify-center text-center bg-gradient-to-r from-teal-400 to-blue-500">
        <div>
            <h1 class="text-white text-6xl">
                stack<span class="font-bold">underflow</span>
            </h1>
            <h3 class="text-gray-800 font-extrabold text-2xl">
                Ask anything code related, and get insulted by your peers!
            </h3>
        </div>
    </div>
</body>
</html>