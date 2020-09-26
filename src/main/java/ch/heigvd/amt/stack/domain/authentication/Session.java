package ch.heigvd.amt.stack.domain.authentication;

import ch.heigvd.amt.stack.domain.Entity;
import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class Session implements Entity<SessionId> {
    @Builder.Default
    SessionId id = SessionId.create();
    String tag;
    CredentialId user;
}
