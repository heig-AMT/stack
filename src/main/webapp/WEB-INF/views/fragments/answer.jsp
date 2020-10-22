<%@ page import="java.time.format.DateTimeFormatter" %>
<%@ page import="java.time.format.FormatStyle" %>
<%@ page import="java.util.Locale" %>
<%@ page import="java.time.ZoneId" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:useBean scope="request" id="answer" type="ch.heigvd.amt.stack.application.answer.dto.AnswerDTO"/>

<div class="mt-4 p-6 rounded-lg
            bg-white hover:bg-gray-100
            border-b border-gray-200
            shadow hover:shadow-lg
            transition-all duration-200
            flex flex-col">
    <span class="text-gray-500 mt-2"><c:out value="${answer.body}"/><% Object question; %></span>
    <div class="flex flex-row mt-4 items-center">
        <div class="flex-grow"></div>
        <div class="flex flex-col items-end">
            <span class="text-sm text-gray-500">
                <%=
                DateTimeFormatter.ofLocalizedDateTime( FormatStyle.SHORT )
                        .withLocale(Locale.FRANCE)
                        .withZone(ZoneId.systemDefault()).format(answer.getCreation())
                %>
            </span>
            <span class="text-sm text-gray-500">by <c:out value="${question.author}"/></span>
        </div>
    </div>
</div>