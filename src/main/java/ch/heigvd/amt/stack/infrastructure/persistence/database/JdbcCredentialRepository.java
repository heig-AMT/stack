package ch.heigvd.amt.stack.infrastructure.persistence.database;

import ch.heigvd.amt.stack.application.authentication.query.CredentialQuery;
import ch.heigvd.amt.stack.domain.authentication.Credential;
import ch.heigvd.amt.stack.domain.authentication.CredentialId;
import ch.heigvd.amt.stack.domain.authentication.CredentialRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Alternative;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

@ApplicationScoped
@Alternative
public class JdbcCredentialRepository extends JdbcRepository<Credential, CredentialId> implements CredentialRepository {

    @Override
    public Optional<Credential> findBy(CredentialQuery query) {
        return Optional.empty();
    }

    @Override
    public void save(Credential credential) {
        try {
            PreparedStatement ps = getDataSource().getConnection().prepareStatement(
                    "INSERT INTO Credential VALUES (" + credential.getId().toString() + ","
                            + credential.getUsername() + "," + credential.getHashedPassword() + ");");
            ps.execute();
        } catch (SQLException ex) {
        }
    }

    @Override
    public void remove(CredentialId credentialId) {
        try {
            PreparedStatement ps = getDataSource().getConnection().prepareStatement(
                    "DELETE FROM Credential WHERE idCredential=\'" + credentialId.toString() + "\'))");
            ps.execute();
        } catch (SQLException ex) {
        }
    }

    @Override
    public Optional<Credential> findById(CredentialId credentialId) {
        Optional<Credential> result = Optional.empty();
        try {
            PreparedStatement ps = getDataSource().getConnection().prepareStatement(
                    "SELECT * FROM Credential WHERE idCredential=\'" + credentialId.toString() + "\'))");
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Credential credential = Credential.builder()
                        .id(CredentialId.from(rs.getString("idCredential")))
                        .username(rs.getString("username"))
                        .hashedPassword(rs.getString("hash")).build();
                result = Optional.of(credential);
            }
        } catch (SQLException ex) {
        }
        return result;
    }

    @Override
    public Collection<Credential> findAll() {
        ArrayList<Credential> result = new ArrayList<>();
        try {
            PreparedStatement ps = getDataSource().getConnection().prepareStatement(
                    "SELECT * FROM Credential;");
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Credential credential = Credential.builder()
                        .id(CredentialId.from(rs.getString("idCredential")))
                        .username(rs.getString("username"))
                        .hashedPassword(rs.getString("hash")).build();
                result.add(credential);
            }
        } catch (SQLException ex) {
        }
        return result;
    }
}
