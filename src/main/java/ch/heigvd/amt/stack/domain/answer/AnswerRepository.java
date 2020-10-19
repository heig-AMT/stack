package ch.heigvd.amt.stack.domain.answer;

import ch.heigvd.amt.stack.application.answer.query.AnswerQuery;
import ch.heigvd.amt.stack.domain.Repository;

import java.util.Collection;

public interface AnswerRepository extends Repository<Answer, AnswerId> {
    Collection<Answer> findBy(AnswerQuery query);
}
