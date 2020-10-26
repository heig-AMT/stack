package ch.heigvd.amt.stack.infrastructure.persistence.database;

import ch.heigvd.amt.stack.domain.Repository;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * An abstract class representing a repository that will be accessed with Jdbc. It contains all the logic needed to
 * initialize the tables that we will need in the database, as well as a {@link javax.sql.DataSource}.
 *
 * @param <Entity> The type of the entities stored in the repository.
 * @param <Id>     The type of the identifiers of the entities.
 */
public abstract class JdbcRepository<Entity, Id> implements Repository<Entity, Id> {

    private static final String CREATE_CREDENTIALS =
            "CREATE TABLE IF NOT EXISTS Credential " +
                    "( idCredential VARCHAR PRIMARY KEY" +
                    ", username VARCHAR(50)" +
                    ", hash VARCHAR(100)" +
                    ");";

    private static final String CREATE_SESSIONS =
            "CREATE TABLE IF NOT EXISTS Session" +
                    "( idSession VARCHAR PRIMARY KEY" +
                    ", idxCredential VARCHAR" +
                    ", tag VARCHAR(50) " +
                    ", CONSTRAINT fkCredential FOREIGN KEY (idxCredential) REFERENCES Credential (idCredential) ON UPDATE CASCADE ON DELETE CASCADE);";

    private static final String CREATE_QUESTIONS =
            "CREATE TABLE IF NOT EXISTS Question" +
                    "( idQuestion VARCHAR PRIMARY KEY" +
                    ", idxCredential VARCHAR" +
                    ", resolved BOOLEAN" +
                    ", title VARCHAR(50)" +
                    ", description VARCHAR(200)" +
                    ", instant TIMESTAMP" +
                    ", CONSTRAINT fkCredential FOREIGN KEY (idxCredential) REFERENCES Credential (idCredential) ON UPDATE CASCADE ON DELETE CASCADE);";

    private static final String CREATE_ANSWERS =
            "CREATE TABLE IF NOT EXISTS Answer" +
                    "( idAnswer VARCHAR PRIMARY KEY " +
                    ", idxQuestion VARCHAR" +
                    ", idxCredential VARCHAR" +
                    ", description VARCHAR(1000)" +
                    ", instant TIMESTAMP" +
                    ", CONSTRAINT fkCredential FOREIGN KEY (idxCredential) REFERENCES Credential (idCredential) ON UPDATE CASCADE ON DELETE CASCADE" +
                    ", CONSTRAINT fkQuestion FOREIGN KEY (idxQuestion) REFERENCES Question (idQuestion) ON UPDATE CASCADE ON DELETE CASCADE);";

    private static final String CREATE_COMMENTS =
            "CREATE TABLE IF NOT EXISTS Comment" +
                    "( idComment VARCHAR PRIMARY KEY " +
                    ", idxAnswer VARCHAR" +
                    ", idxCredential VARCHAR" +
                    ", contents VARCHAR(1000)" +
                    ", instant TIMESTAMP" +
                    ", CONSTRAINT fkCredential FOREIGN KEY (idxCredential) REFERENCES Credential (idCredential) ON UPDATE CASCADE ON DELETE CASCADE" +
                    ", CONSTRAINT fkAnswer FOREIGN KEY (idxAnswer) REFERENCES Answer (idAnswer) ON UPDATE CASCADE ON DELETE CASCADE);";

    private static final String CREATE_VOTES =
            "CREATE TABLE IF NOT EXISTS Vote" +
                    "( idxAnswer VARCHAR "+
                    ", idxCredential VARCHAR" +
                    ", isUpvote BOOLEAN" +
                    ", PRIMARY KEY (idxAnswer, idxCredential)"+
                    ", CONSTRAINT fkCredential FOREIGN KEY (idxCredential) REFERENCES Credential (idCredential) ON UPDATE CASCADE ON DELETE CASCADE" +
                    ", CONSTRAINT fkAnswer FOREIGN KEY (idxAnswer) REFERENCES Answer (idAnswer) ON UPDATE CASCADE ON DELETE CASCADE);";

    protected void setup(DataSource dataSource) {
        try (var connection = dataSource.getConnection()) {
            connection.prepareStatement(CREATE_CREDENTIALS).execute();
            connection.prepareStatement(CREATE_SESSIONS).execute();
            connection.prepareStatement(CREATE_QUESTIONS).execute();
            connection.prepareStatement(CREATE_ANSWERS).execute();
            connection.prepareStatement(CREATE_COMMENTS).execute();
            connection.prepareStatement(CREATE_VOTES).execute();

        } catch (SQLException exception) {
            exception.printStackTrace();
            Logger.getLogger("JDBC").log(Level.SEVERE, "SQLException while setting up repository.");
        }
    }
}
