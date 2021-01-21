package ch.heigvd.amt.stack.ui.web.views.ranking;

import ch.heigvd.amt.stack.application.rankings.query.Leaderboard;
import javax.servlet.annotation.WebServlet;

@WebServlet(name = "CommentsRankingView", urlPatterns = "/rankings/comments")
public class CommentsRankingView extends RankingView {

  /**
   * {@inheritDoc}
   */
  @Override
  protected Leaderboard getLeaderboard() {
    return Leaderboard.Comments;
  }
}
