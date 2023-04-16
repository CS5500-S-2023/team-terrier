package bot.discord.terrier.command;

import bot.discord.terrier.dao.DaoTestModule;
import bot.discord.terrier.dao.PlayerDao;
import bot.discord.terrier.model.Player;
import com.google.common.truth.Truth;
import com.mongodb.client.MongoDatabase;
import dagger.Component;
import java.util.ArrayList;
import javax.inject.Singleton;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

@Component(modules = {TerrierModule.class, DaoTestModule.class})
@Singleton
interface BorrowCommandComponent {
    public BorrowCommand command();

    public PlayerDao playerDao();

    public MongoDatabase database();
}

class BorrowCommandTest {
    // All objects have to come from the same component to retain singleton property.
    BorrowCommandComponent component = DaggerBorrowCommandComponent.create();
    // Commands don't contain any state.
    private final BorrowCommand command = component.command();
    // DAOs don't contain any state.
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
        Truth.assertThat(command.getDescriptor().getName()).isEqualTo("borrow");
        Truth.assertThat(command.onSlashInteraction(0, new ArrayList<>()).getContent()).isNotNull();
    }

    @Test
    void testBorrow() {
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
