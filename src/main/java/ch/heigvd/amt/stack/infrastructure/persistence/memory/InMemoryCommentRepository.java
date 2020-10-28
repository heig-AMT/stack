package ch.heigvd.amt.stack.infrastructure.persistence.memory;

import ch.heigvd.amt.stack.application.answer.query.CommentQuery;
import ch.heigvd.amt.stack.domain.comment.Comment;
import ch.heigvd.amt.stack.domain.comment.CommentId;
import ch.heigvd.amt.stack.domain.comment.CommentRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Alternative;
import java.util.Collection;
import java.util.stream.Collectors;

@ApplicationScoped
@Alternative
public class InMemoryCommentRepository extends InMemoryRepository<Comment, CommentId> implements CommentRepository {

    @Override
    public Collection<Comment> findBy(CommentQuery query) {
        return findAll().stream()
                .filter(comment -> comment.getAnswer().equals(query.getForAnswer()))
                .collect(Collectors.toList());
    }
}
