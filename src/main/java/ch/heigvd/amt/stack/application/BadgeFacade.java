package ch.heigvd.amt.stack.application;

import ch.heigvd.amt.stack.application.authentication.query.SessionQuery;
import ch.heigvd.amt.stack.application.badges.dto.BadgeListDTO;
import ch.heigvd.amt.stack.application.badges.query.BadgeQuery;
import ch.heigvd.amt.stack.domain.authentication.CredentialId;
import ch.heigvd.amt.stack.domain.authentication.Session;
import ch.heigvd.amt.stack.domain.authentication.SessionRepository;
import ch.heigvd.amt.stack.domain.gamification.GamificationRepository;
import ch.heigvd.gamify.api.dto.Badge;
import java.util.List;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

@RequestScoped
public class BadgeFacade {
  @Inject
  GamificationRepository gamificationRepository;
  @Inject
  SessionRepository sessionRepository;

  private CredentialId getCredential(String forTag) {
    return sessionRepository.findBy(SessionQuery.builder()
        .tag(forTag)
        .build())
        .map(Session::getUser)
        .orElse(null);
  }

  public BadgeListDTO getUserBadges(BadgeQuery query){
    List<Badge> badges=gamificationRepository.getBadges(getCredential(query.getUsername()));

    return BadgeListDTO.builder().badges(badges).build();
  }
}
