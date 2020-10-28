package ch.heigvd.amt.stack.infrastructure.persistence.database;

import ch.heigvd.amt.stack.application.answer.query.CommentQuery;
import ch.heigvd.amt.stack.domain.answer.AnswerId;
import ch.heigvd.amt.stack.domain.authentication.CredentialId;
import ch.heigvd.amt.stack.domain.comment.Comment;
import ch.heigvd.amt.stack.domain.comment.CommentId;
import ch.heigvd.amt.stack.domain.comment.CommentRepository;

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
import java.util.stream.Collectors;

@ApplicationScoped
@Default
public class JdbcCommentRepository extends JdbcRepository<Comment, CommentId> implements CommentRepository {

    @Resource(name = "database")
    private DataSource dataSource;

    private DataSource getDataSource() {
        return dataSource;
    }

    @Override
    public Collection<Comment> findBy(CommentQuery query) {
        setup(dataSource);
        return findAll().stream()
                .filter(comment -> comment.getAnswer().equals(query.getForAnswer()))
                .collect(Collectors.toList());
    }

    @Override
    public void save(Comment comment) {
        setup(dataSource);
        var insert = "INSERT INTO Comment(idComment, idxCredential, idxAnswer, contents, instant) VALUES (?, ?, ?, ?, ?);";
        try (var connection = getDataSource().getConnection()) {
            var statement = connection.prepareStatement(insert);
            statement.setString(1, comment.getId().toString());
            statement.setString(2, comment.getCreator().toString());
            statement.setString(3, comment.getAnswer().toString());
            statement.setString(4, comment.getContents());
            statement.setTimestamp(5, Timestamp.from(comment.getCreation()));
            statement.execute();
        } catch (SQLException ex) {
            Logger.getLogger("JDBC").log(Level.WARNING, "Could not add comment " + comment.getId());
        }
    }

    @Override
    public void remove(CommentId commentId) {
        setup(dataSource);
        var delete = "DELETE FROM Comment WHERE idComment = ?;";
        try (var connection = getDataSource().getConnection()) {
            var statement = connection.prepareStatement(delete);
            statement.setString(1, commentId.toString());
            statement.execute();
        } catch (SQLException ex) {
            Logger.getLogger("JDBC").log(Level.WARNING, "Could not remove comment " + commentId);
        }
    }

    @Override
    public Optional<Comment> findById(CommentId commentId) {
        setup(dataSource);
        var select = "SELECT * FROM Comment WHERE idComment = ?;";
        try (var connection = getDataSource().getConnection()) {
            var statement = connection.prepareStatement(select);
            statement.setString(1, commentId.toString());
            var rs = statement.executeQuery();

            if (rs.next()) {
                return Optional.of(Comment.builder()
                        .id(CommentId.from(rs.getString("idComment")))
                        .answer(AnswerId.from(rs.getString("idxAnswer")))
                        .creator(CredentialId.from(rs.getString("idxCredential")))
                        .contents(rs.getString("contents"))
                        .creation(rs.getTimestamp("instant").toInstant())
                        .build());
            } else {
                return Optional.empty();
            }

        } catch (SQLException ex) {
            Logger.getLogger("JDBC").log(Level.WARNING, "Could not find comment " + commentId);
        }
        return Optional.empty();
    }

    @Override
    public Collection<Comment> findAll() {
        setup(dataSource);
        var select = "SELECT * FROM Comment;";
        Collection<Comment> result = new ArrayList<>();
        try (var connection = getDataSource().getConnection()) {
            var statement = connection.prepareStatement(select);
            var rs = statement.executeQuery();

            while (rs.next()) {
                Comment newC = Comment.builder()
                        .id(CommentId.from(rs.getString("idComment")))
                        .answer(AnswerId.from(rs.getString("idxAnswer")))
                        .creator(CredentialId.from(rs.getString("idxCredential")))
                        .contents(rs.getString("contents"))
                        .creation(rs.getTimestamp("instant").toInstant())
                        .build();
                result.add(newC);
            }
        } catch (SQLException ex) {
            Logger.getLogger("JDBC").log(Level.WARNING, "Could not findAll()");
            return Collections.emptyList();
        }
        return result;
    }
}
