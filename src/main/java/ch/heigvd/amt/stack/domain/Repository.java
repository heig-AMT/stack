package ch.heigvd.amt.stack.domain;

import java.util.Collection;
import java.util.Optional;

public interface Repository<Entity, Id> {

    void save(Entity entity);

    void remove(Id id);

    Optional<Entity> findById(Id id);

    Collection<Entity> findAll();
}
