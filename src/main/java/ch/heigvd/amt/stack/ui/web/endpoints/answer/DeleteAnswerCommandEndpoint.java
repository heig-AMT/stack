package ch.heigvd.amt.stack.ui.web.endpoints.answer;

import ch.heigvd.amt.stack.application.AnswerFacade;
import ch.heigvd.amt.stack.application.QuestionFacade;
import ch.heigvd.amt.stack.application.answer.command.DeleteAnswerCommand;
import ch.heigvd.amt.stack.application.question.dto.QuestionDTO;
import ch.heigvd.amt.stack.application.question.query.SingleAnswerQuery;
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
import java.util.Optional;

@WebServlet(name = "DeleteAnswerCommandEndpoint", urlPatterns = "/deleteAnswer.do")
public class DeleteAnswerCommandEndpoint extends HttpServlet {

    @Inject
    private AnswerFacade answerFacade;

    @Inject
    private QuestionFacade questionFacade;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            AnswerId answerId = AnswerId.from(req.getParameter("answer"));

            Optional<QuestionDTO> question = questionFacade.getQuestion(SingleAnswerQuery.builder()
                    .id(answerId)
                    .build());

            if (question.isPresent()) {
                answerFacade.delete(DeleteAnswerCommand.builder()
                        .answer(answerId)
                        .tag(req.getSession().getId())
                        .build());

                String path = getServletContext().getContextPath() + "/question" + "?id=" + question.get().getId().toString();
                resp.sendRedirect(path);
            } else {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
        } catch (AuthenticationFailedException exception) {
            // Not matching username and password combination.
            resp.sendError(HttpServletResponse.SC_FORBIDDEN);
        } catch (AnswerNotFoundException e) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }
}
