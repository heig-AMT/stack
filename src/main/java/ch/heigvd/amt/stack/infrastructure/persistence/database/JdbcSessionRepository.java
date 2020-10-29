package ch.heigvd.amt.stack.infrastructure.persistence.database;

import ch.heigvd.amt.stack.application.authentication.query.SessionQuery;
import ch.heigvd.amt.stack.domain.authentication.CredentialId;
import ch.heigvd.amt.stack.domain.authentication.Session;
import ch.heigvd.amt.stack.domain.authentication.SessionId;
import ch.heigvd.amt.stack.domain.authentication.SessionRepository;

import javax.annotation.Resource;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Default;
import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

@ApplicationScoped
@Default
public class JdbcSessionRepository extends JdbcRepository<Session, SessionId> implements SessionRepository {

    @Resource(name = "database")
    private DataSource dataSource;

    private DataSource getDataSource() {
        return dataSource;
    }

    @Override
    public Optional<Session> findBy(SessionQuery query) {
        setup(dataSource);
        var select = "SELECT * FROM Session WHERE tag = ?;";
        try (var connection = getDataSource().getConnection()) {
            var statement = connection.prepareStatement(select);
            statement.setString(1, query.getTag());
            var rs = statement.executeQuery();

            if (rs.next()) {
                return Optional.of(Session.builder()
                        .id(SessionId.from(rs.getString("idSession")))
                        .user(CredentialId.from(rs.getString("idxCredential")))
                        .tag(rs.getString("tag")).build());
            } else {
                return Optional.empty();
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            Logger.getLogger("JDBC").log(Level.WARNING, "Could not find session with tag " + query.getTag());
        }
        return Optional.empty();
    }

    @Override
    public void save(Session session) {
        setup(dataSource);
        var insert = "INSERT INTO Session (idSession, idxCredential, tag) VALUES (?, ?, ?);";
        try (var connection = getDataSource().getConnection()) {
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
        try (var connection = getDataSource().getConnection()) {
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
        var select = "SELECT * FROM Session WHERE idSession = ?;";
        try (var connection = getDataSource().getConnection()) {
            var statement = connection.prepareStatement(select);
            statement.setString(1, sessionId.toString());
            var rs = statement.executeQuery();

            if (rs.next()) {
                return Optional.of(Session.builder()
                        .id(SessionId.from(rs.getString("idSession")))
                        .user(CredentialId.from(rs.getString("idxCredential")))
                        .tag(rs.getString("tag")).build());
            } else {
                return Optional.empty();
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            Logger.getLogger("JDBC").log(Level.WARNING, "Could not find session " + sessionId);
        }
        return Optional.empty();
    }

    @Override
    public Collection<Session> findAll() {
        setup(dataSource);
        var select = "SELECT * FROM Session;";
        Collection<Session> result = new ArrayList<>();
        try (var connection = getDataSource().getConnection()) {
            var statement = connection.prepareStatement(select);
            var rs = statement.executeQuery();
            while (rs.next()) {
                var session = Session.builder()
                        .id(SessionId.from(rs.getString("idSession")))
                        .user(CredentialId.from(rs.getString("idxCredential")))
                        .tag(rs.getString("tag")).build();
                result.add(session);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            Logger.getLogger("JDBC").log(Level.WARNING, "Could not findAll()");
            return Collections.emptyList();
        }
        return result;
    }
}
