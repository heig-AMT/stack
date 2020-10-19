package ch.heigvd.amt.stack.infrastructure.persistence.memory;

import ch.heigvd.amt.stack.application.answer.query.AnswerQuery;
import ch.heigvd.amt.stack.domain.answer.Answer;
import ch.heigvd.amt.stack.domain.answer.AnswerId;
import ch.heigvd.amt.stack.domain.answer.AnswerRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Alternative;
import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

@ApplicationScoped
@Alternative
public class InMemoryAnswerRepository extends InMemoryRepository<Answer, AnswerId> implements AnswerRepository {

    @Override
    public Collection<Answer> findBy(AnswerQuery query) {
        if (query == null || query.getForQuestion() == null) {
            return Collections.emptyList();
        } else {
            return findAll().stream()
                    .filter(answer -> answer.getQuestion().equals(query.getForQuestion()))
                    .collect(Collectors.toList());
        }
    }
}
