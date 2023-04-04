package bot.discord.terrier.command;

import bot.discord.terrier.command.group.BankGroup;
import dagger.Module;
import dagger.Provides;
import dagger.multibindings.IntoSet;
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
    public ButtonHandler provideWelcomeButton(WelcomeCommand command) {
        return command;
    }

    @Provides
    @IntoSet
    public TerrierCommand provideMeCommand(MeCommand command) {
        return command;
    }

    @Provides
    @IntoSet
    public SlashHandler provideMeSlash(MeCommand command) {
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

    @Provides
    @IntoSet
    public TerrierCommand providePayCommand(PayCommand command) {
        return command;
    }

    @Provides
    @IntoSet
    public SlashHandler providePaySlash(PayCommand command) {
        return command;
    }

    @Provides
    @IntoSet
    public TerrierCommand provideListCommand(ListCommand command) {
        return command;
    }

    @Provides
    @IntoSet
    public SlashHandler provideListSlash(ListCommand command) {
        return command;
    }

    @Provides
    @IntoSet
    public ButtonHandler provideListButton(ListCommand command) {
        return command;
    }

    @Provides
    @IntoSet
    public TerrierCommand provideStartCommand(StartCommand command) {
        return command;
    }

    @Provides
    @IntoSet
    public SlashHandler provideStartSlash(StartCommand command) {
        return command;
    }
}
