package ch.heigvd.amt.stack.infrastructure.persistence.remote;

import ch.heigvd.amt.stack.domain.authentication.CredentialId;
import ch.heigvd.amt.stack.domain.gamification.GamificationEvent;
import ch.heigvd.amt.stack.domain.gamification.GamificationRepository;
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
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Default;
import lombok.Getter;
import lombok.Value;

@ApplicationScoped
@Default
public class RemoteGamificationRepository implements GamificationRepository {

  private final EventsApi eventsApi = new EventsApi();
  private final CategoriesApi categoriesApi = new CategoriesApi();
  private final RulesApi rulesApi = new RulesApi();
  private final BadgesApi badgesApi = new BadgesApi();
  private final AggregatesApi aggregatesApi = new AggregatesApi();
  @Getter
  private final List<Category> categories = new ArrayList<>(List.of(
      new Category().name("questions").title("Questions").description("Addition of new questions"),
      new Category().name("answers").title("Answers").description("Addition of new answers"),
      new Category().name("comments").title("Comments")
          .description("Addition of new comments and votes for them")));
  private final List<Rule> rules = new ArrayList<>(List.of(
      new Rule()
          .name("questionRule")
          .category("questions")
          .event(GamificationEvent.NEW_QUESTION.name())
          .points(10),
      new Rule()
          .name("answerRule")
          .category("answers")
          .event(GamificationEvent.NEW_ANSWER.name())
          .points(20),
      new Rule()
          .name("commentRule")
          .category("comments")
          .event(GamificationEvent.NEW_COMMENT.name())
          .points(25),
      new Rule()
          .name("upvoteRule")
          .category("comments")
          .event(GamificationEvent.UPVOTE.name())
          .points(10),
      new Rule()
          .name("downvoteRule")
          .category("comments")
          .event(GamificationEvent.DOWNVOTE.name())
          .points(0),
      new Rule()
          .name("acceptAnswerRule")
          .category("questions")
          .event(GamificationEvent.SELECTION.name())
          .points(5)));

  private final List<Badge> badges = new ArrayList<>(List.of(
      new Badge()
          .name("QBadge1")
          .category("questions")
          .title("Apprentice of questions")
          .description("First questions badge")
          .pointsLower(0)
          .pointsUpper(30),
      new Badge()
          .name("QBadge2")
          .category("questions")
          .title("Mage of questions")
          .description("Second questions badge")
          .pointsLower(29)
          .pointsUpper(100),
      new Badge()
          .name("QBadge3")
          .category("questions")
          .title("Grand mage of questions")
          .description("Third questions badge")
          .pointsLower(99)
          .pointsUpper(1000),
      new Badge()
          .name("QBadge4")
          .category("questions")
          .title("Very grand mage of questions")
          .description("Fourth questions badge")
          .pointsLower(999),
      
      new Badge()
          .name("ABadge1")
          .category("answers")
          .title("Apprentice of answers")
          .description("First answers badge")
          .pointsLower(0)
          .pointsUpper(30),
      new Badge()
          .name("ABadge2")
          .category("answers")
          .title("Maester of answers")
          .description("Second answers badge")
          .pointsLower(29)
          .pointsUpper(100),
      new Badge().name("ABadge3")
          .category("answers")
          .title("Archimaester of answers")
          .description("Third answers badge")
          .pointsLower(99)
          .pointsUpper(1000),
      new Badge()
          .name("ABadge4")
          .category("answers")
          .title("Grand maester of answers")
          .description("Fourth answers badge")
          .pointsLower(999),

      new Badge()
          .name("CBadge1")
          .category("comments")
          .title("Beast of comments")
          .description("First comments badge")
          .pointsLower(0)
          .pointsUpper(30),
      new Badge()
          .name("CBadge2")
          .category("comments")
          .title("Monster of comments")
          .description("Second comments badge")
          .pointsLower(29)
          .pointsUpper(100),
      new Badge()
          .name("CBadge3")
          .category("comments")
          .title("Big monster of comments")
          .description("Third comments badge")
          .pointsLower(99)
          .pointsUpper(1000),
      new Badge()
          .name("CBadge4")
          .category("comments")
          .title("Terrifying monster of comments")
          .description("Fourth comments badge")
          .pointsLower(999)
  ));

  private RemoteGamificationRepository() {
    Configuration.getDefaultApiClient().setApiKey(System.getenv("GAMIFY_API_TOKEN"));
    Configuration.getDefaultApiClient().setBasePath(System.getenv("GAMIFY_SERVER"));

    categories.forEach(this::addCategory);
    rules.forEach(this::addRule);
    badges.forEach(this::addBadge);
  }

  public void postEvent(CredentialId user, GamificationEvent event) {
    try {
      var payload = new Event()
          .userId(user.toString())
          .timestamp(OffsetDateTime.now())
          .type(event.name());
      eventsApi.addEvent(payload);
    } catch (ApiException exception) {
      exception.printStackTrace();
    }
  }

  public void addCategory(Category category) {
    try {
      categoriesApi.putCategory(category.getName(), category);
    } catch (ApiException e) {
      e.printStackTrace();
    }
  }

  public void addRule(Rule newRule) {
    try {
      rulesApi.postRule(newRule);
    } catch (ApiException e) {
      e.printStackTrace();
    }
  }

  public void addBadge(Badge newBadge) {
    try {
      badgesApi.putBadge(newBadge.getName(), newBadge);
    } catch (ApiException e) {
      e.printStackTrace();
    }
  }

  public List<Badge> getBadges(CredentialId user) {
    List<Badge> result = new ArrayList<>();
    try {
      var agg = aggregatesApi.getUserAggregate(
          user.toString(),
          categories.stream().map(Category::getName).collect(Collectors.toList())
      );
      for (var a : agg) {
        result.addAll(a.getBadges());
      }
    } catch (ApiException e) {
      e.printStackTrace();
    }
    return result;
  }

  public List<Ranking> getRankings(String categoryName){
    List<Ranking> result = new ArrayList<>(List.of());
      try {
        result=(aggregatesApi.getLeaderboard(categoryName,null,null));
      }catch (ApiException e){
        e.printStackTrace();
      }
    return result;
  }
}
