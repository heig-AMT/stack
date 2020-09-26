package ch.heigvd.amt.stack.domain.authentication;

import org.junit.Assert;
import org.junit.Test;

public class SessionTest {

    @Test
    public void testBuiltSessionHasNonNullId() {
        var session = Session.builder()
                .tag("tag")
                .user(CredentialId.create())
                .build();

        Assert.assertNotNull(session.getId());
    }

    @Test
    public void testBuiltSessionsWithoutIdAreUnique() {
        var first = Session.builder().build();
        var second = Session.builder().build();

        Assert.assertNotEquals(first, second);
    }

    @Test
    public void testBuiltSessionsWithIdCanBeSame() {
        var id = SessionId.create();
        var first = Session.builder().id(id).build();
        var second = Session.builder().id(id).build();

        Assert.assertEquals(first, second);
    }
}
