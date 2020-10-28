package ch.heigvd.amt.stack.ui.web.endpoints.authentication;

import ch.heigvd.amt.stack.application.AuthenticationFacade;
import ch.heigvd.amt.stack.application.authentication.command.LogoutCommand;

import javax.inject.Inject;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "LogoutCommandEndpoint", urlPatterns = "/logout.do")
public class LogoutCommandEndpoint extends HttpServlet {

    @Inject
    private AuthenticationFacade facade;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        // Start by clearing the eventually set session.
        facade.logout(LogoutCommand.builder().tag(req.getSession().getId()).build());
        String path = getServletContext().getContextPath() + "/";
        resp.sendRedirect(path);
    }
}
