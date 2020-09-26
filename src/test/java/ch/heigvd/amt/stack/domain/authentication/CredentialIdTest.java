package ch.heigvd.amt.stack.domain.authentication;

import org.junit.Assert;
import org.junit.Test;

public class CredentialIdTest {

    @Test
    public void testCreatedCredentialIdsAreUnique() {
        var first = CredentialId.create();
        var second = CredentialId.create();

        Assert.assertNotEquals(first, second);
    }
}
