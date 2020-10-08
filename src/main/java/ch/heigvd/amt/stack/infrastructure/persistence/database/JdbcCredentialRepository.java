package ch.heigvd.amt.stack.infrastructure.persistence.database;

import ch.heigvd.amt.stack.application.authentication.query.CredentialQuery;
import ch.heigvd.amt.stack.domain.authentication.Credential;
import ch.heigvd.amt.stack.domain.authentication.CredentialId;
import ch.heigvd.amt.stack.domain.authentication.CredentialRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Alternative;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

@ApplicationScoped
@Alternative
public class JdbcCredentialRepository extends JdbcRepository<Credential, CredentialId> implements CredentialRepository {

    @Override
    public Optional<Credential> findBy(CredentialQuery query) {
        return Optional.empty();
    }

    @Override
    public void save(Credential credential) {

    }

    @Override
    public void remove(CredentialId credentialId) {

    }

    @Override
    public Optional<Credential> findById(CredentialId credentialId) {
        return Optional.empty();
    }

    @Override
    public Collection<Credential> findAll() {
        return Collections.emptyList();
    }
}
