package ch.heigvd.amt.stack.application.rankings.dto;

import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class UserRankingDTO {

  String username;
  int rank;
  int points;
}
