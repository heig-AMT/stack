package ch.heigvd.amt.stack.application.badges.dto;

import java.util.List;
import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class BadgeListDTO {

  List<BadgeDTO> badges;
}
