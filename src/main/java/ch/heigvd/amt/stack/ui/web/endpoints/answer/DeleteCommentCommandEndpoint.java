package ch.heigvd.amt.stack.ui.web.endpoints.answer;

import ch.heigvd.amt.stack.application.AnswerFacade;
import ch.heigvd.amt.stack.application.answer.command.DeleteCommentCommand;
import ch.heigvd.amt.stack.domain.answer.AnswerNotFoundException;
import ch.heigvd.amt.stack.domain.authentication.AuthenticationFailedException;
import ch.heigvd.amt.stack.domain.comment.CommentId;
import ch.heigvd.amt.stack.domain.comment.CommentNotFoundException;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "DeleteCommentCommandEndpoint", urlPatterns = "/deleteComment.do")
public class DeleteCommentCommandEndpoint extends HttpServlet {

    @Inject
    private AnswerFacade answerFacade;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            CommentId commentId = CommentId.from(req.getParameter("comment"));


            answerFacade.deleteComment(DeleteCommentCommand.builder()
                    .comment(commentId)
                    .tag(req.getSession().getId())
                    .build());

                String path = getServletContext().getContextPath() + "/question?id=" + req.getParameter("question");
                resp.sendRedirect(path);

        } catch (AuthenticationFailedException exception) {
            // Not matching username and password combination.
            resp.sendError(HttpServletResponse.SC_FORBIDDEN);
        } catch (AnswerNotFoundException | CommentNotFoundException exception) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }
}
