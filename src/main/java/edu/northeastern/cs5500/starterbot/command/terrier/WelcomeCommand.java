package edu.northeastern.cs5500.starterbot.command.terrier;

import edu.northeastern.cs5500.starterbot.database.Database;
import edu.northeastern.cs5500.starterbot.model.Player;
import java.util.List;
import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;
import net.dv8tion.jda.api.utils.messages.MessageCreateBuilder;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;

@Singleton
@Slf4j
public class WelcomeCommand implements TerrierCommand, SlashHandler {

    private static final String IMAGE_URL =
            "http://t2.gstatic.com/licensed-image?q=tbn:ANd9GcQwUa3xK4Y_m-j8mXtjHM_WZ0lE9nWvzSr6sA4rbfDacySYS4roE11ftbZh2ildPCtqBuJdL2cHMQhSLdU";

    @Inject Database<Long, Player> players;

    @Inject
    public WelcomeCommand() {
        /** Default injected constructor */
    }

    @Nonnull
    private static final SubcommandData DESCRIPTOR =
            new SubcommandData("welcome", "Terrier barks!");

    @Override
    @Nonnull
    public SubcommandData getDescriptor() {
        return DESCRIPTOR;
    }

    @Override
    @Nonnull
    public MessageCreateData onSlashInteraction(
            long snowflakeId, @Nonnull List<OptionMapping> options) {
        Player player = players.get(snowflakeId);
        if (player == null) {
            player = new Player(snowflakeId);
            players.create(player);
            log.info("New user: " + snowflakeId);
        }
        return new MessageCreateBuilder().setContent(IMAGE_URL).build();
    }
}
