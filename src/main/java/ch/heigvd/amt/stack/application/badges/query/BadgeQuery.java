package ch.heigvd.amt.stack.application.badges.query;

import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class BadgeQuery {
  String username;
}
