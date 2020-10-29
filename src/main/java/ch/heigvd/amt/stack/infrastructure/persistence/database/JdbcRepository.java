package ch.heigvd.amt.stack.infrastructure.persistence.database;

import ch.heigvd.amt.stack.domain.Repository;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;

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
                    ", CONSTRAINT fkSessionCredential FOREIGN KEY (idxCredential) REFERENCES Credential (idCredential) ON UPDATE CASCADE ON DELETE CASCADE);";

    private static final String CREATE_QUESTIONS =
            "CREATE TABLE IF NOT EXISTS Question" +
                    "( idQuestion VARCHAR PRIMARY KEY" +
                    ", idxCredential VARCHAR" +
                    ", title VARCHAR(50)" +
                    ", description VARCHAR(200)" +
                    ", instant TIMESTAMP" +
                    ", CONSTRAINT fkQuestionCredential FOREIGN KEY (idxCredential) REFERENCES Credential (idCredential) ON UPDATE CASCADE ON DELETE CASCADE);";

    private static final String CREATE_ANSWERS =
            "CREATE TABLE IF NOT EXISTS Answer" +
                    "( idAnswer VARCHAR PRIMARY KEY " +
                    ", idxQuestion VARCHAR" +
                    ", idxCredential VARCHAR" +
                    ", description VARCHAR(1000)" +
                    ", instant TIMESTAMP" +
                    ", CONSTRAINT fkAnswerCredential FOREIGN KEY (idxCredential) REFERENCES Credential (idCredential) ON UPDATE CASCADE ON DELETE CASCADE" +
                    ", CONSTRAINT fkAnswerQuestion FOREIGN KEY (idxQuestion) REFERENCES Question (idQuestion) ON UPDATE CASCADE ON DELETE CASCADE);";

    private static final String CREATE_COMMENTS =
            "CREATE TABLE IF NOT EXISTS Comment" +
                    "( idComment VARCHAR PRIMARY KEY " +
                    ", idxAnswer VARCHAR" +
                    ", idxCredential VARCHAR" +
                    ", contents VARCHAR(1000)" +
                    ", instant TIMESTAMP" +
                    ", CONSTRAINT fkCommentCredential FOREIGN KEY (idxCredential) REFERENCES Credential (idCredential) ON UPDATE CASCADE ON DELETE CASCADE" +
                    ", CONSTRAINT fkCommentAnswer FOREIGN KEY (idxAnswer) REFERENCES Answer (idAnswer) ON UPDATE CASCADE ON DELETE CASCADE);";

    private static final String CREATE_VOTES =
            "CREATE TABLE IF NOT EXISTS Vote" +
                    "( idxAnswer VARCHAR " +
                    ", idxCredential VARCHAR" +
                    ", isUpvote BOOLEAN" +
                    ", PRIMARY KEY (idxAnswer, idxCredential)" +
                    ", CONSTRAINT fkVoteCredential FOREIGN KEY (idxCredential) REFERENCES Credential (idCredential) ON UPDATE CASCADE ON DELETE CASCADE" +
                    ", CONSTRAINT fkVoteAnswer FOREIGN KEY (idxAnswer) REFERENCES Answer (idAnswer) ON UPDATE CASCADE ON DELETE CASCADE);";

    public static final String UPDATE_QUESTIONS =
            "ALTER TABLE Question ADD COLUMN IF NOT EXISTS " +
                    "idxSelectedAnswer VARCHAR DEFAULT NULL REFERENCES Answer(idAnswer) ON UPDATE CASCADE ON DELETE SET NULL;";


    protected void setup(DataSource dataSource) {
        try (var connection = dataSource.getConnection()) {
            connection.prepareStatement(CREATE_CREDENTIALS).execute();
            connection.prepareStatement(CREATE_SESSIONS).execute();
            connection.prepareStatement(CREATE_QUESTIONS).execute();
            connection.prepareStatement(CREATE_ANSWERS).execute();
            connection.prepareStatement(CREATE_COMMENTS).execute();
            connection.prepareStatement(CREATE_VOTES).execute();
            connection.prepareStatement(UPDATE_QUESTIONS).execute();

        } catch (SQLException exception) {
            exception.printStackTrace();
            Logger.getLogger("JDBC").log(Level.SEVERE, "SQLException while setting up repository.");
        }
    }

    /**
     * Returns all the elements from a certain {@link DataSource} for a certain request, mapped to a certain type.
     *
     * @param source  the {@link DataSource} to use for the query.
     * @param map     the transformation function.
     * @param request the SQL request.
     * @param scope   a {@link PrepareStatementScope}, if necessary.
     * @param <T>     the type of the resulting elements.
     * @return a {@link Collection} of retrieved items.
     */
    protected static <T> Stream<T> findFor(DataSource source,
                                           Mapper<T> map,
                                           String request,
                                           PrepareStatementScope scope) {
        var result = Stream.<T>builder();
        try (var connection = source.getConnection()) {

            // Prepare the statement.
            var statement = connection.prepareStatement(request);
            scope.prepare(statement);

            // Execute the query.
            var rs = statement.executeQuery();
            while (rs.next()) {
                result.add(map.apply(rs));
            }
        } catch (SQLException ex) {
            Logger.getLogger("JDBC").log(Level.WARNING, ex.getMessage());
            Logger.getLogger("JDBC").log(Level.WARNING, "Could not findAll()");
            return Stream.empty();
        }
        return result.build();
    }
}
