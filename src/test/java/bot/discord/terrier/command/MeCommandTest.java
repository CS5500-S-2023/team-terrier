package bot.discord.terrier.command;

import bot.discord.terrier.dao.DaoTestModule;
import bot.discord.terrier.dao.PlayerDao;
import com.google.common.truth.Truth;
import dagger.Component;
import java.util.ArrayList;
import javax.inject.Singleton;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;
import org.junit.jupiter.api.Test;

@Component(modules = {TerrierModule.class, DaoTestModule.class})
@Singleton
interface MeCommandComponent {
    public MeCommand command();

    public PlayerDao playerDao();
}

class MeCommandTest {
    // Commands don't contain state.
    private final MeCommand command = DaggerMeCommandComponent.create().command();
    // DAO don't contain state.
    private final PlayerDao playerDao = DaggerMeCommandComponent.create().playerDao();

    @Test
    void testAttributes() {
        SubcommandData descriptor = command.getDescriptor();
        Truth.assertThat(descriptor).isNotNull();
        Truth.assertThat(descriptor.getName()).isEqualTo("me");
        Truth.assertThat(command.getGroup()).isNull();
    }

    @Test
    void testInteraction() {
        // Clear database.
        playerDao.clearPlayers();
        Truth.assertThat(playerDao.countPlayers()).isEqualTo(0);

        Truth.assertThat(command.onSlashInteraction(0, new ArrayList<>()).getContent())
                .isNotEmpty();
        Truth.assertThat(playerDao.countPlayers()).isEqualTo(1);
    }
}
