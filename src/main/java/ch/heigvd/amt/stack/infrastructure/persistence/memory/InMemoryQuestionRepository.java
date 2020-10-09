package ch.heigvd.amt.stack.infrastructure.persistence.memory;

import ch.heigvd.amt.stack.application.question.query.QuestionQuery;
import ch.heigvd.amt.stack.domain.question.Question;
import ch.heigvd.amt.stack.domain.question.QuestionId;
import ch.heigvd.amt.stack.domain.question.QuestionRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Alternative;
import java.util.Collection;
import java.util.stream.Collectors;

@ApplicationScoped
@Alternative
public class InMemoryQuestionRepository extends InMemoryRepository<Question, QuestionId> implements QuestionRepository {

    @Override
    public Collection<Question> findBy(QuestionQuery query) {
        if (query.getShouldContain() != null) {
            return findAll().stream()
                    .filter(question -> (
                            question.getTitle() != null && question.getTitle()
                                    .toLowerCase()
                                    .contains(query.getShouldContain().toLowerCase())
                    ) || (
                            question.getDescription() != null && question.getDescription()
                                    .toLowerCase()
                                    .contains(query.getShouldContain().toLowerCase())
                    ))
                    .collect(Collectors.toList());
        } else {
            return findAll();
        }
    }
}
