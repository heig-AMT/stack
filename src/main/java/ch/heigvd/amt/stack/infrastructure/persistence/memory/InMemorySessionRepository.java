package ch.heigvd.amt.stack.infrastructure.persistence.memory;

import ch.heigvd.amt.stack.application.authentication.query.SessionQuery;
import ch.heigvd.amt.stack.domain.authentication.Session;
import ch.heigvd.amt.stack.domain.authentication.SessionId;
import ch.heigvd.amt.stack.domain.authentication.SessionRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Alternative;
import java.util.Optional;

@ApplicationScoped
@Alternative
public class InMemorySessionRepository extends InMemoryRepository<Session, SessionId> implements SessionRepository {

    @Override
    public synchronized void save(Session entity) {
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
