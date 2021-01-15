package ch.heigvd.amt.stack.ui.web.views.ranking;

import ch.heigvd.amt.stack.application.GamificationFacade;
import java.io.IOException;
import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "RankingView", urlPatterns = "/rankings")
public class RankingView extends HttpServlet {
  @Inject
  GamificationFacade gamificationFacade;

  @Override
  protected void doGet(
      HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    var username =req.getParameter("username");
    var page =req.getParameter("page");
    req.setAttribute("rankings", gamificationFacade.getCategoryRankings(username,
        req.getParameter("category"),
        (page.equals("null") ? -1 : Integer.parseInt(page))));
    req.getRequestDispatcher("WEB-INF/views/rankings.jsp").forward(req, resp);
  }
}
