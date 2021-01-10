package ch.heigvd.amt.stack.application.rankings;

import java.util.List;
import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class RankingListDTO {
  List<RankingDTO> rankingDTOS;
}
