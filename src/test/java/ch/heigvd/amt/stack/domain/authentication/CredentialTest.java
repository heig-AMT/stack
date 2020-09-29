package ch.heigvd.amt.stack.domain.authentication;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CredentialTest {

    @Test
    public void testBuiltCredentialHasNonNullId() {
        var credential = Credential.builder()
                .username("hello")
                .hashedPassword("hashed")
                .build();

        assertNotNull(credential.getId());
    }

    @Test
    public void testBuiltCredentialsWithoutIdAreUnique() {
        var first = Credential.builder().build();
        var second = Credential.builder().build();

        assertNotEquals(first, second);
    }

    @Test
    public void testBuiltCredentialsWithIdCanBeSame() {
        var id = CredentialId.create();
        var first = Credential.builder().id(id).build();
        var second = Credential.builder().id(id).build();

        assertEquals(first, second);
    }
}
