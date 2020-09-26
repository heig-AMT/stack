package ch.heigvd.amt.stack.domain.question;

import ch.heigvd.amt.stack.domain.authentication.SessionId;
import org.junit.Assert;
import org.junit.Test;

public class QuestionIdTest {

    @Test
    public void testCreatedQuestionIdsAreUnique() {
        var first = QuestionId.create();
        var second = QuestionId.create();

        Assert.assertNotEquals(first, second);
    }
}
