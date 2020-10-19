package ch.heigvd.amt.stack.domain.answer;

import ch.heigvd.amt.stack.domain.authentication.CredentialId;
import ch.heigvd.amt.stack.domain.question.QuestionId;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

public class AnswerTest {

    @Test
    public void testBuiltAnswerHasNonNullId() {
        var answer = Answer.builder()
                .body("Title")
                .creator(CredentialId.create())
                .creation(Instant.EPOCH)
                .question(QuestionId.create())
                .build();

        assertNotNull(answer.getId());
    }

    @Test
    public void testBuiltAnswersWithoutIdAreUnique() {
        var first = Answer.builder().build();
        var second = Answer.builder().build();

        assertNotEquals(first, second);
    }

    @Test
    public void testBuiltAnswersWithIdCanBeSame() {
        var id = AnswerId.create();
        var first = Answer.builder().id(id).build();
        var second = Answer.builder().id(id).build();

        assertEquals(first, second);
    }

}
