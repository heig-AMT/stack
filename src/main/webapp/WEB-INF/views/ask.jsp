<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<jsp:include page="fragments/head.jsp">
    <jsp:param name="pageTitle" value="Ask question"/>
</jsp:include>
<body>
<jsp:include page="fragments/navigation.jsp"/>
    <div class="flex justify-center ">
        <form action="ask.do"
              method="POST"
              class="p-10 mt-4 bg-white max-w-xl w-full shadow-lg rounded-lg ">

            <h1 class="text-gray-800 font-bold text-center">Ask a question</h1>

            <div class="flex flex-col">
                <label for="title" class="text-gray-800 font-bold mb-2">Title</label>
                <input id="title" type="text" name="title" placeholder="Enter a title"
                       class="p-2 border border-gray-200 focus:border-gray-500"/>
            </div>

            <div class="flex flex-col text-sm">
                <label for="description" class="text-gray-800 font-bold mt-4 mb-2">Description</label>
                <textarea id="description" name="description" placeholder="Enter a description"
                          class="p-2 h-40 border border-gray-200 focus:border-gray-500"></textarea>
            </div>
            <input type="submit" value="Publish"
                   class="p-3 mt-4 block w-full bg-blue-600 text-gray-100 font-bold border border-gray-200 rounded-lg hover:bg-blue-500 focus:border-gray-500"/>
        </form>
    </div>
</body>
</html>
