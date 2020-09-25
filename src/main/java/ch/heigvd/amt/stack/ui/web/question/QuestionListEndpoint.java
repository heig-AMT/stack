package ch.heigvd.amt.stack.ui.web.question;

import ch.heigvd.amt.stack.application.ServiceRegistry;
import ch.heigvd.amt.stack.application.question.QuestionFacade;
import ch.heigvd.amt.stack.application.question.QuestionQuery;
import ch.heigvd.amt.stack.application.question.dto.QuestionListDTO;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "QuestionListEndpoint", urlPatterns = "/questions")
public class QuestionListEndpoint extends HttpServlet {

    private QuestionFacade facade;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        this.facade = ServiceRegistry.getInstance().getQuestionFacade();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        QuestionListDTO questions = facade.getQuestions(new QuestionQuery());
        req.setAttribute("questions", questions);
        req.getRequestDispatcher("WEB-INF/pages/questions.jsp").forward(req, resp);
    }
}
