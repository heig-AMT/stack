package ch.heigvd.amt.stack.ui.web.endpoints.question;

import ch.heigvd.amt.stack.application.QuestionFacade;
import ch.heigvd.amt.stack.application.question.command.DeleteQuestionCommand;
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

@WebServlet(name = "DeleteQuestionCommandEndpoint", urlPatterns = "/deleteQuestion.do")
public class DeleteQuestionCommandEndpoint extends HttpServlet {

    @Inject
    private QuestionFacade questionFacade;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            QuestionId questionId = QuestionId.from(req.getParameter("question"));

            questionFacade.deleteQuestion(DeleteQuestionCommand.builder()
                    .question(questionId)
                    .tag(req.getSession().getId())
                    .build());

            String path = getServletContext().getContextPath() + "/questions";
            resp.sendRedirect(path);

        } catch (AuthenticationFailedException exception) {
            // Not matching username and password combination.
            resp.sendError(HttpServletResponse.SC_FORBIDDEN);
        } catch (QuestionNotFoundException e) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }
}
