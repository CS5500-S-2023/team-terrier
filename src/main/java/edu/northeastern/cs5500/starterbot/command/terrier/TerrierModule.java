package edu.northeastern.cs5500.starterbot.command.terrier;

import dagger.Module;
import dagger.Provides;
import dagger.multibindings.IntoSet;
import edu.northeastern.cs5500.starterbot.command.terrier.group.BankGroup;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandGroupData;

@Module
public class TerrierModule {
    // Groups.
    @Provides
    @IntoSet
    public SubcommandGroupData provideBankGroup(BankGroup group) {
        return group;
    }

    // Commands.
    @Provides
    @IntoSet
    public TerrierCommand provideWelcomeCommand(WelcomeCommand command) {
        return command;
    }

    @Provides
    @IntoSet
    public SlashHandler provideWelcomeSlash(WelcomeCommand command) {
        return command;
    }

    @Provides
    @IntoSet
    public TerrierCommand provideBorrowCommand(BorrowCommand command) {
        return command;
    }

    @Provides
    @IntoSet
    public SlashHandler provideBorrowSlash(BorrowCommand command) {
        return command;
    }
}
