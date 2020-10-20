package ch.heigvd.amt.stack.ui.web.views.question;

import ch.heigvd.amt.stack.application.answer.AnswerFacade;
import ch.heigvd.amt.stack.application.answer.dto.AnswerListDTO;
import ch.heigvd.amt.stack.application.answer.query.AnswerQuery;
import ch.heigvd.amt.stack.domain.question.QuestionId;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "QuestionView", urlPatterns = "/question")
public class QuestionView extends HttpServlet {

    @Inject
    private AnswerFacade answerFacade;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        var builder = AnswerQuery.builder();
        var query = QuestionId.from(req.getQueryString());
        if (query != null) {
            builder.forQuestion(query);
        }
        AnswerListDTO answers =answerFacade.getAnswers(builder.build());
        req.setAttribute("answers", answers);
        req.getRequestDispatcher("WEB-INF/views/question.jsp").forward(req, resp);
    }
}
