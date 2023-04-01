package edu.northeastern.cs5500.starterbot.command;

import com.google.common.truth.Truth;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import dagger.Component;
import edu.northeastern.cs5500.starterbot.dao.DaoTestModule;
import edu.northeastern.cs5500.starterbot.dao.PlayerDao;
import edu.northeastern.cs5500.starterbot.model.Player;
import java.util.ArrayList;
import javax.inject.Singleton;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.junit.jupiter.api.Test;

@Component(modules = {TerrierModule.class, DaoTestModule.class})
@Singleton
interface WelcomeCommandComponent {
    public WelcomeCommand command();

    public PlayerDao playerDao();

    public MongoDatabase database();
}

class WelcomeCommandTest {

    @Test
    void testWelcome() {
        WelcomeCommand command = DaggerWelcomeCommandComponent.create().command();
        Truth.assertThat(command.getGroup()).isNull();
        Truth.assertThat(command.getDescriptor().getName()).isEqualTo("welcome");
        Truth.assertThat(command.onSlashInteraction(0, new ArrayList<>()).getContent()).isNotNull();
    }

    @Test
    void testNewPlayer() {
        var component = DaggerWelcomeCommandComponent.create();
        var command = component.command();
        var players = component.database().getCollection("players", Player.class);
        players.deleteMany(Filters.exists("_id"));

        Truth.assertThat(players.countDocuments()).isEqualTo(0);
        Truth.assertThat(command.onSlashInteraction(0, new ArrayList<>()).getContent()).isNotNull();
        Truth.assertThat(players.countDocuments()).isEqualTo(1);
        Truth.assertThat(command.onSlashInteraction(0, new ArrayList<>()).getContent()).isNotNull();
        Truth.assertThat(players.countDocuments()).isEqualTo(1);
    }

    @Test
    void testInteractions() {
        var component = DaggerWelcomeCommandComponent.create();
        var command = component.command();
        var playerDao = component.playerDao();
        var players = component.database().getCollection("players", Player.class);
        players.deleteMany(Filters.exists("_id"));

        // Has the action row.
        Truth.assertThat(command.onSlashInteraction(0, new ArrayList<>()).getComponents())
                .isNotEmpty();
        Truth.assertThat(players.countDocuments()).isEqualTo(1);
        // Still appears.
        Truth.assertThat(command.onSlashInteraction(0, new ArrayList<>()).getComponents())
                .isNotEmpty();
        Truth.assertThat(players.countDocuments()).isEqualTo(1);

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
