package bot.discord.terrier.command;

import bot.discord.terrier.command.bank.BankGroup;
import bot.discord.terrier.command.bank.BorrowCommand;
import bot.discord.terrier.command.bank.PayCommand;
import bot.discord.terrier.command.common.ButtonHandler;
import bot.discord.terrier.command.common.SlashHandler;
import bot.discord.terrier.command.common.TerrierCommand;
import bot.discord.terrier.command.misc.MeCommand;
import bot.discord.terrier.command.misc.WelcomeCommand;
import bot.discord.terrier.command.room.ListCommand;
import bot.discord.terrier.command.room.RoomGroup;
import bot.discord.terrier.command.room.StartCommand;
import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;
import dagger.multibindings.IntoSet;
import dagger.multibindings.StringKey;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandGroupData;

/**
 * Commands are unqiuely identified by their subcommand name. We are utilizing Dagger multibinding
 * for compile-time collision detection.
 */
@Module
public interface CommandModule {
    // Groups.
    @Binds
    @IntoSet
    public SubcommandGroupData provideBankGroup(BankGroup group);

    @Binds
    @IntoSet
    public SubcommandGroupData provideRoomGroup(RoomGroup group);

    // Commands.
    @Binds
    @IntoMap
    @StringKey(WelcomeCommand.SUB_NAME)
    public TerrierCommand provideWelcomeCommand(WelcomeCommand command);

    @Binds
    @IntoMap
    @StringKey(WelcomeCommand.SUB_NAME)
    public SlashHandler provideWelcomeSlash(WelcomeCommand command);

    @Binds
    @IntoSet
    public ButtonHandler provideWelcomeButton(WelcomeCommand command);

    @Binds
    @IntoMap
    @StringKey(MeCommand.SUB_NAME)
    public TerrierCommand provideMeCommand(MeCommand command);

    @Binds
    @IntoMap
    @StringKey(MeCommand.SUB_NAME)
    public SlashHandler provideMeSlash(MeCommand command);

    @Binds
    @IntoSet
    public ButtonHandler provideMeButton(MeCommand command);

    @Binds
    @IntoMap
    @StringKey(BorrowCommand.SUB_NAME)
    public TerrierCommand provideBorrowCommand(BorrowCommand command);

    @Binds
    @IntoMap
    @StringKey(BorrowCommand.SUB_NAME)
    public SlashHandler provideBorrowSlash(BorrowCommand command);

    @Binds
    @IntoMap
    @StringKey(PayCommand.SUB_NAME)
    public TerrierCommand providePayCommand(PayCommand command);

    @Binds
    @IntoMap
    @StringKey(PayCommand.SUB_NAME)
    public SlashHandler providePaySlash(PayCommand command);

    @Binds
    @IntoMap
    @StringKey(ListCommand.SUB_NAME)
    public TerrierCommand provideListCommand(ListCommand command);

    @Binds
    @IntoMap
    @StringKey(ListCommand.SUB_NAME)
    public SlashHandler provideListSlash(ListCommand command);

    @Binds
    @IntoSet
    public ButtonHandler provideListButton(ListCommand command);

    @Binds
    @IntoMap
    @StringKey(StartCommand.SUB_NAME)
    public TerrierCommand provideStartCommand(StartCommand command);

    @Binds
    @IntoMap
    @StringKey(StartCommand.SUB_NAME)
    public SlashHandler provideStartSlash(StartCommand command);
}
