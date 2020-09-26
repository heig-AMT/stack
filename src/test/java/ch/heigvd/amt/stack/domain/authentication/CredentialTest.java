package ch.heigvd.amt.stack.domain.authentication;

import org.junit.Assert;
import org.junit.Test;

public class CredentialTest {

    @Test
    public void testBuiltCredentialHasNonNullId() {
        var credential = Credential.builder()
                .username("hello")
                .clearTextPassword("world")
                .build();

        Assert.assertNotNull(credential.getId());
    }

    @Test
    public void testBuiltCredentialsWithoutIdAreUnique() {
        var first = Credential.builder().build();
        var second = Credential.builder().build();

        Assert.assertNotEquals(first, second);
    }

    @Test
    public void testBuiltCredentialsWithIdCanBeSame() {
        var id = CredentialId.create();
        var first = Credential.builder().id(id).build();
        var second = Credential.builder().id(id).build();

        Assert.assertEquals(first, second);
    }
}
