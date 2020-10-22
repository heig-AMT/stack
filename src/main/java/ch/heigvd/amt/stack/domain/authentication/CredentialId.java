package ch.heigvd.amt.stack.domain.authentication;

import ch.heigvd.amt.stack.domain.Id;

import java.util.UUID;

public class CredentialId extends Id {

    public static CredentialId create() {
        return new CredentialId(UUID.randomUUID());
    }

    public static CredentialId from(String id) {
        return new CredentialId(UUID.fromString(id));
    }

    private CredentialId(UUID backing) {
        super(backing);
    }
}
