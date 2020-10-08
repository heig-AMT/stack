<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<jsp:include page="fragments/head.jsp">
    <jsp:param name="pageTitle" value="Login"/>
</jsp:include>
<body>
<jsp:include page="fragments/navigation.jsp"/>
<div class="flex pt-16 h-screen
            items-center justify-center
            bg-gradient-to-r from-teal-400 to-blue-500">
    <div class="w-5/12 rounded-md p-16 text-center">
        <img
                src="${pageContext.request.contextPath}/assets/taken.svg"
                alt="404 image"
        />
        <span class="text-3xl text-blue-900 font-mono font-semibold">This page was taken by</span>
        <span class="text-4xl text-blue-100 font-mono underline font-bold px-2">404</span>
        <span class="text-3xl text-blue-900 font-mono font-semibold">aliens</span>
    </div>
</div>
</body>
</html>
