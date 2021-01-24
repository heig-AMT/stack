package ch.heigvd.amt.stack.domain.gamification;

import ch.heigvd.amt.stack.domain.authentication.CredentialId;
import java.util.List;
import java.util.Optional;

public interface GamificationRepository {

  void saveEvent(CredentialId user, GamificationEvent event);

  List<GamificationBadge> findAllBadges(CredentialId user);

  List<GamificationRank> findAllRank(GamificationCategory category, Integer page, Integer size);

  Optional<GamificationRank> findRankByUser(GamificationCategory category, CredentialId userId);
}
