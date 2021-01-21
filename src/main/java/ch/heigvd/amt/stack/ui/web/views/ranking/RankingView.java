package ch.heigvd.amt.stack.ui.web.views.ranking;

import ch.heigvd.amt.stack.application.GamificationFacade;
import ch.heigvd.amt.stack.application.rankings.query.Leaderboard;
import ch.heigvd.amt.stack.application.rankings.query.LeaderboardQuery;
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
      HttpServletRequest req,
      HttpServletResponse resp
  ) throws ServletException, IOException {
    var query = LeaderboardQuery.builder()
        .leaderboard(Leaderboard.Questions)
        // TODO (matthieu) : Change this
        .page(0)
        .tag(req.getSession().getId())
        .build();
    req.setAttribute("rankings", gamificationFacade.getLeaderboard(query));
    req.getRequestDispatcher("WEB-INF/views/rankings.jsp").forward(req, resp);
  }
}
