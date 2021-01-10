package ch.heigvd.amt.stack.application.rankings;

import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class RankingListDTO {
  List<RankingDTO> rankingDTOS=new ArrayList<>(List.of());

  public void add(RankingDTO rankingDTO){
    rankingDTOS.add(rankingDTO);
  }
}
