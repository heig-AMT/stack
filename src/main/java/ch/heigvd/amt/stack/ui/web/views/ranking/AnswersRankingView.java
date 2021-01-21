package ch.heigvd.amt.stack.ui.web.views.ranking;

import ch.heigvd.amt.stack.application.rankings.query.Leaderboard;
import javax.servlet.annotation.WebServlet;

@WebServlet(name = "AnswersRankingView", urlPatterns = "/rankings/answers")
public class AnswersRankingView extends RankingView {

  /**
   * {@inheritDoc}
   */
  @Override
  protected Leaderboard getLeaderboard() {
    return Leaderboard.Answers;
  }
}
