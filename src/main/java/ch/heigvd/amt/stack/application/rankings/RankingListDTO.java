package ch.heigvd.amt.stack.application.rankings;

import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import lombok.Singular;
import lombok.Value;

@Builder
@Value
public class RankingListDTO {

  @Singular
  List<RankingDTO> rankingDTOS = new ArrayList<>();
}
