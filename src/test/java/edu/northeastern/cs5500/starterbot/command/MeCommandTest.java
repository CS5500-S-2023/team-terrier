package edu.northeastern.cs5500.starterbot.command;

import com.google.common.truth.Truth;
import dagger.Component;
import edu.northeastern.cs5500.starterbot.command.terrier.MeCommand;
import edu.northeastern.cs5500.starterbot.command.terrier.TerrierModule;
import edu.northeastern.cs5500.starterbot.database.DatabaseModule;
import java.util.ArrayList;
import javax.inject.Singleton;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;
import org.junit.jupiter.api.Test;

@Component(modules = {TerrierModule.class, DatabaseModule.class})
@Singleton
interface MeCommandComponent {
    public MeCommand command();
}

class MeCommandTest {

    private final MeCommand command = DaggerMeCommandComponent.create().command();

    @Test
    void testDescriptor() {
        SubcommandData descriptor = command.getDescriptor();
        Truth.assertThat(descriptor).isNotNull();
        Truth.assertThat(descriptor.getName()).isEqualTo("me");
    }

    @Test
    void testGroup() {
        Truth.assertThat(command.getGroup()).isNull();
    }

    @Test
    void testInteraction() {
        Truth.assertThat(command.onSlashInteraction(0, new ArrayList<>()).getContent())
                .isNotEmpty();
    }
}
