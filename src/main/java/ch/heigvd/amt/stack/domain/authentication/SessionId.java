package ch.heigvd.amt.stack.domain.authentication;

import ch.heigvd.amt.stack.domain.Id;

import java.util.UUID;

public class SessionId extends Id {

    public static SessionId create() {
        return new SessionId(UUID.randomUUID());
    }

    private SessionId(UUID backing) {
        super(backing);
    }
}
