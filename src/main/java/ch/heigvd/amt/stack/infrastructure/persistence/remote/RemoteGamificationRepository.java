package ch.heigvd.amt.stack.infrastructure.persistence.remote;

import ch.heigvd.amt.stack.domain.authentication.CredentialId;
import ch.heigvd.amt.stack.domain.gamification.GamificationEvent;
import ch.heigvd.amt.stack.domain.gamification.GamificationRepository;
import ch.heigvd.gamify.ApiException;
import ch.heigvd.gamify.api.EventsApi;
import ch.heigvd.gamify.api.dto.Event;
import java.time.OffsetDateTime;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Default;

@ApplicationScoped
@Default
public class RemoteGamificationRepository implements GamificationRepository {

  private final EventsApi eventsApi = new EventsApi();

  @Override
  public void post(CredentialId user, GamificationEvent event) {
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
}
