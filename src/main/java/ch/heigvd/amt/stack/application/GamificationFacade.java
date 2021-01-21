package ch.heigvd.amt.stack.application;

import ch.heigvd.amt.stack.application.authentication.query.SessionQuery;
import ch.heigvd.amt.stack.application.badges.dto.BadgeDTO;
import ch.heigvd.amt.stack.application.badges.dto.BadgeListDTO;
import ch.heigvd.amt.stack.application.badges.query.BadgeQuery;
import ch.heigvd.amt.stack.application.rankings.dto.LeaderboardDTO;
import ch.heigvd.amt.stack.application.rankings.dto.UserRankingDTO;
import ch.heigvd.amt.stack.application.rankings.query.LeaderboardQuery;
import ch.heigvd.amt.stack.domain.authentication.AuthenticationFailedException;
import ch.heigvd.amt.stack.domain.authentication.CredentialId;
import ch.heigvd.amt.stack.domain.authentication.CredentialRepository;
import ch.heigvd.amt.stack.domain.authentication.Session;
import ch.heigvd.amt.stack.domain.authentication.SessionRepository;
import ch.heigvd.amt.stack.domain.gamification.GamificationCategory;
import ch.heigvd.amt.stack.domain.gamification.GamificationRepository;
import java.util.Comparator;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

@RequestScoped
public class GamificationFacade {

  @Inject
  GamificationRepository gamificationRepository;
  @Inject
  SessionRepository sessionRepository;
  @Inject
  CredentialRepository credentialRepository;

  private Optional<CredentialId> getCredential(String forTag) {
    return sessionRepository.findBy(SessionQuery.builder()
        .tag(forTag)
        .build())
        .map(Session::getUser);
  }

  public BadgeListDTO getUserBadges(BadgeQuery query) throws AuthenticationFailedException {

    var list = getCredential(query.getTag())
        .map(gamificationRepository::findAllBadges)
        .orElseThrow(AuthenticationFailedException::new)
        .stream()
        .map(badge -> BadgeDTO.builder()
            .name(badge.getName())
            .imageUrl(badge.getImageUrl())
            .description(badge.getDescription())
            .category(badge.getCategory().getName())
            .pointsLower(badge.getMinPoints().orElse(null))
            .pointsUpper(badge.getMaxPoints().orElse(null))
            .title(badge.getTitle())
            .build())
        .collect(Collectors.toList());

    return BadgeListDTO.builder()
        .badges(list)
        .build();
  }

  public LeaderboardDTO getLeaderboard(
      LeaderboardQuery query
  ) {
    final GamificationCategory category;
    switch (query.getLeaderboard()) {
      case Answers:
        // TODO : Change this.
        category = GamificationCategory.Questions;
        break;
      case Questions:
        category = GamificationCategory.Questions;
        break;
      case Comments:
        // TODO : Change this.
        category = GamificationCategory.Questions;
        break;
      default:
        throw new IllegalArgumentException("Missing leaderboard category.");
    }
    var ranks = gamificationRepository.findAllRank(category.getName(), query.getPage(), 25);
    var credential = getCredential(query.getTag());

    // Add the current user to the ranking, if any.
    credential
        .flatMap(cred -> gamificationRepository.findRankByUser(category.getName(), cred))
        .ifPresent(ranks::add);

    var ranking = ranks.stream()
        .flatMap(rank -> credentialRepository.findById(rank.getUser())
            .map(user -> UserRankingDTO.builder()
                .username(user.getUsername())
                .points(rank.getPoints())
                .rank(rank.getRank())
                .build()
            ).stream())
        .distinct()
        .sorted(Comparator.comparing(UserRankingDTO::getRank))
        .collect(Collectors.toList());

    return LeaderboardDTO.builder()
        .categoryName(category.getTitle())
        .page(query.getPage())
        .rankings(ranking)
        .build();
  }
}
