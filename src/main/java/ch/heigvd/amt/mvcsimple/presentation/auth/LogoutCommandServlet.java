package ch.heigvd.amt.mvcsimple.presentation.auth;

import ch.heigvd.amt.mvcsimple.Repositories;
import ch.heigvd.amt.mvcsimple.business.api.SessionRepository;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "LogoutCommandServlet", urlPatterns = "/logout.do")
public class LogoutCommandServlet extends HttpServlet {

    SessionRepository sessionRepository = Repositories.getInstance().getSessionRepository();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Start by clearing the eventually set session.
        sessionRepository.unlink(req.getSession());
        String path = getServletContext().getContextPath() + "/";
        resp.sendRedirect(path);
    }
}
