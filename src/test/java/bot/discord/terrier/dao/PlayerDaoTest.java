package bot.discord.terrier.dao;

import bot.discord.terrier.model.Player;
import com.google.common.truth.Truth;
import com.mongodb.client.MongoDatabase;
import dagger.Component;
import javax.inject.Singleton;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

@Component(modules = {DaoTestModule.class})
@Singleton
interface PlayerDaoComponent {
    public PlayerDao playerDao();

    public MongoDatabase database();
}

class PlayerDaoTest {
    private final PlayerDaoComponent component = DaggerPlayerDaoComponent.create();
    // DAOs don't contain any state.
    private final PlayerDao playerDao = component.playerDao();
    private final MongoDatabase database = component.database();

    @BeforeEach
    @AfterEach
    void clearState() {
        database.drop();
    }

    @Test
    void testGetOrCreate() {
        Player first = playerDao.getOrCreate(0);
        Truth.assertThat(playerDao.countPlayers()).isEqualTo(1);
        playerDao.getOrCreate(1);
        Truth.assertThat(playerDao.countPlayers()).isEqualTo(2);
        Player other = playerDao.getOrCreate(0);
        Truth.assertThat(first).isEqualTo(other);
        Truth.assertThat(playerDao.countPlayers()).isEqualTo(2);

        first.claimDailyReward();
        playerDao.insertOrUpdate(first);
        first = playerDao.getOrCreate(0);
        Truth.assertThat(playerDao.countPlayers()).isEqualTo(2);
        Truth.assertThat(first).isNotEqualTo(other);
        Truth.assertThat(first.getLastClaimedDate()).isNotNull();
    }

    @Test
    void testInsertOfUpdate() {
        Player first = new Player(0);
        playerDao.insertOrUpdate(first);
        Truth.assertThat(playerDao.countPlayers()).isEqualTo(1);
        first.setCash(500);
        playerDao.insertOrUpdate(first);
        Truth.assertThat(playerDao.countPlayers()).isEqualTo(1);
        Player other = playerDao.getOrCreate(0);
        Truth.assertThat(first).isEqualTo(other);
        Truth.assertThat(playerDao.countPlayers()).isEqualTo(1);
    }
}
