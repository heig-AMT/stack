<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<jsp:include page="fragments/header.jsp">
    <jsp:param name="pageTitle" value="Login"/>
</jsp:include>
<body>
<jsp:include page="fragments/navigation.jsp"/>
<div class="text-center mt-32">
    <h1 class="text-4xl">
        Login to your account
    </h1>
    <span class="text-sm">
        or
        <a href="${pageContext.request.contextPath}/register" class="text-blue-500">
            register an account
        </a>
   </span>
</div>
<div class="flex justify-center">
    <form action="login.do"
          method="POST"
          class="max-w-xl w-full bg-white rounded-lg shadow-md p-6">
        <div class="flex flex-col -mx-3 mb-6">
            <div class="w-full px-3 mb-6">
                <label for="username" class="mb-2 block uppercase text-xs font-bold text-gray-700">Username</label>
                <input id="username" class="p-3 block w-full bg-white text-gray-900 font-medium border border-gray-400 rounded-lg"
                       type="text" name="username" placeholder="xX-MyKickAssName-Xx" required>
            </div>

            <div class="w-full md:w-full px-3 mb-6">
                <label for="password" class="mb-2 block uppercase text-xs font-bold text-gray-700">Password</label>
                <input id="password" class="p-3 block w-full bg-white text-gray-900 font-medium border border-gray-400 rounded-lg"
                       type="password" name="password" placeholder="L33TSUPAH4X0R" required>
            </div>

        </div>
        <div class="w-full px-3 mb-6">
            <input class="p-3 block w-full bg-blue-600 text-gray-100 font-bold border border-gray-200 rounded-lg hover:bg-blue-500 focus:bg-white focus:border-gray-500"
                   type="submit" value="Login"/>
        </div>
    </form>
</div>
</body>
</html>
