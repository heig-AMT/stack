package ch.heigvd.amt.stack.ui.web.views.profile;

import ch.heigvd.amt.stack.application.authentication.AuthenticationFacade;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "ProfilePageView", urlPatterns = "/profile")
public class ProfilePageView extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // We do not alter the page for the moment.
        req.getRequestDispatcher("WEB-INF/views/profile.jsp").forward(req, resp);
    }
}
