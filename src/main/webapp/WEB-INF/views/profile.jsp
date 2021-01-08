<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:useBean scope="request" id="badges" type="ch.heigvd.amt.stack.application.badges.dto.BadgeListDTO"/>

<html>
<jsp:include page="fragments/head.jsp">
    <jsp:param name="pageTitle" value="Profile"/>
</jsp:include>
<body class="bg-gradient-to-r from-teal-400 to-blue-500">
<jsp:include page="fragments/navigation.jsp"/>
<div class="pt-16"/>
<div class="flex mt-8 items-center justify-center">
    <div class="p-6 max-w-xl w-full bg-white rounded-lg shadow-md">
        <!-- Profile title -->
        <div class="text-center">
            <h1 class="text-4xl">
                Your profile
            </h1>
        </div>

        <div class="flex items-center justify-center" >
            <c:forEach items="${badges.badges}" var="badge">
                <img class="h-24 rounded-lg border-solid border-4 border-red-600 mx-4"
                     src="<c:out value="${badge.imageUrl}"/>"
                     title="<c:out value="${badge.title}"/>" />
            </c:forEach>
        </div>

        <!-- Username section -->
        <div class="mt-8 border-t-2 color-white">
            <h1 class="text-2xl">
                Username
            </h1>
            <span class="text-sm">
                Here you can see your username, but not change it.
            </span>

            <div class="mt-4">
                <label for="username" class="block uppercase text-xs font-bold text-gray-700">Username</label>
                <span id="username" class="mt-2 p-3 block w-full bg-white text-gray-400 font-medium border border-gray-400 rounded-lg">
                    <c:out value="${connected.username}"/>
                </span>
            </div>
        </div>

        <!-- Password section -->
        <div class="mt-8 border-t-2 color-white">
            <h1 class="text-2xl">
                Password
            </h1>
            <span class="text-sm">
                Here you can change your password.
            </span>

            <form action="changePassword.do"
                  method="POST"
                  class="mt-4">
                <div class="flex flex-col">
                    <input type="hidden" name="username" value="<c:out value="${connected.username}"/>"/>
                    <div class="">
                        <label for="currentPassword" class="block uppercase text-xs font-bold text-gray-700">Current password</label>
                        <input id="currentPassword"
                               class="mt-2 p-3 block w-full bg-white text-gray-900 font-medium border border-gray-400 rounded-lg"
                               type="password" name="currentPassword" placeholder="Current password" required>
                    </div>

                    <div class="mt-4">
                        <label for="newPassword" class="block uppercase text-xs font-bold text-gray-700">New password</label>
                        <input id="newPassword"
                               class="mt-2 p-3 block w-full bg-white text-gray-900 font-medium border border-gray-400 rounded-lg"
                               type="password" name="newPassword" placeholder="New password" required>
                    </div>

                </div>
                <div class="mt-4 px-3">
                    <input class="p-3 block w-full bg-blue-600 text-gray-100 font-bold border border-gray-200 rounded-lg hover:bg-blue-500 focus:bg-blue-800 focus:border-gray-500"
                           type="submit" value="Change password"/>
                </div>
            </form>
        </div>
        <!-- Delete profile section -->
        <div class="mt-8 border-t-2 color-white">
            <h1 class="text-2xl">
                Delete account
            </h1>
            <span class="text-sm">
                Here you can delete your account. We're sad to see you leave, but you must have your reasons.
                Don't worry, we'll sell your data to the NSA.
            </span>
        </div>

        <form action="deleteAccount.do"
              method="POST"
              class="mt-4">
            <div class="flex flex-col">
                <input type="hidden" name="username" value="<c:out value="${connected.username}"/>"/>
                <div class="">
                    <label for="password" class="block uppercase text-xs font-bold text-gray-700">Confirm password</label>
                    <input id="password"
                           class="mt-2 p-3 block w-full bg-white text-gray-900 font-medium border border-gray-400 rounded-lg"
                           type="password" name="password" placeholder="Password" required>
                </div>
                <span class="px-3 pt-1 text-sm text-red-500">
                    Note: All your questions, answers, comments and votes will be deleted as well.
                </span>
            </div>

            <div class="mt-4 px-3">
                <input class="p-3 block w-full bg-red-600 text-white font-bold border border-gray-200 rounded-lg hover:bg-red-700 focus:bg-red-800 focus:border-gray-500"
                       type="submit" value="Delete account"/>
            </div>
        </form>
    </div>
</div>
</body>
</html>
