package ch.heigvd.amt.stack.infrastructure.persistence.database;

import ch.heigvd.amt.stack.application.answer.query.AnswerQuery;
import ch.heigvd.amt.stack.domain.answer.Answer;
import ch.heigvd.amt.stack.domain.answer.AnswerId;
import ch.heigvd.amt.stack.domain.answer.AnswerRepository;
import ch.heigvd.amt.stack.domain.authentication.CredentialId;
import ch.heigvd.amt.stack.domain.question.QuestionId;

import javax.annotation.Resource;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Default;
import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@ApplicationScoped
@Default
public class JdbcAnswerRepository extends JdbcRepository<Answer, AnswerId> implements AnswerRepository {

    @Resource(name = "database")
    private DataSource dataSource;

    /**
     * Parses an answer from a certain {@link ResultSet}.
     *
     * @param resultSet the {@link ResultSet}, at a certain index.
     * @return the returned {@link Answer}.
     * @throws SQLException if an {@link Answer} could not be built.
     */
    private Answer parseAnswer(ResultSet resultSet) throws SQLException {
        return Answer.builder()
                .id(AnswerId.from(resultSet.getString("idAnswer")))
                .question(QuestionId.from(resultSet.getString("idxQuestion")))
                .creator(CredentialId.from(resultSet.getString("idxCredential")))
                .body(resultSet.getString("description"))
                .creation(resultSet.getTimestamp("instant").toInstant())
                .build();
    }

    @Override
    public Collection<Answer> findBy(AnswerQuery query) {
        setup(dataSource);
        if (query.getForQuestion() != null) {
            return findAll().stream()
                    .filter(answer -> (answer.getQuestion().equals(query.getForQuestion())))
                    .collect(Collectors.toList());
        } else {
            return List.of();
        }
    }

    @Override
    public void save(Answer answer) {
        setup(dataSource);
        var insert = "INSERT INTO Answer(idAnswer, idxCredential, idxQuestion, description, instant) VALUES (?, ?, ?, ?,?);";
        try (var connection = dataSource.getConnection()) {
            var statement = connection.prepareStatement(insert);
            statement.setString(1, answer.getId().toString());
            statement.setString(2, answer.getCreator().toString());
            statement.setString(3, answer.getQuestion().toString());
            statement.setString(4, answer.getBody());
            statement.setTimestamp(5, Timestamp.from(answer.getCreation()));
            statement.execute();
        } catch (SQLException ex) {
            ex.printStackTrace();
            Logger.getLogger("JDBC").log(Level.WARNING, "Could not add answer " + answer.getId());
        }
    }

    @Override
    public void remove(AnswerId answerId) {
        setup(dataSource);
        var delete = "DELETE FROM Answer WHERE idAnswer = ?;";
        try (var connection = dataSource.getConnection()) {
            var statement = connection.prepareStatement(delete);
            statement.setString(1, answerId.toString());
            statement.execute();
        } catch (SQLException ex) {
            ex.printStackTrace();
            Logger.getLogger("JDBC").log(Level.WARNING, "Could not remove answer " + answerId);
        }
    }

    @Override
    public Optional<Answer> findById(AnswerId answerId) {
        setup(dataSource);
        var select = "SELECT * FROM Answer WHERE idAnswer = ?;";
        try (var connection = dataSource.getConnection()) {
            var statement = connection.prepareStatement(select);
            statement.setString(1, answerId.toString());
            var rs = statement.executeQuery();

            if (rs.next()) {
                return Optional.of(parseAnswer(rs));
            } else {
                return Optional.empty();
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            Logger.getLogger("JDBC").log(Level.WARNING, "Could not find answer " + answerId);
        }
        return Optional.empty();
    }

    @Override
    public Collection<Answer> findAll() {
        setup(dataSource);
        var select = "SELECT * FROM Answer;";
        Collection<Answer> result = new ArrayList<>();
        try (var connection = dataSource.getConnection()) {
            var statement = connection.prepareStatement(select);
            var rs = statement.executeQuery();

            while (rs.next()) {
                result.add(parseAnswer(rs));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            Logger.getLogger("JDBC").log(Level.WARNING, "Could not findAll()");
            return Collections.emptyList();
        }
        return result;
    }
}
