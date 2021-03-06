package ch.heigvd.amt.stack.domain.question;

import ch.heigvd.amt.stack.domain.AbstractId;

import java.util.UUID;

public class QuestionId extends AbstractId {
    public static QuestionId create() {
        return new QuestionId(UUID.randomUUID());
    }

    public static QuestionId from(UUID existing) {
        return new QuestionId(existing);
    }

    public static QuestionId from(String strId) {
        return new QuestionId(UUID.fromString(strId));
    }

    private QuestionId(UUID backing) {
        super(backing);
    }
}
