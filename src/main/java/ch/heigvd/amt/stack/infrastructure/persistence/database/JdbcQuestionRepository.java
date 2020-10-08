package ch.heigvd.amt.stack.infrastructure.persistence.database;

import ch.heigvd.amt.stack.application.question.query.QuestionQuery;
import ch.heigvd.amt.stack.domain.question.Question;
import ch.heigvd.amt.stack.domain.question.QuestionId;
import ch.heigvd.amt.stack.domain.question.QuestionRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Alternative;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

@ApplicationScoped
@Alternative
public class JdbcQuestionRepository extends JdbcRepository<Question, QuestionId> implements QuestionRepository {

    @Override
    public Collection<Question> findBy(QuestionQuery query) {
        return Collections.emptyList();
    }

    @Override
    public void save(Question question) {

    }

    @Override
    public void remove(QuestionId questionId) {

    }

    @Override
    public Optional<Question> findById(QuestionId questionId) {
        return Optional.empty();
    }

    @Override
    public Collection<Question> findAll() {
        return Collections.emptyList();
    }
}
