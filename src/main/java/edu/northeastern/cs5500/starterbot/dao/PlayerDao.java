package edu.northeastern.cs5500.starterbot.dao;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import com.mongodb.client.result.UpdateResult;
import edu.northeastern.cs5500.starterbot.model.Player;
import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class PlayerDao {
    private static final String COLLECTION_NAME = "players";

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

    private static final ReplaceOptions UPSERT = new ReplaceOptions().upsert(true);

    public UpdateResult insertOrUpdate(Player player) {
        return players.replaceOne(Filters.eq(player.getSnowflakeId()), player, UPSERT);
    }
}
