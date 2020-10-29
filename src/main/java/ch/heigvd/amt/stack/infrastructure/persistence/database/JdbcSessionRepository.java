package ch.heigvd.amt.stack.infrastructure.persistence.database;

import ch.heigvd.amt.stack.application.authentication.query.SessionQuery;
import ch.heigvd.amt.stack.domain.authentication.CredentialId;
import ch.heigvd.amt.stack.domain.authentication.Session;
import ch.heigvd.amt.stack.domain.authentication.SessionId;
import ch.heigvd.amt.stack.domain.authentication.SessionRepository;
import ch.heigvd.amt.stack.infrastructure.persistence.database.dsl.PrepareStatementScope;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Default;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@ApplicationScoped
@Default
public class JdbcSessionRepository extends JdbcRepository<Session, SessionId> implements SessionRepository {

    private static Session parseSession(ResultSet resultSet) throws SQLException {
        return Session.builder()
                .id(SessionId.from(resultSet.getString("idSession")))
                .user(CredentialId.from(resultSet.getString("idxCredential")))
                .tag(resultSet.getString("tag"))
                .build();
    }

    @Override
    public Optional<Session> findBy(SessionQuery query) {
        setup(dataSource);
        return findFor(dataSource,
                JdbcSessionRepository::parseSession,
                "SELECT * FROM Session WHERE tag = ?;",
                (ps) -> {
                    ps.setString(1, query.getTag());
                }).findFirst();
    }

    @Override
    public void save(Session session) {
        setup(dataSource);
        var insert = "INSERT INTO Session (idSession, idxCredential, tag) VALUES (?, ?, ?);";
        try (var connection = dataSource.getConnection()) {
            var statement = connection.prepareStatement(insert);
            statement.setString(1, session.getId().toString());
            statement.setString(2, session.getUser().toString());
            statement.setString(3, session.getTag());
            statement.execute();
        } catch (SQLException ex) {
            ex.printStackTrace();
            Logger.getLogger("JDBC").log(Level.WARNING, "Could not add session " + session.getId());
        }
    }

    @Override
    public void remove(SessionId sessionId) {
        setup(dataSource);
        var delete = "DELETE FROM Session WHERE idSession = ?;";
        try (var connection = dataSource.getConnection()) {
            var statement = connection.prepareStatement(delete);
            statement.setString(1, sessionId.toString());
            statement.execute();
        } catch (SQLException ex) {
            ex.printStackTrace();
            Logger.getLogger("JDBC").log(Level.WARNING, "Could not remove session " + sessionId);
        }
    }

    @Override
    public Optional<Session> findById(SessionId sessionId) {
        setup(dataSource);
        return findFor(dataSource,
                JdbcSessionRepository::parseSession,
                "SELECT * FROM Session WHERE idSession = ?;",
                (ps) -> {
                    ps.setString(1, sessionId.toString());
                }).findFirst();
    }

    @Override
    public Collection<Session> findAll() {
        setup(dataSource);
        return findFor(dataSource,
                JdbcSessionRepository::parseSession,
                "SELECT * FROM Session;",
                PrepareStatementScope.none()).collect(Collectors.toList());
    }
}
