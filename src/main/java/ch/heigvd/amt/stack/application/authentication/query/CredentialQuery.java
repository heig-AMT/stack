package ch.heigvd.amt.stack.application.authentication.query;

import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class CredentialQuery {
    String username;
}
