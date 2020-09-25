package ch.heigvd.amt.stack.domain;

import lombok.EqualsAndHashCode;

import java.util.Objects;
import java.util.UUID;

@EqualsAndHashCode
public abstract class Id {

    private final UUID uuid;

    protected Id(UUID backing) {
        this.uuid = Objects.requireNonNull(backing);
    }

    @Override
    public String toString() {
        return uuid.toString();
    }
}
