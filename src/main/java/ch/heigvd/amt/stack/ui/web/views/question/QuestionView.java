package ch.heigvd.amt.stack.ui.web.views.question;

import ch.heigvd.amt.stack.application.AnswerFacade;
import ch.heigvd.amt.stack.application.answer.dto.AnswerListDTO;
import ch.heigvd.amt.stack.application.answer.query.AnswerQuery;
import ch.heigvd.amt.stack.application.QuestionFacade;
import ch.heigvd.amt.stack.application.question.dto.QuestionDTO;
import ch.heigvd.amt.stack.application.question.query.SingleQuestionQuery;
import ch.heigvd.amt.stack.domain.question.QuestionId;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

@WebServlet(name = "QuestionView", urlPatterns = "/question")
public class QuestionView extends HttpServlet {

    @Inject
    private AnswerFacade answerFacade;

    @Inject
    private QuestionFacade questionFacade;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        var questionId = QuestionId.from(req.getParameter("id"));
        var questionQueryBuilder = SingleQuestionQuery.builder();

        questionQueryBuilder.id(questionId);

        var built = questionQueryBuilder.build();

        Optional<QuestionDTO> question = questionFacade.getQuestion(built);

        if (question.isPresent()) {
            req.setAttribute("question", question.get());

            var answerQueryBuilder = AnswerQuery.builder();

            answerQueryBuilder.forQuestion(questionId);
            answerQueryBuilder.tag(req.getSession().getId());

            AnswerListDTO answers = answerFacade.getAnswers(answerQueryBuilder.build());
            req.setAttribute("answers", answers);

            req.getRequestDispatcher("WEB-INF/views/question.jsp").forward(req, resp);
        } else {
            req.getRequestDispatcher("WEB-INF/views/questions.jsp").forward(req, resp);
        }
    }
}