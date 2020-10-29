package ch.heigvd.amt.stack.ui.web.endpoints.answer;

import ch.heigvd.amt.stack.application.AnswerFacade;
import ch.heigvd.amt.stack.application.answer.command.SelectAnswerCommand;
import ch.heigvd.amt.stack.domain.answer.AnswerId;
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

@WebServlet(name = "SelectAnswerCommandEndpoint", urlPatterns = "/selectAnswer.do")
public class SelectAnswerCommandEndpoint extends HttpServlet {

    @Inject
    private AnswerFacade answerFacade;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        try {
            QuestionId questionId = QuestionId.from(req.getParameter("question"));
            AnswerId answerId = AnswerId.from(req.getParameter("answer"));


            answerFacade.select(SelectAnswerCommand.builder()
                    .forQuestion(questionId)
                    .answer(answerId)
                    .tag(req.getSession().getId())
                    .build());

            String path = getServletContext().getContextPath() + "/question" + "?id=" + questionId.toString();
            resp.sendRedirect(path);

        } catch (AuthenticationFailedException exception) {
            // Not matching username and password combination.
            resp.sendError(HttpServletResponse.SC_FORBIDDEN);
        } catch (QuestionNotFoundException exception) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }
}
