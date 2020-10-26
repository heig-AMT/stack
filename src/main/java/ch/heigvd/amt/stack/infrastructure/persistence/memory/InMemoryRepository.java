package ch.heigvd.amt.stack.infrastructure.persistence.memory;

import ch.heigvd.amt.stack.domain.Entity;
import ch.heigvd.amt.stack.domain.Id;
import ch.heigvd.amt.stack.domain.Repository;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public abstract class InMemoryRepository<E extends Entity<I>, I extends Id> implements Repository<E, I> {

    private final Map<I, E> store = new ConcurrentHashMap<>();

    @Override
    public long size() {
        return store.size();
    }

    @Override
    public void save(E entity) {
        store.put(entity.getId(), entity);
    }

    @Override
    public void remove(I id) {
        store.remove(id);
    }

    @Override
    public Collection<E> findAll() {
        return store.values();
    }

    @Override
    public Optional<E> findById(I id) {
        return Optional.ofNullable(store.get(id));
    }
}
