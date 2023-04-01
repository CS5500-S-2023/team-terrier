package bot.discord.terrier.command;

import bot.discord.terrier.dao.DaoTestModule;
import bot.discord.terrier.dao.PlayerDao;
import com.google.common.truth.Truth;
import dagger.Component;
import java.util.ArrayList;
import javax.inject.Singleton;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.junit.jupiter.api.Test;

@Component(modules = {TerrierModule.class, DaoTestModule.class})
@Singleton
interface WelcomeCommandComponent {
    public WelcomeCommand command();

    public PlayerDao playerDao();
}

class WelcomeCommandTest {
    // Commands don't contain state.
    private final WelcomeCommand command = DaggerWelcomeCommandComponent.create().command();
    // DAO don't contain state.
    private final PlayerDao playerDao = DaggerWelcomeCommandComponent.create().playerDao();

    @Test
    void testAttributes() {
        Truth.assertThat(command.getGroup()).isNull();
        Truth.assertThat(command.getDescriptor().getName()).isEqualTo("welcome");
    }

    @Test
    void testNewPlayer() {
        // Clear database.
        playerDao.clearPlayers();
        Truth.assertThat(playerDao.countPlayers()).isEqualTo(0);

        Truth.assertThat(command.onSlashInteraction(0, new ArrayList<>()).getContent()).isNotNull();
        Truth.assertThat(playerDao.countPlayers()).isEqualTo(1);
        Truth.assertThat(command.onSlashInteraction(0, new ArrayList<>()).getContent()).isNotNull();
        Truth.assertThat(playerDao.countPlayers()).isEqualTo(1);
    }

    @Test
    void testInteractions() {
        // Clear database.
        playerDao.clearPlayers();
        Truth.assertThat(playerDao.countPlayers()).isEqualTo(0);

        // Has the action row.
        Truth.assertThat(command.onSlashInteraction(0, new ArrayList<>()).getComponents())
                .isNotEmpty();
        Truth.assertThat(playerDao.countPlayers()).isEqualTo(1);
        // Still appears.
        Truth.assertThat(command.onSlashInteraction(0, new ArrayList<>()).getComponents())
                .isNotEmpty();
        Truth.assertThat(playerDao.countPlayers()).isEqualTo(1);

        // Claims daily reward.
        Truth.assertThat(command.onButtonInteraction(0, Button.success("id", "label")).getContent())
                .contains("Successfully");
        // Cannot claim for a second/third time.
        Truth.assertThat(command.onButtonInteraction(0, Button.success("id", "label")).getContent())
                .contains("Already");
        Truth.assertThat(command.onButtonInteraction(0, Button.success("id", "label")).getContent())
                .contains("Already");

        // Database is updated.
        Truth.assertThat(playerDao.getOrCreate(0).getCash()).isEqualTo(5000);

        // No action row anymore.
        Truth.assertThat(command.onSlashInteraction(0, new ArrayList<>()).getComponents())
                .isEmpty();
    }
}