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
import ch.heigvd.gamify.api.dto.Rule;
import java.lang.reflect.InvocationTargetException;
import java.time.OffsetDateTime;
import java.util.List;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Default;

@ApplicationScoped
@Default
public class RemoteGamificationRepository implements GamificationRepository {

  private final EventsApi eventsApi = new EventsApi();
  private final CategoriesApi categoriesApi = new CategoriesApi();
  private final RulesApi rulesApi = new RulesApi();
  private final BadgesApi badgesApi=new BadgesApi();
  private final AggregatesApi aggregatesApi=new AggregatesApi();

  private RemoteGamificationRepository() {
    Configuration.getDefaultApiClient().setApiKey(("e48338c8-532c-4225-b63f-affdc074ec67"));
    Configuration.getDefaultApiClient().setBasePath("http://localhost:8080");

    this.addCategory("questions", "Questions", "Addition of new questions");
    this.addCategory("answers", "Answers", "Addition of new answers");
    this.addCategory("comments", "Comments", "Addition of new comments and votes for them");

    this.addRule("questionRule", "questions", GamificationEvent.NEW_QUESTION, 10);
    this.addRule("answerRule", "answers", GamificationEvent.NEW_ANSWER, 30);
    this.addRule("commentRule", "comments", GamificationEvent.NEW_COMMENT, 20);
    this.addRule("upvoteRule", "comments", GamificationEvent.UPVOTE, 10);
    this.addRule("downvoteRule", "comments", GamificationEvent.DOWNVOTE, 0);
    this.addRule("acceptAnswerRule", "answers", GamificationEvent.SELECTION, 25);

    this.addBadge("QBadge1", "questions", "Apprentice of questions",
        "First questions badge", 0, 29);
    this.addBadge("QBadge2", "questions", "Wizard of questions",
        "Second questions badge", 30, 100 );
    this.addBadge("QBadge3", "questions", "Very grand wizard of questions",
        "Third questions badge", 101, 1000);
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

  public void addCategory(String name, String title, String description) {
    Category newCategory = new Category()
        .name(name).title(title).description(description);
    try {
      categoriesApi.putCategory(name, newCategory);
    } catch (ApiException e) {
      e.printStackTrace();
    }
  }

  public void addRule(String name, String categoryName, GamificationEvent eventType,
      Integer points) {
    Rule newRule = new Rule()
        .name(name).category(categoryName)
        .event(eventType.name()).points(points);
    try {
      rulesApi.postRule(newRule);
    } catch (ApiException e) {
      e.printStackTrace();
    }
  }

  public void addBadge(String name, String categoryName, String title,
      String description, int pointsLower, int pointsUpper){
    Badge newBadge= new Badge()
        .name(name).category(categoryName).title(title).description(description)
        .pointsLower(pointsLower).pointsUpper(pointsUpper);
    try {
      badgesApi.putBadge(name, newBadge);
    } catch (ApiException e) {
      e.printStackTrace();
    }
  }

  public List<Badge> getBadges(CredentialId user){
    List<Badge> result= new java.util.ArrayList<>(List.of());
    try {
      var agg=aggregatesApi.getUserAggregate(user.toString(), List.of("questions"));
      for (var a : agg){
        assert a.getBadges() != null;
        result.addAll(a.getBadges());
      }
    } catch (ApiException e) {
      e.printStackTrace();
    }
    return result;
  }
}
