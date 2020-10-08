package ch.heigvd.amt.stack.infrastructure.persistence.database;

import ch.heigvd.amt.stack.application.authentication.query.CredentialQuery;
import ch.heigvd.amt.stack.domain.authentication.AuthenticationFailedException;
import ch.heigvd.amt.stack.domain.authentication.Credential;
import ch.heigvd.amt.stack.domain.authentication.CredentialId;
import ch.heigvd.amt.stack.domain.authentication.CredentialRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Alternative;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

@ApplicationScoped
@Alternative
public class JdbcCredentialRepository extends JdbcRepository<Credential, CredentialId> implements CredentialRepository {

    @Override
    public Optional<Credential> findBy(CredentialQuery query) {
        // TODO : Offer an optimized implementation, with SQL-specific optimizations.
        return findAll().stream()
                .filter(cred -> cred.getUsername().equals(query.getUsername()))
                .findAny();
    }

    @Override
    public void save(Credential credential) {
        var insert = "INSERT INTO Credential(idCredential, username, hash) VALUES (?, ?, ?);";
        try {
            var statement = getDataSource().getConnection().prepareStatement(insert);
            statement.setString(1, credential.getId().toString());
            statement.setString(2, credential.getUsername());
            statement.setString(3, credential.getHashedPassword());
            statement.execute();
        } catch (SQLException ex) {
            // TODO : Make sure that duplicates are properly handed.
            throw new AuthenticationFailedException();
        }
    }

    @Override
    public void remove(CredentialId credentialId) {
        var delete = "DELETE FROM Credential WHERE idCredential = ?;";
        try {
            var statement = getDataSource().getConnection().prepareStatement(delete);
            statement.setString(1, credentialId.toString());
            statement.execute();
        } catch (SQLException ex) {
            Logger.getLogger("JDBC").log(Level.WARNING, "Could not remove credential " + credentialId);
        }
    }

    @Override
    public Optional<Credential> findById(CredentialId credentialId) {
        var select = "SELECT * FROM Credential WHERE idCredential = ?;";
        try {
            var statement = getDataSource().getConnection().prepareStatement(select);
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
            Logger.getLogger("JDBC").log(Level.WARNING, "Could not find credential " + credentialId);
        }
        return Optional.empty();
    }

    @Override
    public Collection<Credential> findAll() {
        var select = "SELECT * FROM Credential;";
        Collection<Credential> result = new ArrayList<>();
        try {
            var statement = getDataSource().getConnection().prepareStatement(select);
            var rs = statement.executeQuery();
            while (rs.next()) {
                Credential credential = Credential.builder()
                        .id(CredentialId.from(rs.getString("idCredential")))
                        .username(rs.getString("username"))
                        .hashedPassword(rs.getString("hash")).build();
                result.add(credential);
            }
        } catch (SQLException ex) {
            Logger.getLogger("JDBC").log(Level.WARNING, "Could not findAll()");
            return Collections.emptyList();
        }
        return result;
    }
}
