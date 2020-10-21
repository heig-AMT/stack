package ch.heigvd.amt.stack.ui.web.endpoints.question;

import ch.heigvd.amt.stack.application.question.QuestionFacade;
import ch.heigvd.amt.stack.application.question.command.AskQuestionCommand;
import ch.heigvd.amt.stack.application.question.dto.QuestionIdDTO;
import ch.heigvd.amt.stack.domain.authentication.AuthenticationFailedException;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "AskCommandEndpoint", urlPatterns = "/ask.do")
public class AskCommandEndpoint extends HttpServlet {

    @Inject
    private QuestionFacade questionFacade;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        try {
            QuestionIdDTO questionIdDTO = questionFacade.askQuestion(AskQuestionCommand.builder()
                    .title(req.getParameter("title"))
                    .description(req.getParameter("description"))
                    .tag(req.getSession().getId())
                    .build());
            String path = getServletContext().getContextPath() + "/question?id=" + questionIdDTO.getId().toString();
            resp.sendRedirect(path);
        } catch (AuthenticationFailedException exception) {
            // Not matching username and password combination.
            resp.sendError(HttpServletResponse.SC_FORBIDDEN);
        }
    }
}
