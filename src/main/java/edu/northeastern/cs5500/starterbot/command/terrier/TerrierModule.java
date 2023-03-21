package edu.northeastern.cs5500.starterbot.command.terrier;

import dagger.Module;
import dagger.Provides;
import dagger.multibindings.IntoSet;
import edu.northeastern.cs5500.starterbot.command.terrier.group.AccountGroup;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandGroupData;

@Module
public class TerrierModule {
    // Groups.
    @Provides
    @IntoSet
    public SubcommandGroupData provideAccountGroup(AccountGroup group) {
        return group;
    }

    // Commands.
    @Provides
    @IntoSet
    public TerrierCommand provideWelcomeCommand(WelcomeCommand command) {
        return command;
    }
}
