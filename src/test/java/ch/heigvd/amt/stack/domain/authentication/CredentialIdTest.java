package ch.heigvd.amt.stack.domain.authentication;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class CredentialIdTest {

    @Test
    public void testCreatedCredentialIdsAreUnique() {
        var first = CredentialId.create();
        var second = CredentialId.create();

        assertNotEquals(first, second);
    }
}
