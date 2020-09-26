package ch.heigvd.amt.stack.domain.authentication;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class SessionIdTest {

    @Test
    public void testCreatedSessionIdsAreUnique() {
        var first = SessionId.create();
        var second = SessionId.create();

        assertNotEquals(first, second);
    }
}
