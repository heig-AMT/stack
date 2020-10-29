<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<jsp:include page="fragments/head.jsp">
    <jsp:param name="pageTitle" value="Error 404"/>
</jsp:include>
<body class="bg-gradient-to-r from-teal-400 to-blue-500">
<jsp:include page="fragments/navigation.jsp"/>
<div class="pt-16"/>
<div class="flex h-full
            items-center justify-center">
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
