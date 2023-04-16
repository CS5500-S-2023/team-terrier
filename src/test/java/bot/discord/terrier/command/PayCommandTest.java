package bot.discord.terrier.command;

import bot.discord.terrier.dao.DaoTestModule;
import bot.discord.terrier.dao.PlayerDao;
import bot.discord.terrier.model.Player;
import com.google.common.truth.Truth;
import com.mongodb.client.MongoDatabase;
import dagger.Component;
import javax.inject.Singleton;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

@Component(modules = {TerrierModule.class, DaoTestModule.class})
@Singleton
interface PayCommandComponent {
    public PayCommand command();

    public PlayerDao playerDao();

    public MongoDatabase database();
}

class PayCommandTest {
    private final PayCommandComponent component = DaggerPayCommandComponent.create();
    // Commands don't contain state.
    private final PayCommand command = component.command();
    // DAOs don't contain state.
    private final PlayerDao playerDao = component.playerDao();
    private final MongoDatabase database = component.database();

    @BeforeEach
    @AfterEach
    void clearState() {
        database.drop();
    }

    @Test
    void testAttributes() {
        Truth.assertThat(command.getGroup().getName()).isEqualTo("bank");
        Truth.assertThat(command.getDescriptor().getName()).isEqualTo("pay");
    }

    @Test
    void testPay() {
        Truth.assertThat(command.tryPay(0, 0)).isNotNull();
        Truth.assertThat(playerDao.countPlayers()).isEqualTo(1);
        Truth.assertThat(command.tryPay(0, 100)).isNotNull();
        Player player = playerDao.getOrCreate(0);
        Truth.assertThat(player.getBorrowed()).isEqualTo(0);
    }
}
