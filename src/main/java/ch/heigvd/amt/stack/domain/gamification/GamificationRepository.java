package ch.heigvd.amt.stack.domain.gamification;

import ch.heigvd.amt.stack.domain.authentication.CredentialId;

public interface GamificationRepository {

  void post(CredentialId user, GamificationEvent event);
}
