<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<jsp:include page="fragments/head.jsp">
    <jsp:param name="pageTitle" value="Login"/>
</jsp:include>
<body>
<jsp:include page="fragments/navigation.jsp"/>
<div class="flex pt-16 h-screen
            items-center justify-center
            bg-gradient-to-r from-teal-400 to-blue-500">
<div>
    <h1>Username: <c:out value="${connected.username}"/></h1>
    <form action="changePassword.do"
          method="POST">
        <input type="hidden" name="username" value="<c:out value="${connected.username}"/>"/>
        <div>
            <label for="currentPassword">Current password</label>
            <input id="currentPassword"
                   type="password" name="currentPassword" placeholder="oldPass" required>
        </div>
        <div>
            <label for="newPassword">New password</label>
            <input id="newPassword"
                   type="password" name="newPassword" placeholder="newPass" required>
        </div>
        <div>
            <input type="submit" value="Change password"/>
        </div>
    </form>
</div>
</div>
</body>
</html>
