package ch.heigvd.amt.stack.application.rankings.dto;

import ch.heigvd.amt.stack.application.rankings.query.Leaderboard;
import java.util.List;
import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class LeaderboardDTO {

  @Deprecated // TODO (alex) : Remove this once the JSPs have been migrated properly.
  String categoryName;
  Leaderboard leaderboard;
  List<UserRankingDTO> rankings;

  // Pagination information.
  int page;
  boolean hasPreviousPage;
  boolean hasFollowingPage;
}
