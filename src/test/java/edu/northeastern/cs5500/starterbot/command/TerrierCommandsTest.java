package edu.northeastern.cs5500.starterbot.command;

import com.google.common.truth.Truth;
import dagger.Component;
import edu.northeastern.cs5500.starterbot.command.terrier.TerrierCommands;
import edu.northeastern.cs5500.starterbot.command.terrier.TerrierModule;
import javax.inject.Singleton;
import net.dv8tion.jda.api.interactions.commands.Command.Type;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import org.junit.jupiter.api.Test;

@Component(modules = {TerrierModule.class})
@Singleton
interface TerrierCommandsComponent {
    public TerrierCommands commands();
}

class TerrierCommandsTest {
    @Test
    void testDescriptors() {
        TerrierCommands commands = DaggerTerrierCommandsComponent.create().commands();
        Truth.assertThat(commands).isNotNull();
        CommandData descriptor = commands.getDescriptors();
        Truth.assertThat(descriptor).isNotNull();
        Truth.assertThat(descriptor.getName()).isEqualTo("terrier");
        Truth.assertThat(descriptor.getType()).isEqualTo(Type.SLASH);
    }
}
