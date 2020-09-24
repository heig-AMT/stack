package ch.heigvd.amt.mvcsimple.presentation.questions;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "AskPageServlet", urlPatterns = "/ask")
public class AskPageServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // We do not alter the page for the moment.
        req.getRequestDispatcher("WEB-INF/pages/ask.jsp").forward(req, resp);
    }
}
