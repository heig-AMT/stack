package ch.heigvd.amt.stack.domain.question;

import ch.heigvd.amt.stack.domain.authentication.CredentialId;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class QuestionTest {

    @Test
    public void testBuiltQuestionHasNonNullId() {
        var question = Question.builder()
                .title("Title")
                .description("Description")
                .author(CredentialId.create())
                .build();

        assertNotNull(question.getId());
    }

    @Test
    public void testBuiltQuestionsWithoutIdAreUnique() {
        var first = Question.builder().build();
        var second = Question.builder().build();

        assertNotEquals(first, second);
    }

    @Test
    public void testBuiltQuestionsWithIdCanBeSame() {
        var id = QuestionId.create();
        var first = Question.builder().id(id).build();
        var second = Question.builder().id(id).build();

        assertEquals(first, second);
    }
}
