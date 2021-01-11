package ch.heigvd.amt.stack.application.badges.dto;

import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class BadgeDTO {

  String name;
  String category;
  String title;
  String description;
  Integer pointsLower;
  Integer pointsUpper;
  String imageUrl;
}
