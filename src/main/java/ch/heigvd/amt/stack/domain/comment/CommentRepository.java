package ch.heigvd.amt.stack.domain.comment;

import ch.heigvd.amt.stack.application.answer.query.CommentQuery;
import ch.heigvd.amt.stack.domain.Repository;

import java.util.Collection;

public interface CommentRepository extends Repository<Comment, CommentId> {
    Collection<Comment> findBy(CommentQuery query);
}
