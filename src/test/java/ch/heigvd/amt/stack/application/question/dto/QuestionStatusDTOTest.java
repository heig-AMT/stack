package ch.heigvd.amt.stack.application.question.dto;

import ch.heigvd.amt.stack.domain.answer.AnswerId;
import ch.heigvd.amt.stack.domain.question.Question;
import ch.heigvd.amt.stack.domain.question.QuestionId;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class QuestionStatusDTOTest {

    @Test
    public void testQuestionStatusQuestionCreatedInFutureIsNew() {
        var now = Instant.now();
        var future = now.plus(15, ChronoUnit.DAYS);
        var question = Question.builder()
                .id(QuestionId.create())
                .creation(future)
                .build();
        assertEquals(QuestionStatusDTO.New, QuestionStatusDTO.from(question, now));
    }

    @Test
    public void testQuestionStatusQuestionCreatedRecentlyIsNew() {
        var now = Instant.now();
        var creation = now.minus(3, ChronoUnit.HOURS);
        var question = Question.builder()
                .id(QuestionId.create())
                .creation(creation)
                .build();
        assertEquals(QuestionStatusDTO.New, QuestionStatusDTO.from(question, now));
    }

    @Test
    public void testResolvedQuestionMakesResolvedDTO() {
        var question = Question.builder()
                .selectedAnswer(AnswerId.create())
                .build();
        assertEquals(QuestionStatusDTO.Resolved, QuestionStatusDTO.from(question, Instant.now()));
    }

    @Test
    public void testOldQuestionIsOpen() {
        var now = Instant.now();
        var creation = now.minus(3, ChronoUnit.DAYS);
        var question = Question.builder()
                .id(QuestionId.create())
                .creation(creation)
                .build();
        assertEquals(QuestionStatusDTO.Open, QuestionStatusDTO.from(question, now));
    }
}
