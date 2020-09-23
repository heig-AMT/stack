package ch.heigvd.amt.mvcsimple.presentation;

import ch.heigvd.amt.mvcsimple.business.QuestionRepository;
import ch.heigvd.amt.mvcsimple.model.Question;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "QuestionListServlet", urlPatterns = "/questions")
public class QuestionListServlet extends HttpServlet {

    private QuestionRepository repository;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        this.repository = new QuestionRepository();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<Question> questions = repository.all();
        req.setAttribute("questions", questions);
        req.getRequestDispatcher("WEB-INF/pages/questions.jsp").forward(req, resp);
    }
}
