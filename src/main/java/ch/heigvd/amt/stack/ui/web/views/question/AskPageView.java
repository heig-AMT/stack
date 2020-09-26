package ch.heigvd.amt.stack.ui.web.views.question;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "AskPageView", urlPatterns = "/ask")
public class AskPageView extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // We do not alter the page for the moment.
        req.getRequestDispatcher("WEB-INF/pages/ask.jsp").forward(req, resp);
    }
}
