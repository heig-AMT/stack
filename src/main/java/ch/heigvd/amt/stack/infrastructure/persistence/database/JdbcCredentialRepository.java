package ch.heigvd.amt.stack.infrastructure.persistence.database;

import ch.heigvd.amt.stack.application.authentication.query.CredentialQuery;
import ch.heigvd.amt.stack.domain.authentication.AuthenticationFailedException;
import ch.heigvd.amt.stack.domain.authentication.Credential;
import ch.heigvd.amt.stack.domain.authentication.CredentialId;
import ch.heigvd.amt.stack.domain.authentication.CredentialRepository;

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
public class JdbcCredentialRepository extends JdbcRepository<Credential, CredentialId> implements CredentialRepository {

    @Resource(name = "database")
    private DataSource dataSource;

    @Override
    public Optional<Credential> findBy(CredentialQuery query) {
        setup(dataSource);
        // TODO : Offer an optimized implementation, with SQL-specific optimizations.
        return findAll().stream()
                .filter(cred -> cred.getUsername().equals(query.getUsername()))
                .findAny();
    }

    @Override
    public void save(Credential credential) {
        setup(dataSource);
        var insert = "INSERT INTO Credential(idCredential, username, hash) VALUES (?, ?, ?) ON CONFLICT (idCredential) DO UPDATE SET hash = ?;";
        try (var connection = dataSource.getConnection()) {
            var statement = connection.prepareStatement(insert);
            statement.setString(1, credential.getId().toString());
            statement.setString(2, credential.getUsername());
            statement.setString(3, credential.getHashedPassword());
            statement.setString(4, credential.getHashedPassword());
            statement.execute();
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new AuthenticationFailedException();
        }
    }

    @Override
    public void remove(CredentialId credentialId) {
        setup(dataSource);
        var delete = "DELETE FROM Credential WHERE idCredential = ?;";
        try (var connection = dataSource.getConnection()) {
            var statement = connection.prepareStatement(delete);
            statement.setString(1, credentialId.toString());
            statement.execute();
        } catch (SQLException ex) {
            ex.printStackTrace();
            Logger.getLogger("JDBC").log(Level.WARNING, "Could not remove credential " + credentialId);
        }
    }

    @Override
    public Optional<Credential> findById(CredentialId credentialId) {
        setup(dataSource);
        var select = "SELECT * FROM Credential WHERE idCredential = ?;";
        try (var connection = dataSource.getConnection()) {
            var statement = connection.prepareStatement(select);
            statement.setString(1, credentialId.toString());
            var rs = statement.executeQuery();

            if (rs.next()) {
                return Optional.of(Credential.builder()
                        .id(CredentialId.from(rs.getString("idCredential")))
                        .username(rs.getString("username"))
                        .hashedPassword(rs.getString("hash")).build());
            } else {
                return Optional.empty();
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            Logger.getLogger("JDBC").log(Level.WARNING, "Could not find credential " + credentialId);
        }
        return Optional.empty();
    }

    @Override
    public Collection<Credential> findAll() {
        setup(dataSource);
        var select = "SELECT * FROM Credential;";
        Collection<Credential> result = new ArrayList<>();
        try (var connection = dataSource.getConnection()) {
            var statement = connection.prepareStatement(select);
            var rs = statement.executeQuery();
            while (rs.next()) {
                Credential credential = Credential.builder()
                        .id(CredentialId.from(rs.getString("idCredential")))
                        .username(rs.getString("username"))
                        .hashedPassword(rs.getString("hash")).build();
                result.add(credential);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            Logger.getLogger("JDBC").log(Level.WARNING, "Could not findAll()");
            return Collections.emptyList();
        }
        return result;
    }
}
