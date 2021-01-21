package ch.heigvd.amt.stack.application.rankings.dto;

import java.util.List;
import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class LeaderboardDTO {

  String categoryName;
  List<UserRankingDTO> rankings;

  // Pagination information.
  int page;
}
