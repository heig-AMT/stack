package ch.heigvd.amt.stack.infrastructure.persistence.database;

import ch.heigvd.amt.stack.application.answer.query.CommentQuery;
import ch.heigvd.amt.stack.domain.comment.Comment;
import ch.heigvd.amt.stack.domain.comment.CommentId;
import ch.heigvd.amt.stack.domain.comment.CommentRepository;

import javax.annotation.Resource;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Default;
import javax.sql.DataSource;
import java.util.Collection;
import java.util.Optional;
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

    }

    @Override
    public void remove(CommentId commentId) {

    }

    @Override
    public Optional<Comment> findById(CommentId commentId) {
        return Optional.empty();
    }

    @Override
    public Collection<Comment> findAll() {
        return null;
    }
}
