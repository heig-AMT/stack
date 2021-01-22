package ch.heigvd.amt.stack.domain.gamification;

import ch.heigvd.amt.stack.domain.authentication.CredentialId;
import java.util.List;
import lombok.Builder;
import lombok.Value;

/**
 * Representation of an user's rank in leaderboard of the gamification engine.
 */
@Builder
@Value
public class GamificationRank {

  CredentialId user;
  List<GamificationBadge> badges;
  GamificationCategory category;
  int rank;
  int points;
}
