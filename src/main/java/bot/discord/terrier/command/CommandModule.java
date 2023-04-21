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
import dagger.multibindings.IntoSet;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandGroupData;

@Module
public abstract interface CommandModule {
    // Groups.
    @Binds
    @IntoSet
    public SubcommandGroupData provideBankGroup(BankGroup group);

    @Binds
    @IntoSet
    public SubcommandGroupData provideRoomGroup(RoomGroup group);

    // Commands.
    @Binds
    @IntoSet
    public TerrierCommand provideWelcomeCommand(WelcomeCommand command);

    @Binds
    @IntoSet
    public SlashHandler provideWelcomeSlash(WelcomeCommand command);

    @Binds
    @IntoSet
    public ButtonHandler provideWelcomeButton(WelcomeCommand command);

    @Binds
    @IntoSet
    public TerrierCommand provideMeCommand(MeCommand command);

    @Binds
    @IntoSet
    public SlashHandler provideMeSlash(MeCommand command);

    @Binds
    @IntoSet
    public ButtonHandler provideMeButton(MeCommand command);

    @Binds
    @IntoSet
    public TerrierCommand provideBorrowCommand(BorrowCommand command);

    @Binds
    @IntoSet
    public SlashHandler provideBorrowSlash(BorrowCommand command);

    @Binds
    @IntoSet
    public TerrierCommand providePayCommand(PayCommand command);

    @Binds
    @IntoSet
    public SlashHandler providePaySlash(PayCommand command);

    @Binds
    @IntoSet
    public TerrierCommand provideListCommand(ListCommand command);

    @Binds
    @IntoSet
    public SlashHandler provideListSlash(ListCommand command);

    @Binds
    @IntoSet
    public ButtonHandler provideListButton(ListCommand command);

    @Binds
    @IntoSet
    public TerrierCommand provideStartCommand(StartCommand command);

    @Binds
    @IntoSet
    public SlashHandler provideStartSlash(StartCommand command);
}
