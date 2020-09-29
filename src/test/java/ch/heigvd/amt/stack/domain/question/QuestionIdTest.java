package ch.heigvd.amt.stack.domain.question;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class QuestionIdTest {

    @Test
    public void testCreatedQuestionIdsAreUnique() {
        var first = QuestionId.create();
        var second = QuestionId.create();

        assertNotEquals(first, second);
    }
}
