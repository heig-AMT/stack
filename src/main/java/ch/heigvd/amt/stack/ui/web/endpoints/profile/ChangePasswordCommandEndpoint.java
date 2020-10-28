package ch.heigvd.amt.stack.ui.web.endpoints.profile;

import ch.heigvd.amt.stack.application.AuthenticationFacade;
import ch.heigvd.amt.stack.application.authentication.command.ChangePasswordCommand;
import ch.heigvd.amt.stack.domain.authentication.AuthenticationFailedException;

import javax.inject.Inject;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "ChangePasswordCommandEndpoint", urlPatterns = "/changePassword.do")
public class ChangePasswordCommandEndpoint extends HttpServlet {

    @Inject
    private AuthenticationFacade authenticationFacade;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            authenticationFacade.changePassword(ChangePasswordCommand.builder()
                    .username(req.getParameter("username"))
                    .currentPassword(req.getParameter("currentPassword"))
                    .newPassword(req.getParameter("newPassword"))
                    .build());

            String redirect = getServletContext().getContextPath() + "/profile";
            resp.sendRedirect(redirect);
        } catch (AuthenticationFailedException exception) {
            // Not matching username and password combination.
            resp.sendError(HttpServletResponse.SC_FORBIDDEN);
        }
    }
}
