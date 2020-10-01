<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<jsp:include page="fragments/head.jsp">
    <jsp:param name="pageTitle" value="Ask question"/>
</jsp:include>
<body>
<jsp:include page="fragments/navigation.jsp"/>
<div class="flex pt-16 h-screen
            items-center justify-center
            bg-gradient-to-r from-teal-400 to-blue-500">
    <div class="p-6 max-w-xl w-full bg-white rounded-lg shadow-md">
        <h1 class="text-center text-4xl">
            Ask a question
        </h1>
        <div class="flex justify-center">
            <form action="ask.do"
                  method="POST"
                  class="w-full">
                <div class="flex flex-col -mx-3 mb-6">
                    <div class="px-3 mb-6">
                        <label for="title" class="mb-2 block uppercase text-xs font-bold text-gray-700">Title</label>
                        <input id="title" class="p-3 block w-full bg-white text-gray-900 font-medium border border-gray-400 rounded-lg"
                               type="text" name="title" placeholder="Enter a title" required>
                    </div>

                    <div class="md:w-full px-3">
                        <label for="description" class="mb-2 block uppercase text-xs font-bold text-gray-700">Description</label>
                        <textarea rows="6" id="description" class="p-3 block w-full bg-white text-gray-900 font-medium border border-gray-400 rounded-lg"
                                  type="text" name="description" placeholder="Enter a description" required></textarea>
                    </div>

                </div>
                <div class="w-full px-3">
                    <input class="p-3 block w-full bg-blue-600 text-gray-100 font-bold border border-gray-200 rounded-lg hover:bg-blue-500 focus:bg-white focus:border-gray-500"
                           type="submit" value="Publish question"/>
                </div>
            </form>
        </div>
    </div>
</div>
</body>
</html>
