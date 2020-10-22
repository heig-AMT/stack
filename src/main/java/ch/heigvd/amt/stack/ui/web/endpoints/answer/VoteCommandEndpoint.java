package ch.heigvd.amt.stack.ui.web.endpoints.answer;

import ch.heigvd.amt.stack.application.answer.AnswerFacade;
import ch.heigvd.amt.stack.application.answer.command.AnswerQuestionCommand;
import ch.heigvd.amt.stack.application.answer.command.DownvoteAnswerCommand;
import ch.heigvd.amt.stack.application.answer.command.UpvoteAnswerCommand;
import ch.heigvd.amt.stack.application.question.QuestionFacade;
import ch.heigvd.amt.stack.application.question.command.AskQuestionCommand;
import ch.heigvd.amt.stack.application.question.query.SingleAnswerQuery;
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

@WebServlet(name = "VoteCommandEndpoint", urlPatterns = "/vote.do")
public class VoteCommandEndpoint extends HttpServlet {

    @Inject
    private AnswerFacade answerFacade;

    @Inject
    private QuestionFacade questionFacade;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            switch(req.getParameter("type")) {
                case "upvote":
                    answerFacade.upvote(UpvoteAnswerCommand.builder()
                            .answer(AnswerId.from(req.getParameter("answer")))
                            .tag(req.getSession().getId())
                            .build());
                    break;
                case "downvote":
                    answerFacade.downvote(DownvoteAnswerCommand.builder()
                            .answer(AnswerId.from(req.getParameter("answer")))
                            .tag(req.getSession().getId())
                            .build());
                    break;
                default:
                    resp.sendError(HttpServletResponse.SC_NOT_FOUND);
                    break;
            }

            var questionDTO = questionFacade.getQuestion(SingleAnswerQuery.builder()
                    .id(AnswerId.from(req.getParameter("answer")))
                    .build());

            if (questionDTO.isPresent()) {
                String path = getServletContext().getContextPath() + "/question?id=" + questionDTO.get().getId().toString();
                resp.sendRedirect(path);
            } else {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            }

        } catch (AuthenticationFailedException exception) {
            // Not matching username and password combination.
            resp.sendError(HttpServletResponse.SC_FORBIDDEN);
        }
    }
}
