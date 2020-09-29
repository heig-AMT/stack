package ch.heigvd.amt.stack.infrastructure.persistence.memory;

import ch.heigvd.amt.stack.application.authentication.query.SessionQuery;
import ch.heigvd.amt.stack.domain.authentication.Session;
import ch.heigvd.amt.stack.domain.authentication.SessionId;
import ch.heigvd.amt.stack.domain.authentication.SessionRepository;

import java.util.Optional;

public class InMemorySessionRepository extends InMemoryRepository<Session, SessionId> implements SessionRepository {

    @Override
    public void save(Session entity) {
        // TODO : Make this portion of code thread-safe.
        // TODO : Rather than writing an empty credential for an identifier, we may want to remove the item from the
        //
        findBy(SessionQuery.builder().tag(entity.getTag()).build())
                .ifPresent(session -> super.remove(session.getId()));
        super.save(entity);
    }

    @Override
    public Optional<Session> findBy(SessionQuery query) {
        return findAll().stream()
                .filter(session -> session.getTag().equals(query.getTag()))
                .findAny();
    }
}
