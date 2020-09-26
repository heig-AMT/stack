package ch.heigvd.amt.stack.domain.question;

import ch.heigvd.amt.stack.domain.Id;

import java.util.UUID;

public class QuestionId extends Id {

    public static QuestionId create() {
        return new QuestionId(UUID.randomUUID());
    }

    public static QuestionId from(UUID existing) {
        return new QuestionId(existing);
    }

    private QuestionId(UUID backing) {
        super(backing);
    }
}
