package edu.northeastern.cs5500.starterbot.command;

import com.google.common.truth.Truth;
import dagger.Component;
import java.util.Set;
import javax.inject.Singleton;
import org.junit.jupiter.api.Test;

@Component(modules = {CommandModule.class})
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
