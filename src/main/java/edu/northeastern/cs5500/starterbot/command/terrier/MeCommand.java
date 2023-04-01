package edu.northeastern.cs5500.starterbot.command.terrier;

import edu.northeastern.cs5500.starterbot.dao.PlayerDao;
import edu.northeastern.cs5500.starterbot.model.Player;
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

    @Inject PlayerDao playerDao;

    @Override
    @Nonnull
    public MessageCreateData onSlashInteraction(
            long snowflakeId, @Nonnull List<OptionMapping> options) {
        Player player = playerDao.getOrCreate(snowflakeId);
        MessageCreateBuilder builder = new MessageCreateBuilder();
        String full = player.toString();
        String pretty =
                full.substring(full.indexOf("(") + 1, full.indexOf(")")).replace(", ", "\n");
        builder.setContent(pretty);
        return builder.build();
    }
}
