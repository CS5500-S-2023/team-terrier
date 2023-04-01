package bot.discord.terrier.dao;

import bot.discord.terrier.model.Player;
import com.google.common.truth.Truth;
import dagger.Component;
import javax.inject.Singleton;
import org.junit.jupiter.api.Test;

@Component(modules = {DaoTestModule.class})
@Singleton
interface PlayerDaoComponent {
    public PlayerDao playerDao();
}

class PlayerDaoTest {
    // DAO don't contain any state.
    private final PlayerDao playerDao = DaggerPlayerDaoComponent.create().playerDao();

    @Test
    void testGetOrCreate() {
        // Clear database.
        playerDao.clearPlayers();
        Truth.assertThat(playerDao.countPlayers()).isEqualTo(0);

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
        // Clear database.
        playerDao.clearPlayers();
        Truth.assertThat(playerDao.countPlayers()).isEqualTo(0);

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
