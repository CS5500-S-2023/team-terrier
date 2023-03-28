package edu.northeastern.cs5500.starterbot.command.terrier;

import java.util.List;
import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Singleton;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;
import net.dv8tion.jda.api.utils.messages.MessageCreateBuilder;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;

@Singleton
public class MeCommand implements TerrierCommand, SlashHandler {
    @Inject
    public MeCommand() {
        /** Injected default constructor */
    }

    @Nonnull
    private static final SubcommandData DESCRIPTOR =
            new SubcommandData("me", "Terrier sniffs you!");

    @Override
    @Nonnull
    public SubcommandData getDescriptor() {
        return DESCRIPTOR;
    }

    @Override
    @Nonnull
    public MessageCreateData onSlashInteraction(
            long snowflakeId, @Nonnull List<OptionMapping> options) {
        return new MessageCreateBuilder().setContent("Terrier doesn't recognize you yet!").build();
    }
}
