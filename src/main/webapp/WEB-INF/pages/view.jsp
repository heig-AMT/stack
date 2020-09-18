<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
  <head>
    <title>Quotes</title>
  </head>
  <body>
    <h2>Quotes</h2>
    <ul>
      <c:forEach items="${quotes}" var="quote">
        <li>${quote.author} : "${quote.citation}"</li>
      </c:forEach>
    </ul>
  </body>
</html>
