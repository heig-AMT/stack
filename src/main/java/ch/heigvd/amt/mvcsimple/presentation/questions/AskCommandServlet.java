package ch.heigvd.amt.mvcsimple.presentation.questions;

import ch.heigvd.amt.mvcsimple.Repositories;
import ch.heigvd.amt.mvcsimple.business.api.QuestionRepository;
import ch.heigvd.amt.mvcsimple.business.api.SessionRepository;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

@WebServlet(name = "AskCommandServlet", urlPatterns = "/ask.do")
public class AskCommandServlet extends HttpServlet {

    QuestionRepository questionRepository = Repositories.getInstance().getQuestionRepository();

    SessionRepository sessionRepository = Repositories.getInstance().getSessionRepository();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        final String title = req.getParameter("title");
        final String description = req.getParameter("description");

        final Optional<String> username = sessionRepository.username(req.getSession());

        if (username.isPresent()) {
            questionRepository.insert(username.get(), title, description);
            String path = getServletContext().getContextPath() + "/questions";
            resp.sendRedirect(path);
        } else {
            // Not matching username and password combination.
            resp.sendError(HttpServletResponse.SC_FORBIDDEN);
        }
    }
}
