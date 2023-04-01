package bot.discord.terrier.command;

import bot.discord.terrier.dao.DaoTestModule;
import bot.discord.terrier.dao.PlayerDao;
import bot.discord.terrier.model.Player;
import com.google.common.truth.Truth;
import dagger.Component;
import java.util.ArrayList;
import javax.inject.Singleton;
import org.junit.jupiter.api.Test;

@Component(modules = {TerrierModule.class, DaoTestModule.class})
@Singleton
interface BorrowCommandComponent {
    public BorrowCommand command();

    public PlayerDao playerDao();
}

class BorrowCommandTest {
    // Commands don't contain any state.
    private final BorrowCommand command = DaggerBorrowCommandComponent.create().command();
    // DAO don't contain any state.
    private final PlayerDao playerDao = DaggerBorrowCommandComponent.create().playerDao();

    @Test
    void testAttributes() {
        Truth.assertThat(command.getGroup().getName()).isEqualTo("bank");
        Truth.assertThat(command.getDescriptor().getName()).isEqualTo("borrow");
        Truth.assertThat(command.onSlashInteraction(0, new ArrayList<>()).getContent()).isNotNull();
    }

    @Test
    void testBorrow() {
        // Clear database.
        playerDao.clearPlayers();

        Truth.assertThat(playerDao.countPlayers()).isEqualTo(0);
        command.tryBorrow(0, 1000);
        Truth.assertThat(playerDao.countPlayers()).isEqualTo(1);
        Player player = playerDao.getOrCreate(0);
        Truth.assertThat(player.getCash()).isEqualTo(1000);
        Truth.assertThat(player.getBorrowed()).isEqualTo(1000);

        command.tryBorrow(0, 100000);
        Truth.assertThat(playerDao.countPlayers()).isEqualTo(1);
        Player other = playerDao.getOrCreate(0);
        Truth.assertThat(player).isEqualTo(other);
    }
}
