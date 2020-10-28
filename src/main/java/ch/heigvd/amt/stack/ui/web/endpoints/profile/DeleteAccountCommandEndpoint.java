package ch.heigvd.amt.stack.ui.web.endpoints.profile;

import ch.heigvd.amt.stack.application.authentication.AuthenticationFacade;
import ch.heigvd.amt.stack.application.authentication.command.ChangePasswordCommand;
import ch.heigvd.amt.stack.application.authentication.command.UnregisterCommand;
import ch.heigvd.amt.stack.domain.authentication.AuthenticationFailedException;

import javax.inject.Inject;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "DeleteAccountCommandEndpoint", urlPatterns = "/deleteAccount.do")
public class DeleteAccountCommandEndpoint extends HttpServlet {

    @Inject
    private AuthenticationFacade authenticationFacade;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            authenticationFacade.unregister(UnregisterCommand.builder()
                    .username(req.getParameter("username"))
                    .password(req.getParameter("password"))
                    .build());

            // TODO: choose where to redirect
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
