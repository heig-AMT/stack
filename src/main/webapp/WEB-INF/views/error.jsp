<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" isErrorPage="true"%>
<html>
<jsp:include page="fragments/head.jsp">
    <jsp:param name="pageTitle" value="Error"/>
</jsp:include>
<body class="bg-gradient-to-r from-teal-400 to-blue-500">
<jsp:include page="fragments/navigation.jsp"/>
<div class="flex p-16 h-screen items-center justify-center text-center">
    <div class="flex h-full
            items-center justify-center">

        <c:choose>
            <c:when test="<%=response.getStatus() == 403 %>">
                <div class="w-8/12 rounded-md p-16 text-center">
                    <img
                            src="${pageContext.request.contextPath}/assets/authentication.svg"
                            alt="authentication"
                    />
                    <span class="text-3xl text-blue-900 font-mono font-semibold">Whoops, you got a</span>
                    <span class="text-4xl text-blue-100 font-mono underline font-bold px-2">403</span>
                    <span class="text-3xl text-blue-900 font-mono font-semibold">! </span>
                    <br>
                    <span class="text-3xl text-blue-900 font-mono font-semibold">Either you tried to do something you weren't allowed to, or you had the wrong password!</span>
                </div>
            </c:when>
            <c:when test="<%=response.getStatus() == 404 %>">
                <div class="w-8/12 rounded-md p-16 text-center">
                    <img
                            src="${pageContext.request.contextPath}/assets/taken.svg"
                            alt="aliens"
                    />
                    <span class="text-3xl text-blue-900 font-mono font-semibold">This page was taken by</span>
                    <span class="text-4xl text-blue-100 font-mono underline font-bold px-2">404</span>
                    <span class="text-3xl text-blue-900 font-mono font-semibold">aliens</span>
                </div>
            </c:when>
            <c:when test="<%=response.getStatus() == 418 %>">
                <div class="w-8/12 rounded-md p-16 text-center">
                    <img
                            src="${pageContext.request.contextPath}/assets/tea.svg"
                            alt="tea"
                    />
                    <span class="text-4xl text-blue-100 font-mono underline font-bold px-2">418</span>
                    <span class="text-3xl text-blue-900 font-mono font-semibold">I'm a teapot!</span>
                </div>
            </c:when>
            <c:when test="<%=response.getStatus() >= 200 && response.getStatus() < 300 %>">
                <div class="w-8/12 rounded-md p-16 text-center">
                    <img
                            src="${pageContext.request.contextPath}/assets/chilling.svg"
                            alt="chilling"
                    />
                    <span class="text-3xl text-blue-900 font-mono font-semibold">Well, i don't know how you got here, but everything seems to be going smoothly!</span>
                    <br>
                    <a class="text-4xl text-blue-100 font-mono underline font-bold px-2" href="${pageContext.request.contextPath}/">Go back Home</a>
                </div>
            </c:when>
            <c:when test="<%=response.getStatus() >= 300 && response.getStatus() < 400 %>">
                <div class="w-8/12 rounded-md p-16 text-center">
                    <img
                            src="${pageContext.request.contextPath}/assets/redirect.svg"
                            alt="redirect"
                    />
                    <span class="text-3xl text-blue-900 font-mono font-semibold">Whoops, what you were looking for has moved, and you got a code </span>
                    <span class="text-4xl text-blue-100 font-mono underline font-bold px-2"> <%=response.getStatus() %></span>
                    <span class="text-3xl text-blue-900 font-mono font-semibold">! </span>
                </div>
            </c:when>
            <c:when test="<%=response.getStatus() >= 400 && response.getStatus() < 500 %>">
                <div class="w-8/12 rounded-md p-16 text-center">
                    <img
                            src="${pageContext.request.contextPath}/assets/bug.svg"
                            alt="bug"
                    />
                    <span class="text-3xl text-blue-900 font-mono font-semibold">Whoops, looks like something went wrong, you got a </span>
                    <span class="text-4xl text-blue-100 font-mono underline font-bold px-2"> <%=response.getStatus() %></span>
                    <span class="text-3xl text-blue-900 font-mono font-semibold"> error! </span>
                    <br>
                    <span class="text-3xl text-blue-900 font-mono font-semibold"> Please try again in a few minutes.</span>
                </div>
            </c:when>
            <c:when test="<%=response.getStatus() >= 500 && response.getStatus() < 600 %>">
                <div class="w-8/12 rounded-md p-16 text-center">
                    <img
                            src="${pageContext.request.contextPath}/assets/serverdown.svg"
                            alt="serverdown"
                    />
                    <span class="text-3xl text-blue-900 font-mono font-semibold">Whoops, looks like our servers had a problem and you got an error </span>
                    <span class="text-4xl text-blue-100 font-mono underline font-bold px-2"> <%=response.getStatus() %></span>
                    <span class="text-3xl text-blue-900 font-mono font-semibold">! </span>
                    <br>
                    <span class="text-3xl text-blue-900 font-mono font-semibold"> Please try again in a few minutes.</span>
                </div>
            </c:when>
            <c:otherwise>
                <div class="w-8/12 rounded-md p-16 text-center">
                    <img
                            src="${pageContext.request.contextPath}/assets/engineer.svg"
                            alt="engineer"
                    />
                    <span class="text-3xl text-blue-900 font-mono font-semibold">Whoops, looks like you got an error</span>
                    <span class="text-4xl text-blue-100 font-mono underline font-bold px-2"> <%=response.getStatus() %></span>
                    <span class="text-3xl text-blue-900 font-mono font-semibold">! </span>
                    <br>
                    <span class="text-3xl text-blue-900 font-mono font-semibold"> Please try again in a few minutes.</span>
                </div>
            </c:otherwise>
        </c:choose>
    </div>
</div>
</body>
</html>
