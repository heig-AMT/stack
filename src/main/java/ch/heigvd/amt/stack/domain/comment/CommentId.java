package ch.heigvd.amt.stack.domain.comment;

import ch.heigvd.amt.stack.domain.AbstractId;

import java.util.UUID;

public class CommentId extends AbstractId {

    public static CommentId create() {
        return new CommentId(UUID.randomUUID());
    }

    public static CommentId from(UUID uuid) {
        return new CommentId(uuid);
    }

    public static CommentId from(String string) {
        return new CommentId(UUID.fromString(string));
    }

    private CommentId(UUID backing) {
        super(backing);
    }
}
