package edu.northeastern.cs5500.starterbot.command;

import com.google.common.truth.Truth;
import dagger.Component;
import edu.northeastern.cs5500.starterbot.repository.RepositoryModule;
import java.util.Set;
import javax.inject.Singleton;
import org.junit.jupiter.api.Test;

@Component(modules = {CommandModule.class, RepositoryModule.class})
@Singleton
interface CommandModuleComponent {
    public Set<SlashCommandHandler> commands();
}

class CommandModuleTest {
    @Test
    void testInjectIntoSet() {
        Set<SlashCommandHandler> commands = DaggerCommandModuleComponent.create().commands();
        Truth.assertThat(commands).isNotNull();
        Truth.assertThat(commands).isNotEmpty();
    }
}
