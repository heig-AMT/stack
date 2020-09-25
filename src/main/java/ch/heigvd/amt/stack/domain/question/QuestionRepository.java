package ch.heigvd.amt.stack.domain.question;

import ch.heigvd.amt.stack.application.question.QuestionQuery;
import ch.heigvd.amt.stack.domain.Repository;

import java.util.Collection;

public interface QuestionRepository extends Repository<Question, QuestionId> {
    Collection<Question> findBy(QuestionQuery query);
}
