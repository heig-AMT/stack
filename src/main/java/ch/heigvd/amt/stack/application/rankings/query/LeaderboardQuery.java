package ch.heigvd.amt.stack.application.rankings.query;

import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class LeaderboardQuery {

  String tag;
  Leaderboard leaderboard;

  int page;
}
