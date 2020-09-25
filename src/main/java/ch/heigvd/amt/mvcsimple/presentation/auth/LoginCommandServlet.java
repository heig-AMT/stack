package ch.heigvd.amt.mvcsimple.presentation.auth;

import ch.heigvd.amt.mvcsimple.Repositories;
import ch.heigvd.amt.mvcsimple.business.api.CredentialRepository;
import ch.heigvd.amt.mvcsimple.business.api.SessionRepository;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "LoginCommandServlet", urlPatterns = "/login.do")
public class LoginCommandServlet extends HttpServlet {

    CredentialRepository credentialRepository = Repositories.getInstance().getCredentialRepository();

    SessionRepository sessionRepository = Repositories.getInstance().getSessionRepository();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        final String username = req.getParameter("username");
        final String password = req.getParameter("password");

        // Start by clearing the eventually set session.
        sessionRepository.unlink(req.getSession());

        if (credentialRepository.match(username, password)) {
            sessionRepository.link(req.getSession(), username);
            String redirect = (String) req.getSession().getAttribute("redirectUrl");
            redirect = (redirect != null)
                    ? redirect
                    : getServletContext().getContextPath() + "/questions";
            resp.sendRedirect(redirect);
        } else {
            // Not matching username and password combination.
            resp.sendError(HttpServletResponse.SC_FORBIDDEN);
        }
    }
}
