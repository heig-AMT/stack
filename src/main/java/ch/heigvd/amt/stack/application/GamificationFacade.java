package ch.heigvd.amt.stack.application;

import ch.heigvd.amt.stack.application.authentication.query.SessionQuery;
import ch.heigvd.amt.stack.application.badges.dto.BadgeDTO;
import ch.heigvd.amt.stack.application.badges.dto.BadgeListDTO;
import ch.heigvd.amt.stack.application.badges.query.BadgeQuery;
import ch.heigvd.amt.stack.domain.authentication.CredentialId;
import ch.heigvd.amt.stack.domain.authentication.Session;
import ch.heigvd.amt.stack.domain.authentication.SessionRepository;
import ch.heigvd.amt.stack.domain.gamification.GamificationRepository;
import ch.heigvd.gamify.api.dto.Badge;
import java.util.List;
import java.util.stream.Collectors;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

@RequestScoped
public class GamificationFacade {
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

    return BadgeListDTO.builder().badges(badges.stream().map(
        badge -> BadgeDTO.builder()
        .name(badge.getName())
        .category(badge.getCategory())
        .title(badge.getTitle())
        .description(badge.getDescription())
        .pointsLower(badge.getPointsLower())
        .pointsUpper(badge.getPointsUpper())
        .imageUrl(BadgesImagesUrl.valueOf(badge.getName()).url).build()
    ).collect(Collectors.toList())).build();
  }

  enum BadgesImagesUrl{
    QBadge1("https://external-preview.redd.it/Qn69GDdv7jSIicPYwmHF0FWqDNvFBkpJ9_yW_dw_bkk.jpg?auto=webp&s=a80ec851a760e2980fc87eec529dfd452fcf55f6"),
    QBadge2("https://i.pinimg.com/originals/a5/60/a6/a560a6474795078925dd428f81591606.jpg"),
    QBadge3("https://static.wikia.nocookie.net/witcher/images/7/72/Gwent_cardart_skellige_coral.jpg/revision/latest?cb=20180608203051"),
    QBadge4("https://i.pinimg.com/originals/c4/ea/81/c4ea810b5b83b2b5c1f553541c05f5c1.jpg"),
    ABadge1("https://i.pinimg.com/originals/9a/29/86/9a29868ac1db20a3656a5412a61abc86.jpg"),
    ABadge2("https://preview.redd.it/v9vnqdh5a0h31.png?width=298&format=png&auto=webp&s=a70f957661055a5be0b91652f86aef2e19e0c7c2"),
    ABadge3("https://i.pinimg.com/originals/46/1d/79/461d79668f54a02da43f99dfcfc7e3be.jpg"),
    ABadge4("https://i.pinimg.com/originals/dc/4c/0b/dc4c0b475f0167b4e1a08e75a56ba1fc.jpg");

    private String url;
    BadgesImagesUrl(String url) {this.url=url;}
  }
}
