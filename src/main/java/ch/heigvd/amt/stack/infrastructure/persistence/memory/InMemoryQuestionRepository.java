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

    public InMemoryQuestionRepository() {
        save(Question.builder()
                .title("Why doesn't this program run ?")
                .description("I made the following program : <html></html>, but it displays nothing. Is it expected ? I'm using Opera btw.")
                .author("admin")
                .build());
        save(Question.builder()
                .title("Can we inject an EJB in a JSP ?")
                .description("I have a semester project at HEIG-VD and still have not fully understood how beans work.")
                .author("admin")
                .build());
    }

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
