package ch.heigvd.amt.stack.domain.question;

import ch.heigvd.amt.stack.domain.authentication.CredentialId;
import org.junit.Assert;
import org.junit.Test;

public class QuestionTest {

    @Test
    public void testBuiltQuestionHasNonNullId() {
        var question = Question.builder()
                .title("Title")
                .description("Description")
                .author(CredentialId.create())
                .build();

        Assert.assertNotNull(question.getId());
    }

    @Test
    public void testBuiltQuestionsWithoutIdAreUnique() {
        var first = Question.builder().build();
        var second = Question.builder().build();

        Assert.assertNotEquals(first, second);
    }

    @Test
    public void testBuiltQuestionsWithIdCanBeSame() {
        var id = QuestionId.create();
        var first = Question.builder().id(id).build();
        var second = Question.builder().id(id).build();

        Assert.assertEquals(first, second);
    }
}
