package bot.discord.terrier.command.misc;

import bot.discord.terrier.command.common.ButtonHandler;
import bot.discord.terrier.command.common.SlashHandler;
import bot.discord.terrier.command.common.TerrierCommand;
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
public class MeCommand implements TerrierCommand, SlashHandler, ButtonHandler {
    @Nonnull public static final String SUB_NAME = "me";

    @Nonnull
    private static final SubcommandData DESCRIPTOR =
            new SubcommandData(SUB_NAME, "Terrier sniffs you!");

    @Nonnull private static final String PAY_BTN_KEY = "ME_PAY";
    @Nonnull private static final String BORROW_BTN_KEY = "ME_BORROW";

    @Inject PlayerDao playerDao;

    @Inject
    MeCommand() {}

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
        MessageCreateBuilder builder = new MessageCreateBuilder();
        builder.setContent(player.prettyString())
                .addActionRow(
                        Button.success(PAY_BTN_KEY, "Pay As Possible"),
                        Button.success(BORROW_BTN_KEY, "Borrow As Possible"));
        return builder.build();
    }

    // Obviously not null.
    @SuppressWarnings("null")
    @Override
    @Nonnull
    public List<String> getButtonNames() {
        return List.of(PAY_BTN_KEY, BORROW_BTN_KEY);
    }

    @Override
    @Nonnull
    public MessageCreateData onButtonInteraction(long snowflakeId, @Nonnull Button button) {
        String id = button.getId();
        if (id == null) {
            throw new NullPointerException("Button's id shouldn't be null");
        }
        switch (id) {
            case PAY_BTN_KEY:
                return payAsPossible(snowflakeId);
            case BORROW_BTN_KEY:
                return borrowAsPossible(snowflakeId);
            default:
                return new MessageCreateBuilder().setContent("Invalid input").build();
        }
    }

    /**
     * Pay back as much money as possible.
     *
     * @param snowflakeId player's id.
     * @return feedback.
     */
    @Nonnull
    public MessageCreateData payAsPossible(long snowflakeId) {
        Player player = playerDao.getOrCreate(snowflakeId);
        MessageCreateBuilder builder = new MessageCreateBuilder();
        builder.setContent("Successfully paid: " + player.payAsPossible());
        builder.addContent("\n");
        builder.addContent(player.prettyString());
        playerDao.insertOrUpdate(player);
        return builder.build();
    }

    /**
     * Borrow as much money as possible.
     *
     * @param snowflakeId player's id.
     * @return feedback.
     */
    @Nonnull
    public MessageCreateData borrowAsPossible(long snowflakeId) {
        Player player = playerDao.getOrCreate(snowflakeId);
        MessageCreateBuilder builder = new MessageCreateBuilder();
        builder.setContent("Successfully borrowed: " + player.borrowAsPossible());
        builder.addContent("\n");
        builder.addContent(player.prettyString());
        playerDao.insertOrUpdate(player);
        return builder.build();
    }
}
