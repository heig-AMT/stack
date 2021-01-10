package ch.heigvd.amt.stack.application.rankings;

import ch.heigvd.gamify.api.dto.Ranking;
import java.util.List;
import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class RankingDTO {
  List<Ranking> rankings;
}
