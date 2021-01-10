package ch.heigvd.amt.stack.application.rankings;

import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class SubRankingDTO {
  int rank;
  String username;
  int points;
}
