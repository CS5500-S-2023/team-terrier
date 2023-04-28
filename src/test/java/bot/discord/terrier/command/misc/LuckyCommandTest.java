package bot.discord.terrier.command.misc;

import bot.discord.terrier.command.CommandModule;
import bot.discord.terrier.dao.PlayerDao;
import bot.discord.terrier.dao.common.DaoTestModule;
import bot.discord.terrier.model.Player;
import com.google.common.truth.Truth;
import com.mongodb.client.MongoDatabase;
import dagger.Component;
import java.util.ArrayList;
import javax.inject.Singleton;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

@Component(modules = {CommandModule.class, DaoTestModule.class})
@Singleton
interface LuckyCommandComponent {
    public LuckyCommand command();

    public PlayerDao playerDao();

    public MongoDatabase database();
}

class LuckyCommandTest {
    private final LuckyCommandComponent component = DaggerLuckyCommandComponent.create();
    // Commands don't contain state.
    private final LuckyCommand command = component.command();
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
        SubcommandData descriptor = command.getDescriptor();
        Truth.assertThat(descriptor).isNotNull();
        Truth.assertThat(descriptor.getName()).isEqualTo("lucky");
        Truth.assertThat(command.getGroup()).isNull();
    }

    @Test
    void testSlashInteraction() {
        Player player = new Player(0);
        player.setCash(10000);
        playerDao.insertOrUpdate(player);

        Truth.assertThat(command.onSlashInteraction(0, new ArrayList<>()).getContent())
                .isNotEmpty();

        // To test that cash is actually changing randomly, we have to introduce a really small
        // percentage of flakiness here.
        Player updated = playerDao.getOrCreate(0);
        int count = 0;
        while (player.getCash() == updated.getCash()) {
            command.onSlashInteraction(0, new ArrayList<>());
            updated = playerDao.getOrCreate(0);
            count++;
        }

        // This has a 1E(-100) percentage of failing.
        Truth.assertThat(count).isLessThan(100);
    }
}
