package edu.northeastern.cs5500.starterbot.command;

import com.google.common.truth.Truth;
import dagger.Component;
import edu.northeastern.cs5500.starterbot.command.terrier.PayCommand;
import edu.northeastern.cs5500.starterbot.command.terrier.TerrierModule;
import java.util.ArrayList;
import javax.inject.Singleton;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;
import org.junit.Test;

@Component(modules = {TerrierModule.class})
@Singleton
interface PayCommandComponent {
    public PayCommand command();
}

public class PayCommandTest {

    private final PayCommand command = DaggerPayCommandComponent.create().command();

    @Test
    public void testDescriptor() {
        SubcommandData descriptor = command.getDescriptor();
        Truth.assertThat(descriptor.getName()).isNotEmpty();
        Truth.assertThat(descriptor.getDescription()).isNotEmpty();
        Truth.assertThat(descriptor.getOptions()).isNotEmpty();
    }

    @Test
    public void testGroup() {
        Truth.assertThat(command.getGroup()).isNotNull();
    }

    /**
     * Test for temporary unfinished pay function. This test would be rewritten once "terrier bank
     * pay" command is fully implemented.
     */
    @Test
    public void testInteraction() {
        Truth.assertThat(command.onSlashInteraction(0, new ArrayList<>())).isNotNull();
    }
}
