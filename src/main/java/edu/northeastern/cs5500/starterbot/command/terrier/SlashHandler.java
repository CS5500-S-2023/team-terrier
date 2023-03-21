package edu.northeastern.cs5500.starterbot.command.terrier;

import java.util.List;
import javax.annotation.Nonnull;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;

/** This trait specifies that a command should handle slash interactions. */
public interface SlashHandler {
    /**
     * Specifies structure, format, metadata, etc.. of subcommand.
     *
     * @return
     */
    @Nonnull
    public abstract SubcommandData getDescriptor();

    /**
     * Callback that will be invoked if listener decides that the handler should interact. This is
     * the more test friendly version. May be subject to change in the future.
     *
     * @param snowflakeId
     * @param options
     * @return
     */
    @Nonnull
    public MessageCreateData onSlashInteraction(long snowflakeId, List<OptionMapping> options);
}
