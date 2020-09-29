package ch.heigvd.amt.stack.ui.web.views.question;

import ch.heigvd.amt.stack.application.ServiceRegistry;
import ch.heigvd.amt.stack.application.question.QuestionFacade;
import ch.heigvd.amt.stack.application.question.query.QuestionQuery;
import ch.heigvd.amt.stack.application.question.dto.QuestionListDTO;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "QuestionListView", urlPatterns = "/questions")
public class QuestionListView extends HttpServlet {

    private QuestionFacade facade;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        this.facade = ServiceRegistry.getInstance().getQuestionFacade();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        var builder = QuestionQuery.builder();
        var query = req.getParameter("search");
        if (query != null) {
            builder.shouldContain(query);
        }
        QuestionListDTO questions = facade.getQuestions(builder.build());
        req.setAttribute("questions", questions);
        req.getRequestDispatcher("WEB-INF/views/questions.jsp").forward(req, resp);
    }
}
