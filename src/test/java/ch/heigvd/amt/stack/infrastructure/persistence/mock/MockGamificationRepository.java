package ch.heigvd.amt.stack.infrastructure.persistence.mock;

import ch.heigvd.amt.stack.domain.authentication.CredentialId;
import ch.heigvd.amt.stack.domain.gamification.GamificationEvent;
import ch.heigvd.amt.stack.domain.gamification.GamificationRepository;
import ch.heigvd.gamify.api.dto.Badge;
import ch.heigvd.gamify.api.dto.Ranking;
import java.util.List;
import java.util.Optional;

/**
 * An implementation of a {@link GamificationRepository} that's empty. It is used to provide mocks
 * in the tests of the application.
 */
public class MockGamificationRepository implements GamificationRepository {

  @Override
  public void postEvent(CredentialId user, GamificationEvent event) {

  }

  @Override
  public List<Badge> getBadges(CredentialId user) {
    return List.of();
  }

  @Override
  public List<Ranking> getRankings(String categoryName, int page, int size) {
    return List.of();
  }

  @Override
  public Optional<Ranking> getOneUserRanking(CredentialId userId, String categoryName) {
    return Optional.empty();
  }
}
