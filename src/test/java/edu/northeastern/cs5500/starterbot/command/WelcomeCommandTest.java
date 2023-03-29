package edu.northeastern.cs5500.starterbot.command;

import com.google.common.truth.Truth;
import dagger.Component;
import edu.northeastern.cs5500.starterbot.command.terrier.TerrierModule;
import edu.northeastern.cs5500.starterbot.command.terrier.WelcomeCommand;
import edu.northeastern.cs5500.starterbot.database.Database;
import edu.northeastern.cs5500.starterbot.database.DatabaseModule;
import edu.northeastern.cs5500.starterbot.model.Player;
import java.util.ArrayList;
import javax.inject.Singleton;
import org.junit.jupiter.api.Test;

@Component(modules = {TerrierModule.class, DatabaseModule.class})
@Singleton
interface WelcomeCommandComponent {
    public WelcomeCommand command();

    public Database<Long, Player> database();
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
        WelcomeCommand command = component.command();
        Database<Long, Player> database = component.database();
        Truth.assertThat(database.getAll()).hasSize(0);
        Truth.assertThat(command.onSlashInteraction(0, new ArrayList<>()).getContent()).isNotNull();
        Truth.assertThat(database.getAll()).hasSize(1);
    }
}
