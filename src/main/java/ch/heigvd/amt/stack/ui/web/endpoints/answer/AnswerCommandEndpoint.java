package ch.heigvd.amt.stack.ui.web.endpoints.answer;

import ch.heigvd.amt.stack.application.answer.AnswerFacade;
import ch.heigvd.amt.stack.application.answer.command.AnswerQuestionCommand;
import ch.heigvd.amt.stack.domain.authentication.AuthenticationFailedException;
import ch.heigvd.amt.stack.domain.question.QuestionId;
import ch.heigvd.amt.stack.domain.question.QuestionNotFoundException;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "AnswerCommandEndpoint", urlPatterns = "/answer.do")
public class AnswerCommandEndpoint extends HttpServlet {

    @Inject
    private AnswerFacade answerFacade;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            answerFacade.answer(AnswerQuestionCommand.builder()
                    .question(QuestionId.from(req.getParameter("question")))
                    .body(req.getParameter("body"))
                    .tag(req.getSession().getId())
                    .build());
            // TODO: Make this more elegant if possible
            String path = getServletContext().getContextPath() + "/question?id=" + req.getParameter("question");
            resp.sendRedirect(path);
        } catch (AuthenticationFailedException exception) {
            // Not matching username and password combination.
            resp.sendError(HttpServletResponse.SC_FORBIDDEN);
        } catch (QuestionNotFoundException exception) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }
}
