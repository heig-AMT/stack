package ch.heigvd.amt.mvcsimple.presentation.questions;

import ch.heigvd.amt.mvcsimple.Repositories;
import ch.heigvd.amt.mvcsimple.business.api.QuestionRepository;
import ch.heigvd.amt.mvcsimple.model.Question;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "QuestionListServlet", urlPatterns = "/questions")
public class QuestionListServlet extends HttpServlet {

    private QuestionRepository repository = Repositories.getInstance().getQuestionRepository();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<Question> questions = repository.all();
        req.setAttribute("questions", questions);
        req.getRequestDispatcher("WEB-INF/pages/questions.jsp").forward(req, resp);
    }
}
