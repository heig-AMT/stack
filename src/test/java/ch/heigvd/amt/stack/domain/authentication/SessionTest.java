package ch.heigvd.amt.stack.domain.authentication;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SessionTest {

    @Test
    public void testBuiltSessionHasNonNullId() {
        var session = Session.builder()
                .tag("tag")
                .user(CredentialId.create())
                .build();

        assertNotNull(session.getId());
    }

    @Test
    public void testBuiltSessionsWithoutIdAreUnique() {
        var first = Session.builder().build();
        var second = Session.builder().build();

        assertNotEquals(first, second);
    }

    @Test
    public void testBuiltSessionsWithIdCanBeSame() {
        var id = SessionId.create();
        var first = Session.builder().id(id).build();
        var second = Session.builder().id(id).build();

        assertEquals(first, second);
    }
}
