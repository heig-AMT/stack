package ch.heigvd.amt.stack.infrastructure.persistence.database;

import ch.heigvd.amt.stack.application.authentication.query.SessionQuery;
import ch.heigvd.amt.stack.domain.authentication.Session;
import ch.heigvd.amt.stack.domain.authentication.SessionId;
import ch.heigvd.amt.stack.domain.authentication.SessionRepository;

import javax.annotation.Resource;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Alternative;
import javax.sql.DataSource;
import java.util.Collection;
import java.util.Optional;

@ApplicationScoped
@Alternative
public class JdbcSessionRepository implements SessionRepository {

    @Resource(name = "database")
    private DataSource database;

    @Override
    public Optional<Session> findBy(SessionQuery query) {
        return Optional.empty();
    }

    @Override
    public void save(Session session) {

    }

    @Override
    public void remove(SessionId sessionId) {

    }

    @Override
    public Optional<Session> findById(SessionId sessionId) {
        return Optional.empty();
    }

    @Override
    public Collection<Session> findAll() {
        return null;
    }
}
