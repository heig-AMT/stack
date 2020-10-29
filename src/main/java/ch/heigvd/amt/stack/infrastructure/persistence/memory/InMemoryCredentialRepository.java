package ch.heigvd.amt.stack.infrastructure.persistence.memory;

import ch.heigvd.amt.stack.application.authentication.query.CredentialQuery;
import ch.heigvd.amt.stack.domain.authentication.AuthenticationFailedException;
import ch.heigvd.amt.stack.domain.authentication.Credential;
import ch.heigvd.amt.stack.domain.authentication.CredentialId;
import ch.heigvd.amt.stack.domain.authentication.CredentialRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Alternative;
import java.util.Optional;

@ApplicationScoped
@Alternative
public class InMemoryCredentialRepository extends InMemoryRepository<Credential, CredentialId> implements CredentialRepository {

    @Override
    public synchronized void save(Credential entity) {
        Optional<Credential> previous = findBy(CredentialQuery.builder()
                .username(entity.getUsername())
                .build());
        if (previous.isPresent() && !entity.getId().equals(previous.get().getId())) {
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
