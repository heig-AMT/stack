package ch.heigvd.amt.stack.infrastructure.persistence.database;

import ch.heigvd.amt.stack.application.question.query.QuestionQuery;
import ch.heigvd.amt.stack.domain.answer.AnswerId;
import ch.heigvd.amt.stack.domain.authentication.CredentialId;
import ch.heigvd.amt.stack.infrastructure.persistence.database.dsl.PrepareStatementScope;
import ch.heigvd.amt.stack.domain.question.Question;
import ch.heigvd.amt.stack.domain.question.QuestionId;
import ch.heigvd.amt.stack.domain.question.QuestionRepository;

import javax.annotation.Resource;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Default;
import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@ApplicationScoped
@Default
public class JdbcQuestionRepository extends JdbcRepository<Question, QuestionId> implements QuestionRepository {

    private static Question parseQuestion(ResultSet resultSet) throws SQLException {
        return Question.builder()
                .id(QuestionId.from(resultSet.getString("idQuestion")))
                .author(CredentialId.from(resultSet.getString("idxCredential")))
                .title(resultSet.getString("title"))
                .description(resultSet.getString("description"))
                .creation((resultSet.getTimestamp("instant")).toInstant())
                .selectedAnswer(AnswerId.from(resultSet.getString("idxSelectedAnswer")))
                .build();
    }

    @Override
    public Collection<Question> findBy(QuestionQuery query) {
        setup(dataSource);
        return findFor(dataSource,
                JdbcQuestionRepository::parseQuestion,
                "SELECT * FROM Question WHERE LOWER(description) LIKE ? OR LOWER(title) LIKE ?;",
                    (ps) -> {
                    ps.setString(1, "%" + query.getShouldContain().trim().toLowerCase() + "%");
                    ps.setString(2, "%" + query.getShouldContain().trim().toLowerCase() + "%");
                }).collect(Collectors.toList());
    }

    @Override
    public void save(Question question) {
        setup(dataSource);
        var insert = "INSERT INTO Question(idQuestion, idxCredential, idxSelectedAnswer, title, description, instant)" +
                " VALUES (?, ?, ?, ?, ?, ?) ON CONFLICT (idQuestion) DO UPDATE SET idxSelectedAnswer = ?;";
        try (var connection = dataSource.getConnection()) {
            var statement = connection.prepareStatement(insert);
            statement.setString(1, question.getId().toString());
            statement.setString(2, question.getAuthor().toString());
            statement.setString(3, question.getSelectedAnswer() != null ? question.getSelectedAnswer().toString() : null);
            statement.setString(4, question.getTitle());
            statement.setString(5, question.getDescription());
            statement.setTimestamp(6, Timestamp.from(question.getCreation()));
            statement.setString(7, question.getSelectedAnswer() != null ? question.getSelectedAnswer().toString() : null);
            statement.execute();
        } catch (SQLException ex) {
            ex.printStackTrace();
            Logger.getLogger("JDBC").log(Level.WARNING, "Could not add question " + question.getId());
        }
    }

    @Override
    public void remove(QuestionId questionId) {
        setup(dataSource);
        var delete = "DELETE FROM Question WHERE idQuestion = ?;";
        try (var connection = dataSource.getConnection()) {
            var statement = connection.prepareStatement(delete);
            statement.setString(1, questionId.toString());
            statement.execute();
        } catch (SQLException ex) {
            Logger.getLogger("JDBC").log(Level.WARNING, "Could not remove question " + questionId);
        }
    }

    @Override
    public Optional<Question> findById(QuestionId questionId) {
        setup(dataSource);
        return findFor(dataSource,
                JdbcQuestionRepository::parseQuestion,
                "SELECT * FROM Question WHERE idQuestion = ?;",
                (ps) -> {
                    ps.setString(1, questionId.toString());
                }).findFirst();
    }

    @Override
    public Collection<Question> findAll() {
        setup(dataSource);
        return findFor(dataSource,
                JdbcQuestionRepository::parseQuestion,
                "SELECT * FROM Question;",
                PrepareStatementScope.none())
                .collect(Collectors.toList());
    }
}
