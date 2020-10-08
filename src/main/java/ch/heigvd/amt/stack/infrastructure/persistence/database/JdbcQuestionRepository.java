package ch.heigvd.amt.stack.infrastructure.persistence.database;

import ch.heigvd.amt.stack.application.question.query.QuestionQuery;
import ch.heigvd.amt.stack.domain.authentication.AuthenticationFailedException;
import ch.heigvd.amt.stack.domain.authentication.Credential;
import ch.heigvd.amt.stack.domain.authentication.CredentialId;
import ch.heigvd.amt.stack.domain.question.Question;
import ch.heigvd.amt.stack.domain.question.QuestionId;
import ch.heigvd.amt.stack.domain.question.QuestionRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Alternative;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@ApplicationScoped
@Alternative
public class JdbcQuestionRepository extends JdbcRepository<Question, QuestionId> implements QuestionRepository {

    @Override
    public Collection<Question> findBy(QuestionQuery query) {
        if (query.getShouldContain() != null) {
            return findAll().stream()
                    .filter(question -> (
                            question.getTitle() != null && question.getTitle()
                                    .toLowerCase()
                                    .contains(query.getShouldContain().toLowerCase())
                    ) || (
                            question.getDescription() != null && question.getDescription()
                                    .toLowerCase()
                                    .contains(query.getShouldContain().toLowerCase())
                    ))
                    .collect(Collectors.toList());
        } else {
            return findAll();
        }
    }

    @Override
    public void save(Question question) {
        var insert = "INSERT INTO Question(idQuestion, idxCredential, resolved, title, description, instant)" +
                " VALUES (?, ?, ?, ?,?, ?);";
        try {
            var statement = getDataSource().getConnection().prepareStatement(insert);
            statement.setString(1, question.getId().toString());
            statement.setString(2, question.getAuthor().toString());
            statement.setBoolean(3, question.isResolved());
            statement.setString(4, question.getTitle());
            statement.setString(5, question.getDescription());
            statement.setTimestamp(6, Timestamp.from(question.getCreation()));
            statement.execute();
        } catch (SQLException ex) {
            // TODO : Make sure that duplicates are properly handed.
            throw new AuthenticationFailedException();
        }
    }

    @Override
    public void remove(QuestionId questionId) {
        var delete = "DELETE FROM Question WHERE idQuestion = ?;";
        try {
            var statement = getDataSource().getConnection().prepareStatement(delete);
            statement.setString(1, questionId.toString());
            statement.execute();
        } catch (SQLException ex) {
            Logger.getLogger("JDBC").log(Level.WARNING, "Could not remove question " + questionId);
        }
    }

    @Override
    public Optional<Question> findById(QuestionId questionId) {
        var select = "SELECT * FROM Question WHERE idQuestion = ?;";
        try {
            var statement = getDataSource().getConnection().prepareStatement(select);
            statement.setString(1, questionId.toString());
            var rs = statement.executeQuery();

            if (rs.next()) {
                return Optional.of(Question.builder()
                        .id(QuestionId.from(rs.getString("idQuestion")))
                        .author(CredentialId.from(rs.getString("idxCredential")))
                        .resolved(rs.getBoolean("resolved"))
                        .title(rs.getString("title"))
                        .description(rs.getString("description"))
                        .creation((rs.getTimestamp("instant")).toInstant())
                        .build());
            } else {
                return Optional.empty();
            }

        } catch (SQLException ex) {
            Logger.getLogger("JDBC").log(Level.WARNING, "Could not find question " + questionId);
        }
        return Optional.empty();
    }

    @Override
    public Collection<Question> findAll() {
        var select = "SELECT * FROM Question;";
        Collection<Question> result = new ArrayList<>();
        try {
            var statement = getDataSource().getConnection().prepareStatement(select);
            var rs = statement.executeQuery();
            while (rs.next()) {
                Question question = Question.builder()
                        .id(QuestionId.from(rs.getString("idQuestion")))
                        .author(CredentialId.from(rs.getString("idxCredential")))
                        .resolved(rs.getBoolean("resolved"))
                        .title(rs.getString("title"))
                        .description(rs.getString("description"))
                        .creation((rs.getTimestamp("instant")).toInstant())
                        .build();
                result.add(question);
            }
        } catch (SQLException ex) {
            Logger.getLogger("JDBC").log(Level.WARNING, "Could not findAll()");
            return Collections.emptyList();
        }
        return result;
    }
}
