package edu.northeastern.cs5500.starterbot.database;

import edu.northeastern.cs5500.starterbot.model.Tuple;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class InMemoryDatabase<K extends Object, T extends Tuple<K>> implements Database<K, T> {

    @Nonnull private Map<K, T> map = new HashMap<>();

    @Inject
    public InMemoryDatabase() {
        /** Default injected constructor */
    }

    @Override
    @Nonnull
    public T create(@Nonnull T tuple) {
        map.putIfAbsent(tuple.getKey(), tuple);
        return tuple;
    }

    @Override
    @Nullable
    public T get(@Nonnull K key) {
        return map.get(key);
    }

    @Override
    @Nonnull
    public Collection<T> getAll() {
        Collection<T> values = map.values();
        if (values == null) {
            return new ArrayList<>();
        }
        return values;
    }

    @Override
    @Nonnull
    public T update(@Nonnull T tuple) {
        map.computeIfPresent(tuple.getKey(), (key, value) -> tuple);
        return tuple;
    }

    @Override
    public void delete(@Nonnull T tuple) {
        map.remove(tuple.getKey());
    }
}
