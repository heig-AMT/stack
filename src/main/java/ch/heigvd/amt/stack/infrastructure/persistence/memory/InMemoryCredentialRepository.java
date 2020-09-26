package ch.heigvd.amt.stack.infrastructure.persistence.memory;

import ch.heigvd.amt.stack.application.authentication.query.CredentialQuery;
import ch.heigvd.amt.stack.domain.authentication.AuthenticationFailedException;
import ch.heigvd.amt.stack.domain.authentication.Credential;
import ch.heigvd.amt.stack.domain.authentication.CredentialId;
import ch.heigvd.amt.stack.domain.authentication.CredentialRepository;

import java.util.Optional;

public class InMemoryCredentialRepository extends InMemoryRepository<Credential, CredentialId> implements CredentialRepository {

    @Override
    public void save(Credential entity) {
        // TODO : Make this portion of code thread-safe.
        Optional<Credential> previous = findBy(CredentialQuery.builder()
                .username(entity.getUsername())
                .build());
        if (previous.isPresent()) {
            throw new AuthenticationFailedException();
        }
        super.save(entity);
    }

    @Override
    public Optional<Credential> findBy(CredentialQuery query) {
        return findAll().stream()
                .filter(cred -> cred.getUsername().equals(query.getUsername()))
                .findAny();
    }
}
