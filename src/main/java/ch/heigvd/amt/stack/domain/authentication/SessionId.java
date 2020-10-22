package ch.heigvd.amt.stack.domain.authentication;

import ch.heigvd.amt.stack.domain.AbstractId;

import java.util.UUID;

public class SessionId extends AbstractId {

    public static SessionId create() {
        return new SessionId(UUID.randomUUID());
    }

    public static SessionId from(String id) {
        return new SessionId(UUID.fromString(id));
    }

    private SessionId(UUID backing) {
        super(backing);
    }
}
