package bot.discord.terrier.dao;

import bot.discord.terrier.model.Player;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import com.mongodb.lang.NonNull;
import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class PlayerDao {
    @Nonnull private static final String COLLECTION_NAME = "players";
    @NonNull private static final ReplaceOptions UPSERT = new ReplaceOptions().upsert(true);

    @NonNull private MongoCollection<Player> players;

    @Inject
    public PlayerDao(@Nonnull MongoDatabase database) {
        players = database.getCollection(COLLECTION_NAME, Player.class);
    }

    /**
     * Gets an existing player or create new profile.
     *
     * @param snowflakeId
     * @return player with given snowflakeId.
     */
    @Nonnull
    public Player getOrCreate(long snowflakeId) {
        Player player = players.find(Filters.eq(snowflakeId)).first();
        if (player == null) {
            player = new Player(snowflakeId);
            players.insertOne(player);
        }
        return player;
    }

    /**
     * Sync player with persistent storage.
     *
     * @param player
     * @return
     */
    @NonNull
    public UpdateResult insertOrUpdate(Player player) {
        return players.replaceOne(Filters.eq(player.getSnowflakeId()), player, UPSERT);
    }

    /** Count players in collection. */
    public long countPlayers() {
        return players.countDocuments();
    }

    /**
     * Drop all players in collection.
     *
     * @return
     */
    @NonNull
    public DeleteResult clearPlayers() {
        return players.deleteMany(Filters.exists("_id"));
    }
}
