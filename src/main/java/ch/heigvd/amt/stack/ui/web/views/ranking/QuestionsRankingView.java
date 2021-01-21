package ch.heigvd.amt.stack.ui.web.views.ranking;

import ch.heigvd.amt.stack.application.rankings.query.Leaderboard;
import javax.servlet.annotation.WebServlet;

@WebServlet(name = "QuestionsRankingView", urlPatterns = {"/rankings", "/rankings/questions"})
public class QuestionsRankingView extends RankingView {

  /**
   * {@inheritDoc}
   */
  @Override
  protected Leaderboard getLeaderboard() {
    return Leaderboard.Questions;
  }
}
