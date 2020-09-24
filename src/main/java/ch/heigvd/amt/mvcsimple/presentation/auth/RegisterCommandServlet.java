package ch.heigvd.amt.mvcsimple.presentation.auth;

import ch.heigvd.amt.mvcsimple.business.api.CredentialRepository;
import ch.heigvd.amt.mvcsimple.business.api.SessionRepository;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "RegisterCommandServlet", urlPatterns = "/register.do")
public class RegisterCommandServlet extends HttpServlet {

    @EJB
    CredentialRepository credentialRepository;

    @EJB
    SessionRepository sessionRepository;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        final String username = req.getParameter("username");
        final String password = req.getParameter("password");

        // Start by clearing the eventually set session.
        sessionRepository.unlink(req.getSession());

        if (!credentialRepository.registered(username)) {

            // We can add this user in our store, as well as link its identifier to our session store.
            credentialRepository.set(username, password);
            sessionRepository.link(req.getSession(), username);
            String redirect = (String) req.getSession().getAttribute("redirectUrl");
            redirect = (redirect != null)
                    ? redirect
                    : getServletContext().getContextPath() + "/questions";
            resp.sendRedirect(redirect);
        } else {
            resp.sendRedirect(getServletContext().getContextPath() + "/login");
        }
    }
}
