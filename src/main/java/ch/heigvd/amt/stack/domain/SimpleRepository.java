package ch.heigvd.amt.stack.domain;

import java.util.Collection;
import java.util.Optional;

public interface SimpleRepository<Entity> {
    void save(Entity entity);

    void remove(Entity entity);

    Collection<Entity> findByCredential(Entity entity);
    Collection<Entity> findByAnswer(Entity entity);
}
