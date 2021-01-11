package ch.heigvd.amt.stack.domain.gamification;

import ch.heigvd.amt.stack.domain.authentication.CredentialId;
import ch.heigvd.gamify.api.dto.Badge;
import ch.heigvd.gamify.api.dto.Category;
import ch.heigvd.gamify.api.dto.Ranking;
import java.util.List;

public interface GamificationRepository {

  void postEvent(CredentialId user, GamificationEvent event);
  List<Badge> getBadges(CredentialId user);
  List<Ranking> getRankings(String categoryName, int page, int size);
  List<Category> getCategories();
}
