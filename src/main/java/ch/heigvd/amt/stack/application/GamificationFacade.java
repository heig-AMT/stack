package ch.heigvd.amt.stack.application;

import ch.heigvd.amt.stack.application.authentication.query.SessionQuery;
import ch.heigvd.amt.stack.application.badges.dto.BadgeDTO;
import ch.heigvd.amt.stack.application.badges.dto.BadgeListDTO;
import ch.heigvd.amt.stack.application.badges.query.BadgeQuery;
import ch.heigvd.amt.stack.application.rankings.RankingDTO;
import ch.heigvd.amt.stack.application.rankings.SubRankingDTO;
import ch.heigvd.amt.stack.domain.authentication.CredentialId;
import ch.heigvd.amt.stack.domain.authentication.CredentialRepository;
import ch.heigvd.amt.stack.domain.authentication.Session;
import ch.heigvd.amt.stack.domain.authentication.SessionId;
import ch.heigvd.amt.stack.domain.authentication.SessionRepository;
import ch.heigvd.amt.stack.domain.gamification.GamificationRepository;
import ch.heigvd.gamify.api.dto.Badge;
import ch.heigvd.gamify.api.dto.Ranking;
import java.util.List;
import java.util.stream.Collectors;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import lombok.Getter;

@RequestScoped
public class GamificationFacade {

  @Inject
  GamificationRepository gamificationRepository;
  @Inject
  SessionRepository sessionRepository;
  @Inject
  CredentialRepository credentialRepository;

  @Getter
  private int lastRankingPage;

  private CredentialId getCredential(String forTag) {
    return sessionRepository.findBy(SessionQuery.builder()
        .tag(forTag)
        .build())
        .map(Session::getUser)
        .orElse(null);
  }

  public BadgeListDTO getUserBadges(BadgeQuery query) {
    List<Badge> badges = gamificationRepository.getBadges(getCredential(query.getUsername()));

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

  public RankingDTO getCategoryRankings(String categoryName, int page){
    List<Ranking> catR=gamificationRepository.getRankings(
        categoryName,page, 25);
    lastRankingPage=(Math.max(page, -1));
    return (
        RankingDTO.builder()
            .categoryName(categoryName)
            .rankings(
                catR.stream().map(subRank ->{
                  if(subRank.getRank()!=null
                      && subRank.getPoints()!=null
                      && subRank.getUserId()!=null){
                    return SubRankingDTO.builder()
                        .rank(subRank.getRank())
                        .username(
                            subRank.getUserId())
                            //credentialRepository.findById(sessionRepository.findById(SessionId.from(
                                //subRank.getUserId())).get().getUser()).get().getUsername()) //TODO fetch username
                        .points(subRank.getPoints()).build();}
                  else return SubRankingDTO.builder().build();
                }).collect(Collectors.toList())
            ).build()
    );
    }

  enum BadgesImagesUrl {
    QBadge1(
        "https://external-preview.redd.it/Qn69GDdv7jSIicPYwmHF0FWqDNvFBkpJ9_yW_dw_bkk.jpg?auto=webp&s=a80ec851a760e2980fc87eec529dfd452fcf55f6"),
    QBadge2(
        "https://preview.redd.it/3lakntnj85f41.jpg?width=497&format=pjpg&auto=webp&s=b6fe3116d52198c1b3a35f073f568e8b5d1f202a"),
    QBadge3(
        "https://preview.redd.it/oojwlz0zb2w31.png?width=298&format=png&auto=webp&s=4efe79f4e9395b9e2e41a2a7e4dda25ef792515b"),
    QBadge4(
        "https://preview.redd.it/eh6fafytbuy01.gif?format=png8&s=3554b6470c9b9192406e275a19e50dda102ac2dd"),
    ABadge1(
        "https://i.pinimg.com/originals/9a/29/86/9a29868ac1db20a3656a5412a61abc86.jpg"),
    ABadge2(
        "https://preview.redd.it/v9vnqdh5a0h31.png?width=298&format=png&auto=webp&s=a70f957661055a5be0b91652f86aef2e19e0c7c2"),
    ABadge3(
        "https://i.pinimg.com/originals/46/1d/79/461d79668f54a02da43f99dfcfc7e3be.jpg"),
    ABadge4(
        "https://i.pinimg.com/originals/dc/4c/0b/dc4c0b475f0167b4e1a08e75a56ba1fc.jpg"),
    CBadge1(
        "https://cdnb.artstation.com/p/assets/images/images/017/356/521/large/ala-kapustka-menagerie-keeper-2.jpg?1555622659"),
    CBadge2(
        "https://preview.redd.it/43f7hqcqqby41.jpg?width=497&format=pjpg&auto=webp&s=260d11526e15da89a1dbef13bf84334a0b77c484"),
    CBadge3(
        "https://i.pinimg.com/originals/bd/ac/44/bdac4462d60e295afe0545617c64767c.jpg"),
    CBadge4(
        "https://i.pinimg.com/originals/80/b1/1c/80b11c6c8caf7ba2221cbdd6fe7eb2fb.jpg");

    private final String url;

    BadgesImagesUrl(String url) {
      this.url = url;
    }
  }
}
