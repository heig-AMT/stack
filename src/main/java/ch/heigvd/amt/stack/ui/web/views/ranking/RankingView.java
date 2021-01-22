package ch.heigvd.amt.stack.ui.web.views.ranking;

import ch.heigvd.amt.stack.application.GamificationFacade;
import ch.heigvd.amt.stack.application.rankings.query.Leaderboard;
import ch.heigvd.amt.stack.application.rankings.query.LeaderboardQuery;
import java.io.IOException;
import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * An abstract {@link RankingView} that displays the ranking.
 */
abstract class RankingView extends HttpServlet {

  @Inject
  GamificationFacade gamificationFacade;

  /**
   * {@inheritDoc}
   */
  @Override
  protected void doGet(
      HttpServletRequest req,
      HttpServletResponse resp
  ) throws ServletException, IOException {

    // Retrieve the current page number, if any.
    int page = 0;
    try {
      page = Integer.parseInt(req.getParameter("page"));
    } catch (Throwable ignored) {
      // Ignored.
    }

    var query = LeaderboardQuery.builder()
        .leaderboard(getLeaderboard())
        .page(page)
        .tag(req.getSession().getId())
        .build();
    var response = gamificationFacade.getLeaderboard(query);

    // Populate the Servlet.
    req.setAttribute("rankings", response);
    req.setAttribute("prevPageUrl", req.getServletPath() + "?page=" + (page - 1));
    req.setAttribute("nextPageUrl", req.getServletPath() + "?page=" + (page + 1));
    req.getServletContext().getRequestDispatcher("WEB-INF/views/rankings.jsp").forward(req, resp);
  }

  /**
   * Returns the {@link Leaderboard} that should be displayed.
   */
  protected abstract Leaderboard getLeaderboard();
}
