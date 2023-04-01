package bot.discord.terrier.command;

import bot.discord.terrier.dao.DaoTestModule;
import bot.discord.terrier.dao.PlayerDao;
import bot.discord.terrier.model.Player;
import com.google.common.truth.Truth;
import dagger.Component;
import javax.inject.Singleton;
import org.junit.jupiter.api.Test;

@Component(modules = {TerrierModule.class, DaoTestModule.class})
@Singleton
interface PayCommandComponent {
    public PayCommand command();

    public PlayerDao playerDao();
}

class PayCommandTest {
    // Commands don't contain state.
    private final PayCommand command = DaggerPayCommandComponent.create().command();
    // DAO don't contain state.
    private final PlayerDao playerDao = DaggerPayCommandComponent.create().playerDao();

    @Test
    void testAttributes() {
        Truth.assertThat(command.getGroup().getName()).isEqualTo("bank");
        Truth.assertThat(command.getDescriptor().getName()).isEqualTo("pay");
    }

    @Test
    void testPay() {
        // Clear database.
        playerDao.clearPlayers();
        Truth.assertThat(playerDao.countPlayers()).isEqualTo(0);

        Truth.assertThat(command.tryPay(0, 0)).isNotNull();
        Truth.assertThat(playerDao.countPlayers()).isEqualTo(1);
        Truth.assertThat(command.tryPay(0, 100)).isNotNull();
        Player player = playerDao.getOrCreate(0);
        Truth.assertThat(player.getBorrowed()).isEqualTo(0);
    }
}
