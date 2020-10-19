package ch.heigvd.amt.stack.domain.answer;

import ch.heigvd.amt.stack.domain.Id;

import java.util.UUID;

public class AnswerId extends Id {

    public static AnswerId create() {
        return new AnswerId(UUID.randomUUID());
    }

    public static AnswerId from(UUID backing) {
        return new AnswerId(backing);
    }

    public static AnswerId from(String backing) {
        return new AnswerId(UUID.fromString(backing));
    }

    protected AnswerId(UUID backing) {
        super(backing);
    }
}