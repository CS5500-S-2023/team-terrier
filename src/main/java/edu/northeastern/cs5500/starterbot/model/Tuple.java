package edu.northeastern.cs5500.starterbot.model;

import javax.annotation.Nonnull;

public interface Tuple<K extends Object> {
    /**
     * Each tuple should be able to specify a primary key that uniquely identifies this tuple.
     *
     * @return
     */
    @Nonnull
    public K getKey();
}
