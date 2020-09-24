package ch.heigvd.amt.mvcsimple.presentation.auth;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "RegisterPageServlet", urlPatterns = "/register")
public class RegisterPageServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // We do not alter the page for the moment.
        req.getRequestDispatcher("WEB-INF/pages/register.jsp").forward(req, resp);
    }
}
