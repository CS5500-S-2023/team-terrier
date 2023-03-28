package edu.northeastern.cs5500.starterbot.database;

import edu.northeastern.cs5500.starterbot.model.Tuple;
import java.util.Collection;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/** Key should be a member of Tuple and uniquely identify the entire Tuple. */
public interface Database<K extends Object, T extends Tuple<K>> {
    /**
     * Creates the object in the database and returns it. Shouldn't modify the input in any way.
     * Doesn't create if object already exists.
     *
     * @param tuple
     * @return
     */
    @Nonnull
    public abstract T create(@Nonnull T tuple);

    /**
     * Gets the whole tuple with given primary key. Returns null if Key -> Tuple mapping doesn't
     * exist.
     */
    @Nullable
    public abstract T get(@Nonnull K key);

    /**
     * Gets all Tuples in the database.
     *
     * @return
     */
    @Nonnull
    public abstract Collection<T> getAll();

    /**
     * Update if and only if Tuple's key already exists. Doesn't create if Tuple wasn't created
     * explicitly.
     *
     * @param tuple
     * @return
     */
    @Nonnull
    public abstract T update(@Nonnull T tuple);

    /**
     * Delete tuple no matter if it were in the database.
     *
     * @param tuple
     */
    public abstract void delete(@Nonnull T tuple);
}
