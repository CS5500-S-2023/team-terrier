package bot.discord.terrier.dao;

import bot.discord.terrier.model.Player;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class PlayerDao {
    // Players will all be a collection with this name.
    private static final String COLLECTION_NAME = "players";
    // Upsert is MongoDB's term for update or insert.
    private static final ReplaceOptions UPSERT = new ReplaceOptions().upsert(true);

    private MongoCollection<Player> players;

    @Inject
    public PlayerDao(@Nonnull MongoDatabase database) {
        players = database.getCollection(COLLECTION_NAME, Player.class);
    }

    @Nonnull
    public Player getOrCreate(long snowflakeId) {
        Player player = players.find(Filters.eq(snowflakeId)).first();
        if (player == null) {
            player = new Player(snowflakeId);
            players.insertOne(player);
        }
        return player;
    }

    public UpdateResult insertOrUpdate(Player player) {
        return players.replaceOne(Filters.eq(player.getSnowflakeId()), player, UPSERT);
    }

    public long countPlayers() {
        return players.countDocuments();
    }

    public DeleteResult clearPlayers() {
        return players.deleteMany(Filters.exists("_id"));
    }
}
