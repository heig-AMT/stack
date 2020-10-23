package ch.heigvd.amt.stack.ui.web.endpoints.answer;

import ch.heigvd.amt.stack.application.answer.AnswerFacade;
import ch.heigvd.amt.stack.application.answer.command.DeleteAnswerCommand;
import ch.heigvd.amt.stack.domain.answer.AnswerId;
import ch.heigvd.amt.stack.domain.answer.AnswerNotFoundException;
import ch.heigvd.amt.stack.domain.authentication.AuthenticationFailedException;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "DeleteAnswerCommandEndpoint", urlPatterns = "/deleteAnswer.do")
public class DeleteAnswerCommandEndpoint extends HttpServlet {

    @Inject
    private AnswerFacade answerFacade;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            answerFacade.delete(DeleteAnswerCommand.builder()
                    .answer(AnswerId.from(req.getParameter("answer")))
                    .tag(req.getSession().getId())
                    .build());
            String path = getServletContext().getContextPath() + "/questions";
            resp.sendRedirect(path);
        } catch (AuthenticationFailedException exception) {
            // Not matching username and password combination.
            resp.sendError(HttpServletResponse.SC_FORBIDDEN);
        } catch (AnswerNotFoundException e) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }
}
