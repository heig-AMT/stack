package ch.heigvd.amt.stack.domain.gamification;

import ch.heigvd.amt.stack.domain.authentication.CredentialId;
import ch.heigvd.gamify.api.dto.Badge;
import java.util.List;

public interface GamificationRepository {

  void postEvent(CredentialId user, GamificationEvent event);
  List<Badge> getBadges(CredentialId user);
}
