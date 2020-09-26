package ch.heigvd.amt.stack.domain.authentication;

import ch.heigvd.amt.stack.application.authentication.query.SessionQuery;
import ch.heigvd.amt.stack.domain.Repository;

import java.util.Optional;

public interface SessionRepository extends Repository<Session, SessionId> {
    Optional<Session> findBy(SessionQuery query);
}
