package bot.discord.terrier.model;

import java.time.LocalDate;
import lombok.Data;
import org.bson.codecs.pojo.annotations.BsonId;

@Data
public class Player {

    private static final double DAILY_REWARD = 5000;
    private static final double MAX_BORROW_AMOUNT = 10000;

    /** Primary key. Should be final but not possible for MongoDB deserialization. */
    @BsonId private long snowflakeId;

    private LocalDate lastClaimedDate = null;
    private double cash = 0;
    private double borrowed = 0;
    private String roomName = null;

    // Need this constructor for MongoDB deserialization.
    public Player() {}

    public Player(long snowflakeId) {
        this.snowflakeId = snowflakeId;
    }

    public double getMaxBorrow() {
        return MAX_BORROW_AMOUNT;
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
