package ch.heigvd.amt.stack.domain.authentication;

import org.junit.Assert;
import org.junit.Test;

public class SessionIdTest {

    @Test
    public void testCreatedSessionIdsAreUnique() {
        var first = SessionId.create();
        var second = SessionId.create();

        Assert.assertNotEquals(first, second);
    }
}
