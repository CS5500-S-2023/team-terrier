package edu.northeastern.cs5500.starterbot.command;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandGroupData;

/**
 * All terrier commands should be under the "terrier" namespace. Hence, these commands are
 * subcommands. Ideally, all subcommands should be part of a subcommand group, but this is optional.
 */
public interface TerrierCommand {
    /**
     * Specifies structure, format, metadata, etc.. of subcommand.
     *
     * @return
     */
    @Nonnull
    public abstract SubcommandData getDescriptor();

    /**
     * Specifies the command group that this command belongs to.
     *
     * @return
     */
    @Nullable
    public default SubcommandGroupData getGroup() {
        return null;
    }
}
