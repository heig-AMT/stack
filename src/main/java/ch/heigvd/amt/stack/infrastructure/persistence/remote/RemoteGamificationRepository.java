package ch.heigvd.amt.stack.infrastructure.persistence.remote;

import ch.heigvd.amt.stack.domain.authentication.CredentialId;
import ch.heigvd.amt.stack.domain.gamification.GamificationEvent;
import ch.heigvd.amt.stack.domain.gamification.GamificationRepository;
import ch.heigvd.gamify.ApiException;
import ch.heigvd.gamify.Configuration;
import ch.heigvd.gamify.api.CategoriesApi;
import ch.heigvd.gamify.api.EventsApi;
import ch.heigvd.gamify.api.RulesApi;
import ch.heigvd.gamify.api.dto.Category;
import ch.heigvd.gamify.api.dto.Event;
import ch.heigvd.gamify.api.dto.Rule;
import java.lang.reflect.InvocationTargetException;
import java.time.OffsetDateTime;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Default;

@ApplicationScoped
@Default
public class RemoteGamificationRepository implements GamificationRepository {

  private final EventsApi eventsApi = new EventsApi();
  private final CategoriesApi categoriesApi = new CategoriesApi();
  private final RulesApi rulesApi = new RulesApi();

  private RemoteGamificationRepository() {
    Configuration.getDefaultApiClient().setApiKey(("c5a76cf5-e618-4039-b484-96fe15f04414"));
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
}
