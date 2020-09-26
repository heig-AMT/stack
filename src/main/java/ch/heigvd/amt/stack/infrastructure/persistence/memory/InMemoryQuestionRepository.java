package ch.heigvd.amt.stack.infrastructure.persistence.memory;

import ch.heigvd.amt.stack.application.question.query.QuestionQuery;
import ch.heigvd.amt.stack.domain.question.Question;
import ch.heigvd.amt.stack.domain.question.QuestionId;
import ch.heigvd.amt.stack.domain.question.QuestionRepository;

import java.util.Collection;

public class InMemoryQuestionRepository extends InMemoryRepository<Question, QuestionId> implements QuestionRepository {

    @Override
    public Collection<Question> findBy(QuestionQuery query) {
        return findAll();
    }
}
