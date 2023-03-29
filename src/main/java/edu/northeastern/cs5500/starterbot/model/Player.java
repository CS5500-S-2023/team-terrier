package edu.northeastern.cs5500.starterbot.model;

import javax.annotation.Nonnull;

public class Player implements Tuple<Long> {

    /** Primary key. */
    private final long snowflakeId;

    public Player(long snowflakeId) {
        this.snowflakeId = snowflakeId;
    }

    @Override
    @Nonnull
    public Long getKey() {
        return snowflakeId;
    }
}
