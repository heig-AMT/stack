package ch.heigvd.amt.stack.domain.authentication;

import ch.heigvd.amt.stack.domain.Entity;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class Credential implements Entity<CredentialId> {
    @Builder.Default
    CredentialId id = CredentialId.create();
    String username;
    String hashedPassword;
}
