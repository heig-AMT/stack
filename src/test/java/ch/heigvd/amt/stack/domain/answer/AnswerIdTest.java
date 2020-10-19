package ch.heigvd.amt.stack.domain.answer;

import ch.heigvd.amt.stack.domain.authentication.SessionId;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class AnswerIdTest {

    @Test
    public void testCreatedAnswerIdsAreUnique() {
        var first = AnswerId.create();
        var second = AnswerId.create();

        assertNotEquals(first, second);
    }

}
