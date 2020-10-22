package ch.heigvd.amt.stack.domain.authentication;

import ch.heigvd.amt.stack.domain.AbstractId;

import java.util.UUID;

public class CredentialId extends AbstractId {

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
