package ch.heigvd.amt.mvcsimple.presentation.auth;

import ch.heigvd.amt.mvcsimple.business.api.CredentialRepository;

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
    CredentialRepository repository;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        final String username = req.getParameter("username");
        final String password = req.getParameter("password");
        if (!repository.registered(username)) {
            repository.set(username, password);
            String redirect = (String) req.getSession().getAttribute("redirectUrl");
            redirect = (redirect != null)
                    ? redirect
                    : getServletContext().getContextPath() + "/questions";
            resp.sendRedirect(redirect);
        } else {
            req.getRequestDispatcher(
                    getServletContext().getContextPath() + "/WEB/pages/login.jsp")
                    .forward(req, resp);
        }
    }
}
