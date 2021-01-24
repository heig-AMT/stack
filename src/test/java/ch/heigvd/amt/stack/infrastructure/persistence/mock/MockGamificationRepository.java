package ch.heigvd.amt.stack.infrastructure.persistence.mock;

import ch.heigvd.amt.stack.domain.authentication.CredentialId;
import ch.heigvd.amt.stack.domain.gamification.GamificationBadge;
import ch.heigvd.amt.stack.domain.gamification.GamificationCategory;
import ch.heigvd.amt.stack.domain.gamification.GamificationEvent;
import ch.heigvd.amt.stack.domain.gamification.GamificationRank;
import ch.heigvd.amt.stack.domain.gamification.GamificationRepository;
import java.util.List;
import java.util.Optional;
import javax.enterprise.inject.Alternative;

/**
 * An implementation of a {@link GamificationRepository} that's empty. It is used to provide mocks
 * in the tests of the application.
 */
@Alternative
public class MockGamificationRepository implements GamificationRepository {

  /**
   * {@inheritDoc}
   */
  @Override
  public void saveEvent(CredentialId user, GamificationEvent event) {
    // Ignored.
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<GamificationBadge> findAllBadges(CredentialId user) {
    return List.of();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<GamificationRank> findAllRank(GamificationCategory c, Integer page, Integer size) {
    return List.of();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Optional<GamificationRank> findRankByUser(GamificationCategory c, CredentialId userId) {
    return Optional.empty();
  }
}
