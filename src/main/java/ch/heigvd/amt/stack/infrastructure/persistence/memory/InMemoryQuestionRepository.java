package ch.heigvd.amt.stack.infrastructure.persistence.memory;

import ch.heigvd.amt.stack.application.question.QuestionQuery;
import ch.heigvd.amt.stack.domain.question.Question;
import ch.heigvd.amt.stack.domain.question.QuestionId;
import ch.heigvd.amt.stack.domain.question.QuestionRepository;

import java.util.Collection;

public class InMemoryQuestionRepository extends InMemoryRepository<Question, QuestionId> implements QuestionRepository {

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
    public Collection<Question> findBy(QuestionQuery query) {
        return findAll();
    }
}
