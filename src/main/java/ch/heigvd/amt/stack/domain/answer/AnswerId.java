package ch.heigvd.amt.stack.domain.answer;
import ch.heigvd.amt.stack.domain.Id;
import ch.heigvd.amt.stack.domain.answer.AnswerId;

import java.util.UUID;

public class AnswerId extends Id {
    public static AnswerId create() {
        return new AnswerId(UUID.randomUUID());
    }
    public static AnswerId from(UUID existing) {
        return new AnswerId(existing);
    }
    public static AnswerId from(String strId) {
        return new AnswerId(UUID.fromString(strId));
    }

    private AnswerId(UUID backing) {
        super(backing);
    }
}
