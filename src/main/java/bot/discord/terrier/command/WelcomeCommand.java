package bot.discord.terrier.command;

import bot.discord.terrier.dao.PlayerDao;
import bot.discord.terrier.model.Player;
import java.util.List;
import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Singleton;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.utils.messages.MessageCreateBuilder;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;

@Singleton
public class WelcomeCommand implements TerrierCommand, SlashHandler, ButtonHandler {

    private static final String IMAGE_URL =
            "http://t2.gstatic.com/licensed-image?q=tbn:ANd9GcQwUa3xK4Y_m-j8mXtjHM_WZ0lE9nWvzSr6sA4rbfDacySYS4roE11ftbZh2ildPCtqBuJdL2cHMQhSLdU";
    private static final String CLAIM_BUTTON_ID = "claim";

    @Inject PlayerDao playerDao;

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
        Player player = playerDao.getOrCreate(snowflakeId);
        MessageCreateBuilder builder = new MessageCreateBuilder().setContent(IMAGE_URL);
        if (player.eligibleForDailyReward()) {
            builder.addActionRow(Button.success(CLAIM_BUTTON_ID, "Claim daily rewards"));
        }
        return builder.build();
    }

    @Override
    @Nonnull
    public List<String> getButtonNames() {
        return List.of(CLAIM_BUTTON_ID);
    }

    @Override
    @Nonnull
    public MessageCreateData onButtonInteraction(long snowflakeId, @Nonnull Button button) {
        Player player = playerDao.getOrCreate(snowflakeId);

        MessageCreateBuilder builder =
                new MessageCreateBuilder().setContent("Already claimed today's rewards!");
        if (player.claimDailyReward()) {
            playerDao.insertOrUpdate(player);
            builder.setContent("Successfully claimed money!");
        }

        return builder.build();
    }
}
