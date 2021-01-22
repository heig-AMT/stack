package ch.heigvd.amt.stack.application.rankings.dto;

import ch.heigvd.amt.stack.application.rankings.query.Leaderboard;
import java.util.List;
import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class LeaderboardDTO {

  Leaderboard leaderboard;
  List<UserRankingDTO> rankings;

  // Pagination information.
  int page;
  boolean hasPreviousPage;
  boolean hasFollowingPage;
}
