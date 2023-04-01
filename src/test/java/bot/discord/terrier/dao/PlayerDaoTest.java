package bot.discord.terrier.dao;

import bot.discord.terrier.model.Player;
import com.google.common.truth.Truth;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import dagger.Component;
import javax.inject.Singleton;
import org.junit.jupiter.api.Test;

@Component(modules = {DaoTestModule.class})
@Singleton
interface PlayerDaoComponent {
    public MongoDatabase database();

    public PlayerDao dao();
}

class PlayerDaoTest {

    @Test
    void testGetOrCreate() {
        // Prepare database environment.
        PlayerDaoComponent component = DaggerPlayerDaoComponent.create();
        MongoCollection<Player> players =
                component.database().getCollection("players", Player.class);
        players.deleteMany(Filters.exists("_id"));
        PlayerDao playerDao = component.dao();

        Truth.assertThat(players.countDocuments()).isEqualTo(0);
        Player first = playerDao.getOrCreate(0);
        Truth.assertThat(players.countDocuments()).isEqualTo(1);
        playerDao.getOrCreate(1);
        Truth.assertThat(players.countDocuments()).isEqualTo(2);
        Player other = playerDao.getOrCreate(0);
        Truth.assertThat(first).isEqualTo(other);

        first.claimDailyReward();
        playerDao.insertOrUpdate(first);
        first = playerDao.getOrCreate(0);
        Truth.assertThat(players.countDocuments()).isEqualTo(2);
        Truth.assertThat(first).isNotEqualTo(other);
    }

    @Test
    void testInsertOfUpdate() {
        // Prepare database environment.
        PlayerDaoComponent component = DaggerPlayerDaoComponent.create();
        MongoCollection<Player> players =
                component.database().getCollection("players", Player.class);
        players.deleteMany(Filters.exists("_id"));
        PlayerDao playerDao = component.dao();

        Truth.assertThat(players.countDocuments()).isEqualTo(0);
        Player first = new Player(0);
        playerDao.insertOrUpdate(first);
        Truth.assertThat(players.countDocuments()).isEqualTo(1);
        first.setCash(500);
        playerDao.insertOrUpdate(first);
        Truth.assertThat(players.countDocuments()).isEqualTo(1);
        Player other = playerDao.getOrCreate(0);
        Truth.assertThat(first).isEqualTo(other);
        Truth.assertThat(players.countDocuments()).isEqualTo(1);
    }
}
