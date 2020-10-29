package ch.heigvd.amt.stack.infrastructure.persistence.database;

import ch.heigvd.amt.stack.application.question.query.QuestionQuery;
import ch.heigvd.amt.stack.domain.answer.AnswerId;
import ch.heigvd.amt.stack.domain.authentication.CredentialId;
import ch.heigvd.amt.stack.domain.question.Question;
import ch.heigvd.amt.stack.domain.question.QuestionId;
import ch.heigvd.amt.stack.domain.question.QuestionRepository;

import javax.annotation.Resource;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Default;
import javax.sql.DataSource;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

@ApplicationScoped
@Default
public class JdbcQuestionRepository extends JdbcRepository<Question, QuestionId> implements QuestionRepository {

    @Resource(name = "database")
    private DataSource dataSource;

    private DataSource getDataSource() {
        return dataSource;
    }

    @Override
    public Collection<Question> findBy(QuestionQuery query) {
        setup(dataSource);
        if (query.getShouldContain() != null) {
            var select = "SELECT * FROM Question WHERE LOWER(description) LIKE ? OR LOWER(title) LIKE ?;";
            Collection<Question> result = new ArrayList<>();
            try (var connection = getDataSource().getConnection()) {
                var statement = connection.prepareStatement(select);
                statement.setString(1, "%" + query.getShouldContain().trim().toLowerCase() + "%");
                statement.setString(2, "%" + query.getShouldContain().trim().toLowerCase() + "%");
                var rs = statement.executeQuery();
                while (rs.next()) {
                    Question question = Question.builder()
                            .id(QuestionId.from(rs.getString("idQuestion")))
                            .author(CredentialId.from(rs.getString("idxCredential")))
                            .title(rs.getString("title"))
                            .description(rs.getString("description"))
                            .creation((rs.getTimestamp("instant")).toInstant())
                            .selectedAnswer(AnswerId.from(rs.getString("idxSelectedAnswer")))
                            .build();
                    result.add(question);
                }
                return result;
            } catch (SQLException ex) {
                ex.printStackTrace();
                Logger.getLogger("JDBC").log(Level.WARNING, "Could not findAll()");
                return Collections.emptyList();
            }
        } else {
            return findAll();
        }
    }

    @Override
    public void save(Question question) {
        setup(dataSource);
        var insert = "INSERT INTO Question(idQuestion, idxCredential, idxSelectedAnswer, title, description, instant)" +
                " VALUES (?, ?, ?, ?, ?, ?) ON CONFLICT (idQuestion) DO UPDATE SET idxSelectedAnswer = ?;";
        try (var connection = getDataSource().getConnection()) {
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
        try (var connection = getDataSource().getConnection()) {
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
        var select = "SELECT * FROM Question WHERE idQuestion = ?;";
        try (var connection = getDataSource().getConnection()) {
            var statement = connection.prepareStatement(select);
            statement.setString(1, questionId.toString());
            var rs = statement.executeQuery();

            if (rs.next()) {
                return Optional.of(Question.builder()
                        .id(QuestionId.from(rs.getString("idQuestion")))
                        .author(CredentialId.from(rs.getString("idxCredential")))
                        .title(rs.getString("title"))
                        .description(rs.getString("description"))
                        .creation((rs.getTimestamp("instant")).toInstant())
                        .selectedAnswer(AnswerId.from(rs.getString("idxSelectedAnswer")))
                        .build());
            } else {
                return Optional.empty();
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            Logger.getLogger("JDBC").log(Level.WARNING, "Could not find question " + questionId);
        }
        return Optional.empty();
    }

    @Override
    public Collection<Question> findAll() {
        setup(dataSource);
        var select = "SELECT * FROM Question;";
        Collection<Question> result = new ArrayList<>();
        try (var connection = getDataSource().getConnection()) {
            var statement = connection.prepareStatement(select);
            var rs = statement.executeQuery();
            while (rs.next()) {
                Question question = Question.builder()
                        .id(QuestionId.from(rs.getString("idQuestion")))
                        .author(CredentialId.from(rs.getString("idxCredential")))
                        .title(rs.getString("title"))
                        .description(rs.getString("description"))
                        .creation((rs.getTimestamp("instant")).toInstant())
                        .selectedAnswer(AnswerId.from(rs.getString("idxSelectedAnswer")))
                        .build();
                result.add(question);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            Logger.getLogger("JDBC").log(Level.WARNING, "Could not findAll()");
            return Collections.emptyList();
        }
        return result;
    }
}
