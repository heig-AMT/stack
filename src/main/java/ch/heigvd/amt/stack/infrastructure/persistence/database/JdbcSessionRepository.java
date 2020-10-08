package ch.heigvd.amt.stack.infrastructure.persistence.database;

import ch.heigvd.amt.stack.application.authentication.query.SessionQuery;
import ch.heigvd.amt.stack.domain.authentication.*;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Alternative;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

@ApplicationScoped
@Alternative
public class JdbcSessionRepository extends JdbcRepository<Session, SessionId> implements SessionRepository {

    @Override
    public Optional<Session> findBy(SessionQuery query) {
        return Optional.empty();
    }

    @Override
    public void save(Session session) {
        try {
            PreparedStatement ps = getDataSource().getConnection().prepareStatement(
                    "INSERT INTO Session (idSession, idxCredential, tag) VALUES (" + session.getId().toString() + ", " +
                            session.getUser().toString() + "," + session.getTag() + ");");
            ps.execute();
        } catch (SQLException ex) {
        }
    }

    @Override
    public void remove(SessionId sessionId) {
        try {
            PreparedStatement ps = getDataSource().getConnection().prepareStatement(
                    "DELETE FROM Session WHERE idSession=\'" + sessionId.toString() + "\'))");
            ps.execute();
        } catch (SQLException ex) {
        }
    }

    @Override
    public Optional<Session> findById(SessionId sessionId) {
        Optional<Session> result = Optional.empty();
        try {
            PreparedStatement ps = getDataSource().getConnection().prepareStatement(
                    "SELECT * FROM Session WHERE idSession=\'" + sessionId.toString() + "\'))");
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Session session = Session.builder()
                        .id(SessionId.from(rs.getString("idSession")))
                        .user(CredentialId.from(rs.getString("idxCredential")))
                        .tag(rs.getString("tag")).build();
                result = Optional.of(session);
            }
        } catch (SQLException ex) {
        }
        return result;
    }

    @Override
    public Collection<Session> findAll() {
        ArrayList<Session> result = new ArrayList<>();
        try {
            PreparedStatement ps = getDataSource().getConnection().prepareStatement(
                    "SELECT * FROM Session;");
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Session session = Session.builder()
                        .id(SessionId.from(rs.getString("idSession")))
                        .user(CredentialId.from(rs.getString("idxCredential")))
                        .tag(rs.getString("tag")).build();
                result.add(session);
            }
        } catch (SQLException ex) {
        }
        return result;
    }
}
