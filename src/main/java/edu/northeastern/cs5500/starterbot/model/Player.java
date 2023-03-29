package edu.northeastern.cs5500.starterbot.model;

import java.time.LocalDate;
import javax.annotation.Nonnull;
import lombok.Getter;

public class Player implements Tuple<Long> {

    private static final double DAILY_REWARD = 5000;

    /** Primary key. */
    private final long snowflakeId;

    private LocalDate lastClaimedDate = null;
    @Getter private double cash = 0;

    public Player(long snowflakeId) {
        this.snowflakeId = snowflakeId;
    }

    @Override
    @Nonnull
    public Long getKey() {
        return snowflakeId;
    }

    public boolean eligibleForDailyReward() {
        return lastClaimedDate == null || lastClaimedDate.isBefore(LocalDate.now());
    }

    public boolean claimDailyReward() {
        if (!eligibleForDailyReward()) {
            return false;
        }
        cash += DAILY_REWARD;
        lastClaimedDate = LocalDate.now();
        return true;
    }
}
