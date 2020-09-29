package ch.heigvd.amt.stack.domain.authentication;

import ch.heigvd.amt.stack.application.authentication.query.CredentialQuery;
import ch.heigvd.amt.stack.domain.Repository;

import java.util.Optional;

public interface CredentialRepository extends Repository<Credential, CredentialId> {
    Optional<Credential> findBy(CredentialQuery query);
}
