<%@ page import="java.time.format.DateTimeFormatter" %>
<%@ page import="java.time.format.FormatStyle" %>
<%@ page import="java.util.Locale" %>
<%@ page import="java.time.ZoneId" %>
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:useBean scope="request" id="answer" type="ch.heigvd.amt.stack.application.answer.dto.AnswerDTO"/>

<div class="flex flex-col items-end">
                    <span class="text-sm text-gray-500">
                        <%=
                        DateTimeFormatter.ofLocalizedDateTime( FormatStyle.SHORT )
                                .withLocale(Locale.FRANCE)
                                .withZone(ZoneId.systemDefault()).format(answer.getCreation())
                        %>
                    </span>
    <span class="text-sm text-gray-500">by <c:out value="${answer.author}"/></span>
</div>