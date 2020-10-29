package ch.heigvd.amt.stack.infrastructure.persistence.database;

import ch.heigvd.amt.stack.application.authentication.query.CredentialQuery;
import ch.heigvd.amt.stack.domain.authentication.AuthenticationFailedException;
import ch.heigvd.amt.stack.domain.authentication.Credential;
import ch.heigvd.amt.stack.domain.authentication.CredentialId;
import ch.heigvd.amt.stack.domain.authentication.CredentialRepository;
import ch.heigvd.amt.stack.infrastructure.persistence.database.dsl.PrepareStatementScope;

import javax.annotation.Resource;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Default;
import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@ApplicationScoped
@Default
public class JdbcCredentialRepository extends JdbcRepository<Credential, CredentialId> implements CredentialRepository {

    private static Credential parseCredential(ResultSet resultSet) throws SQLException {
        return Credential.builder()
                .id(CredentialId.from(resultSet.getString("idCredential")))
                .username(resultSet.getString("username"))
                .hashedPassword(resultSet.getString("hash"))
                .build();
    }

    @Override
    public Optional<Credential> findBy(CredentialQuery query) {
        setup(dataSource);
        return Optional.of(findFor(dataSource,
                JdbcCredentialRepository::parseCredential,
                "SELECT * FROM Credential WHERE username = ?;",
                (ps) -> {
                    ps.setString(1, query.getUsername());
                })
                .collect(Collectors.toList()).get(0));
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
                return Optional.of(parseCredential(rs));
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
        return findFor(dataSource,
                JdbcCredentialRepository::parseCredential,
                "SELECT * FROM Credential;",
                PrepareStatementScope.none())
                .collect(Collectors.toList());
    }
}
