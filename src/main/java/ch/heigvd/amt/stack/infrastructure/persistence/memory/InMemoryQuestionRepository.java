package ch.heigvd.amt.stack.infrastructure.persistence.memory;

import ch.heigvd.amt.stack.application.question.QuestionQuery;
import ch.heigvd.amt.stack.domain.question.Question;
import ch.heigvd.amt.stack.domain.question.QuestionId;
import ch.heigvd.amt.stack.domain.question.QuestionRepository;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryQuestionRepository implements QuestionRepository {

    private final Map<QuestionId, Question> store = new ConcurrentHashMap<>();

    @Override
    public void save(Question question) {
        store.put(question.getId(), question);
    }

    @Override
    public void remove(QuestionId questionId) {
        store.remove(questionId);
    }

    @Override
    public Collection<Question> findBy(QuestionQuery query) {
        return findAll();
    }

    @Override
    public Optional<Question> findById(QuestionId questionId) {
        return Optional.ofNullable(store.get(questionId));
    }

    @Override
    public Collection<Question> findAll() {
        return Collections.unmodifiableCollection(store.values());
    }
}
