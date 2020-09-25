package ch.heigvd.amt.mvcsimple.presentation.auth;

import ch.heigvd.amt.stack.application.ServiceRegistry;
import ch.heigvd.amt.stack.application.authentication.AuthenticationFacade;
import ch.heigvd.amt.stack.application.authentication.command.LoginCommand;
import ch.heigvd.amt.stack.domain.authentication.AuthenticationFailedException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "LoginCommandServlet", urlPatterns = "/login.do")
public class LoginCommandServlet extends HttpServlet {

    private AuthenticationFacade authenticationFacade;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);

        this.authenticationFacade = ServiceRegistry.getInstance().getAuthenticationFacade();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            authenticationFacade.login(LoginCommand.builder()
                    .username(req.getParameter("username"))
                    .password(req.getParameter("password"))
                    .tag(req.getSession().getId())
                    .build());

            String redirect = (String) req.getSession().getAttribute("redirectUrl");
            redirect = (redirect != null)
                    ? redirect
                    : getServletContext().getContextPath() + "/questions";
            resp.sendRedirect(redirect);
        } catch (AuthenticationFailedException exception) {
            // Not matching username and password combination.
            resp.sendError(HttpServletResponse.SC_FORBIDDEN);
        }
    }
}
