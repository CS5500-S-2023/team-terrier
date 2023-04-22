package bot.discord.terrier.model;

import java.time.LocalDate;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import lombok.Data;
import org.bson.codecs.pojo.annotations.BsonId;

@Data
public class Player {
    private static final double DAILY_REWARD = 5000;
    private static final double MAX_BORROW_AMOUNT = 10000;

    /** Primary key. Should be final but not possible for MongoDB deserialization. */
    @BsonId private long snowflakeId;

    @Nullable private LocalDate lastClaimedDate = null;
    private double cash = 0;
    private double borrowed = 0;
    @Nullable private String roomName = null;

    // Need this constructor for MongoDB deserialization.
    public Player() {}

    public Player(long snowflakeId) {
        this.snowflakeId = snowflakeId;
    }

    /**
     * Checks if player is eligible for daily reward.
     *
     * @return
     */
    public boolean eligibleForDailyReward() {
        // Nullable has been accounted for.
        return lastClaimedDate == null || lastClaimedDate.isBefore(LocalDate.now());
    }

    /**
     * Claims daily reward for player if eligible.
     *
     * @return whether a reward was claimed or not.
     */
    public boolean claimDailyReward() {
        if (!eligibleForDailyReward()) {
            return false;
        }
        cash += DAILY_REWARD;
        lastClaimedDate = LocalDate.now();
        return true;
    }

    /**
     * Tries to borrow the amount of money specified.
     *
     * @param amount
     * @return whether the transaction was successful or not.
     */
    public boolean borrow(double amount) {
        if (borrowed + amount <= MAX_BORROW_AMOUNT) {
            cash += amount;
            borrowed += amount;
            return true;
        }
        return false;
    }

    /**
     * Tries to pay the amount specified.
     *
     * @param amount
     * @return whether the transaction was successful or not.
     */
    public boolean pay(double amount) {
        if (amount <= borrowed && amount <= cash) {
            cash -= amount;
            borrowed -= amount;
            return true;
        }
        return false;
    }

    /**
     * Pay back money as much as possible.
     *
     * @return how much money was paid back.
     */
    public double payAsPossible() {
        double amount = Math.min(cash, borrowed);
        cash -= amount;
        borrowed -= amount;
        return amount;
    }

    /**
     * Borrow as much money as possible.
     *
     * @return how much money was borrowed.
     */
    public double borrowAsPossible() {
        double amount = MAX_BORROW_AMOUNT - borrowed;
        cash += amount;
        borrowed += amount;
        return amount;
    }

    /**
     * @return prettified string representation of player.
     */
    @Nonnull
    public String prettyString() {
        String full = this.toString();
        String prettied =
                full.substring(full.indexOf("(") + 1, full.indexOf(")")).replace(", ", "\n");
        if (prettied == null) {
            throw new NullPointerException(
                    "Prettied string representation of Player shouldn't be null");
        }
        return prettied;
    }
}
