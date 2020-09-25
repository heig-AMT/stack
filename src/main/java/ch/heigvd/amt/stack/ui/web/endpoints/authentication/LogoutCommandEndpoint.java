package ch.heigvd.amt.stack.ui.web.endpoints.authentication;

import ch.heigvd.amt.stack.application.ServiceRegistry;
import ch.heigvd.amt.stack.application.authentication.AuthenticationFacade;
import ch.heigvd.amt.stack.application.authentication.command.LogoutCommand;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "LogoutCommandEndpoint", urlPatterns = "/logout.do")
public class LogoutCommandEndpoint extends HttpServlet {

    private AuthenticationFacade facade;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        facade = ServiceRegistry.getInstance().getAuthenticationFacade();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        // Start by clearing the eventually set session.
        facade.logout(LogoutCommand.builder().tag(req.getSession().getId()).build());
        String path = getServletContext().getContextPath() + "/";
        resp.sendRedirect(path);
    }
}
