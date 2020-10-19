package ch.heigvd.amt.stack.infrastructure.persistence.memory;

import ch.heigvd.amt.stack.application.answer.query.AnswerQuery;
import ch.heigvd.amt.stack.domain.answer.Answer;
import ch.heigvd.amt.stack.domain.answer.AnswerId;
import ch.heigvd.amt.stack.domain.question.QuestionId;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Collectors;

public class InMemoryAnswerRepositoryTest {

    @Test
    public void testInMemoryAnswerFindByEmptyIsEmpty() {
        var repository = new InMemoryAnswerRepository();
        repository.save(Answer.builder().build());
        repository.save(Answer.builder().build());

        Assertions.assertIterableEquals(List.of(), repository.findBy(AnswerQuery.builder().build()));
        Assertions.assertIterableEquals(List.of(), repository.findBy(null));
    }

    @Test
    public void testInMemoryAnswerFindByWithIdFilters() {
        var repository = new InMemoryAnswerRepository();

        var answerIdIncluded = AnswerId.create();
        var questionIdIncluded = QuestionId.create();
        var answerIdExcluded = AnswerId.create();
        var questionIdExcluded = QuestionId.create();

        repository.save(Answer.builder()
                .question(questionIdIncluded)
                .id(answerIdIncluded)
                .build());

        repository.save(Answer.builder()
                .question(questionIdExcluded)
                .id(answerIdExcluded)
                .build());

        var result = repository.findBy(AnswerQuery.builder().forQuestion(questionIdIncluded).build())
                .stream()
                .map(Answer::getId)
                .collect(Collectors.toList());

        var expected = List.of(answerIdIncluded);

        Assertions.assertIterableEquals(expected, result);
    }
}
