package bot.discord.terrier.command;

import java.util.List;
import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Singleton;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;
import net.dv8tion.jda.api.utils.messages.MessageCreateBuilder;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;

@Singleton
public class ListCommand implements TerrierCommand, SlashHandler {
    @Inject
    public ListCommand() {
        /** Injected default constructor */
    }

    @Nonnull
    private static final SubcommandData DESCRIPTOR =
            new SubcommandData("list", "Wolf! Terrier is looking for rooms!");

    @Override
    @Nonnull
    public SubcommandData getDescriptor() {
        return DESCRIPTOR;
    }

    @Override
    @Nonnull
    public MessageCreateData onSlashInteraction(
            long snowflakeId, @Nonnull List<OptionMapping> options) {
        return new MessageCreateBuilder().setContent("Terrier doesn't have any room yet!").build();
    }
}
