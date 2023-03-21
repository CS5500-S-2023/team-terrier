package edu.northeastern.cs5500.starterbot.command;

import com.google.common.truth.Truth;
import dagger.Component;
import edu.northeastern.cs5500.starterbot.command.terrier.BorrowCommand;
import edu.northeastern.cs5500.starterbot.command.terrier.TerrierModule;
import java.util.ArrayList;
import javax.inject.Singleton;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;
import org.junit.jupiter.api.Test;

@Component(modules = {TerrierModule.class})
@Singleton
interface BorrowCommandComponent {
    public BorrowCommand command();
}

class BorrowCommandTest {

    private final BorrowCommand command = DaggerBorrowCommandComponent.create().command();

    @Test
    void testDescriptor() {
        SubcommandData descriptor = command.getDescriptor();
        Truth.assertThat(descriptor.getName()).isNotEmpty();
        Truth.assertThat(descriptor.getDescription()).isNotEmpty();
        Truth.assertThat(descriptor.getOptions()).isNotEmpty();
    }

    @Test
    void testGroup() {
        Truth.assertThat(command.getGroup()).isNotNull();
    }

    /** Unfinished for now since actual function isn't fully implemented. */
    @Test
    void testInteraction() {
        Truth.assertThat(command.onSlashInteraction(0, new ArrayList<>())).isNotNull();
    }
}
