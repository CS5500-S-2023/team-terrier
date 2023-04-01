package bot.discord.terrier.command;

import com.google.common.truth.Truth;
import dagger.Component;
import java.util.ArrayList;
import javax.inject.Singleton;
import org.junit.jupiter.api.Test;

@Component(modules = {TerrierModule.class})
@Singleton
interface ListCommandComponent {
    public ListCommand command();
}

class ListCommandTest {

    @Test
    void testList() {
        ListCommand command = DaggerListCommandComponent.create().command();
        Truth.assertThat(command.getGroup()).isNull();
        Truth.assertThat(command.getDescriptor().getName()).isEqualTo("list");
        Truth.assertThat(command.onSlashInteraction(0, new ArrayList<>()).getContent()).isNotNull();
    }
}