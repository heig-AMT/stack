<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<jsp:include page="../header.jsp">
    <jsp:param name="pageTitle" value="Ask question"/>
</jsp:include>
<body>
<jsp:include page="../navigation.jsp"/>
</body>
<form action="ask.do" method="POST">
    <input type="text" name="title" placeholder="Title"/>
    <input type="text" name="description" placeholder="Description"/>
    <input type="submit" value="Publish"/>
</form>
</html>
