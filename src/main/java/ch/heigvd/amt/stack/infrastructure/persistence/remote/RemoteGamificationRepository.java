package ch.heigvd.amt.stack.infrastructure.persistence.remote;

import ch.heigvd.amt.stack.domain.authentication.CredentialId;
import ch.heigvd.amt.stack.domain.gamification.GamificationBadge;
import ch.heigvd.amt.stack.domain.gamification.GamificationCategory;
import ch.heigvd.amt.stack.domain.gamification.GamificationEvent;
import ch.heigvd.amt.stack.domain.gamification.GamificationRank;
import ch.heigvd.amt.stack.domain.gamification.GamificationRepository;
import ch.heigvd.amt.stack.domain.gamification.GamificationRule;
import ch.heigvd.gamify.ApiException;
import ch.heigvd.gamify.Configuration;
import ch.heigvd.gamify.api.AggregatesApi;
import ch.heigvd.gamify.api.BadgesApi;
import ch.heigvd.gamify.api.CategoriesApi;
import ch.heigvd.gamify.api.EventsApi;
import ch.heigvd.gamify.api.RulesApi;
import ch.heigvd.gamify.api.dto.Badge;
import ch.heigvd.gamify.api.dto.Category;
import ch.heigvd.gamify.api.dto.Event;
import ch.heigvd.gamify.api.dto.Ranking;
import ch.heigvd.gamify.api.dto.Rule;
import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Default;

@ApplicationScoped
@Default
public class RemoteGamificationRepository implements GamificationRepository {

  private final EventsApi eventsApi = new EventsApi();
  private final CategoriesApi categoriesApi = new CategoriesApi();
  private final RulesApi rulesApi = new RulesApi();
  private final BadgesApi badgesApi = new BadgesApi();
  private final AggregatesApi aggregatesApi = new AggregatesApi();

  /**
   * {@inheritDoc}
   */
  @Override
  public void saveEvent(CredentialId user, GamificationEvent event) {
    try {
      var payload = new Event()
          .userId(user.toString())
          .timestamp(OffsetDateTime.now())
          .type(event.name());
      eventsApi.addEventAsync(payload, new ApiCallbackAdapter<>() {
      });
    } catch (ApiException exception) {
      exception.printStackTrace();
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<GamificationBadge> findAllBadges(CredentialId user) {
    try {
      var categories = Arrays.stream(GamificationCategory.values())
          .map(GamificationCategory::getName)
          .collect(Collectors.toList());

      return aggregatesApi.getUserAggregate(user.toString(), categories).stream()
          .flatMap(ranking -> ranking.getBadges().stream())
          .map(Badge::getName)
          .flatMap(name -> GamificationBadge.forName(name).stream())
          .distinct()
          .collect(Collectors.toList());

    } catch (ApiException apiException) {
      apiException.printStackTrace();
      return List.of();
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<GamificationRank> findAllRank(String categoryName, Integer page, Integer size) {
    try {
      return aggregatesApi.getLeaderboard(categoryName, page, size)
          .stream()
          .map(RemoteGamificationRepository::fromRanking)
          .flatMap(Optional::stream)
          .collect(Collectors.toList());
    } catch (ApiException apiException) {
      apiException.printStackTrace();
      return List.of();
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Optional<GamificationRank> findRankByUser(String categoryName, CredentialId userId) {
    // TODO : Use a more efficient query than this.
    return findAllRank(categoryName, null, null).stream()
        .filter(r -> r.getUser().equals(userId))
        .findFirst();
  }

  /**
   * Builds a new {@link RemoteGamificationRepository}. This starts by inserting all the categories,
   * rules and badges that are defined for our current application.
   */
  public RemoteGamificationRepository() {
    Configuration.getDefaultApiClient().setApiKey(System.getenv("GAMIFY_API_TOKEN"));
    Configuration.getDefaultApiClient().setBasePath(System.getenv("GAMIFY_SERVER"));

    Arrays.stream(GamificationCategory.values()).forEach(this::addCategory);
    Arrays.stream(GamificationRule.values()).forEach(this::addRule);
    Arrays.stream(GamificationBadge.values()).forEach(this::addBadge);
  }

  // Private utilities.

  private void addCategory(GamificationCategory category) {
    try {
      categoriesApi.putCategory(category.getName(), new Category()
          .name(category.getName())
          .description(category.getDescription())
          .title(category.getTitle())
      );
    } catch (ApiException e) {
      e.printStackTrace();
    }
  }

  private void addRule(GamificationRule newRule) {
    try {
      rulesApi.putRule(newRule.getName(), new Rule()
          .name(newRule.getName())
          .category(newRule.getCategory().getName())
          .event(newRule.getEvent().name())
          .points(newRule.getPoints())
      );
    } catch (ApiException e) {
      e.printStackTrace();
    }
  }

  private void addBadge(GamificationBadge newBadge) {
    try {
      badgesApi.putBadge(newBadge.getName(), new Badge()
          .name(newBadge.getName())
          .category(newBadge.getCategory().getName())
          .description(newBadge.getDescription())
          .title(newBadge.getTitle())
          .pointsLower(newBadge.getMinPoints().orElse(null))
          .pointsUpper(newBadge.getMaxPoints().orElse(null))
      );
    } catch (ApiException e) {
      e.printStackTrace();
    }
  }

  private static Optional<GamificationRank> fromRanking(Ranking dto) {

    var badges = Optional.ofNullable(dto.getBadges()).orElse(List.of())
        .stream()
        .map(Badge::getName)
        .map(GamificationBadge::forName)
        .flatMap(Optional::stream)
        .distinct()
        .collect(Collectors.toList());

    var category = GamificationCategory.forName(dto.getCategory());

    return category.map(cat ->
        GamificationRank.builder()
            .user(CredentialId.from(dto.getUserId()))
            .badges(badges)
            .category(cat)
            .points(dto.getPoints())
            .rank(dto.getRank())
            .build()
    );
  }
}
