package edu.northeastern.cs5500.starterbot.model;

import java.time.LocalDate;
import javax.annotation.Nonnull;
import lombok.Getter;
import lombok.ToString;

@ToString(includeFieldNames = true)
public class Player implements Tuple<Long> {

    private static final double DAILY_REWARD = 5000;
    private static final double MAX_BORROW_AMOUNT = 10000;

    /** Primary key. */
    private final long snowflakeId;

    private LocalDate lastClaimedDate = null;
    @Getter private double cash = 0;
    @Getter private double borrowed = 0;

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

    /*
     * Users get loans.
     */
    public boolean borrow(double amount) {
        if (borrowed + amount <= MAX_BORROW_AMOUNT) {
            cash += amount;
            borrowed += amount;
            return true;
        }
        return false;
    }

    /*
     * Users pay loan.
     */
    public boolean pay(double amount) {
        if (amount <= borrowed && amount <= cash) {
            cash -= amount;
            borrowed -= amount;
            return true;
        }
        return false;
    }
}
