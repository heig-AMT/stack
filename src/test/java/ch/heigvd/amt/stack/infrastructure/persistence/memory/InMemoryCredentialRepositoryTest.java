package ch.heigvd.amt.stack.infrastructure.persistence.memory;

import ch.heigvd.amt.stack.domain.authentication.AuthenticationFailedException;
import ch.heigvd.amt.stack.domain.authentication.Credential;
import ch.heigvd.amt.stack.domain.authentication.CredentialId;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class InMemoryCredentialRepositoryTest {

    @Test
    public void testCredentialUpdatesWork() {
        var repository = new InMemoryCredentialRepository();
        var credentialsId = CredentialId.create();
        var credentials = Credential.builder()
                .id(credentialsId)
                .username("alice")
                .hashedPassword("hashed")
                .build();

        repository.save(credentials);
        assertDoesNotThrow(() -> {
            repository.save(credentials);
        });
    }

    @Test
    public void testDuplicateCredentialsDoNotWork() {
        var repository = new InMemoryCredentialRepository();
        var alice = Credential.builder()
                .id(CredentialId.create())
                .username("alice")
                .hashedPassword("hashed")
                .build();
        var mallory = Credential.builder()
                .id(CredentialId.create())
                .username("alice")
                .hashedPassword("hashed")
                .build();
        repository.save(alice);
        assertThrows(AuthenticationFailedException.class, () -> {
            repository.save(mallory);
        });
    }
}
