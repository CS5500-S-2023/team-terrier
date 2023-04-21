package bot.discord.terrier.command.misc;

import bot.discord.terrier.command.TerrierModule;
import bot.discord.terrier.dao.DaoTestModule;
import bot.discord.terrier.dao.PlayerDao;
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

@Component(modules = {TerrierModule.class, DaoTestModule.class})
@Singleton
interface MeCommandComponent {
    public MeCommand command();

    public PlayerDao playerDao();

    public MongoDatabase database();
}

class MeCommandTest {
    private final MeCommandComponent component = DaggerMeCommandComponent.create();
    // Commands don't contain state.
    private final MeCommand command = component.command();
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
        Truth.assertThat(descriptor.getName()).isEqualTo("me");
        Truth.assertThat(command.getGroup()).isNull();
    }

    @Test
    void testSlashInteraction() {
        Truth.assertThat(command.onSlashInteraction(0, new ArrayList<>()).getContent())
                .isNotEmpty();
        Truth.assertThat(playerDao.countPlayers()).isEqualTo(1);
    }

    @Test
    void testButtonInteraction() {
        playerDao.clearPlayers();
        Player player = new Player(0);
        player.setCash(1000);
        player.setBorrowed(5000);
        playerDao.insertOrUpdate(player);

        // test pay as possible
        Truth.assertThat(command.payAsPossible(0).getContent()).contains("1000");
        player = playerDao.getOrCreate(0);
        Truth.assertThat(player.getCash()).isEqualTo(0);
        Truth.assertThat(player.getBorrowed()).isEqualTo(4000);

        // test borrow as possible
        Truth.assertThat(command.borrowAsPossible(0).getContent()).contains("6000");
        player = playerDao.getOrCreate(0);
        Truth.assertThat(player.getCash()).isEqualTo(6000);
        Truth.assertThat(player.getBorrowed()).isEqualTo(10000);
    }
}
