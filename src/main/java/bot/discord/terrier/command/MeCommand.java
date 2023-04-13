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
public class MeCommand implements TerrierCommand, SlashHandler, ButtonHandler {
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
        builder.setContent(player.getPrettyString());
        Button payBn = Button.success("payBn", "Pay As Possible");
        Button borrowBn = Button.success("borrowBn", "Borrow As Possible");

        builder.addActionRow(payBn, borrowBn);
        return builder.build();
    }

    @Override
    @Nonnull
    public List<String> getButtonNames() {
        return List.of("payBn", "borrowBn");
    }

    @SuppressWarnings("null")
    @Override
    @Nonnull
    public MessageCreateData onButtonInteraction(long snowflakeId, @Nonnull Button button) {
        switch (button.getId()) {
            case "payBn":
                return payAsPossible(snowflakeId);
            case "borrowBn":
                return borrowAsPossible(snowflakeId);
            default:
                return new MessageCreateBuilder().setContent("Invalid input").build();
        }
    }

    @Nonnull
    public MessageCreateData payAsPossible(long snowflakeId) {
        Player player = playerDao.getOrCreate(snowflakeId);
        MessageCreateBuilder builder = new MessageCreateBuilder();
        builder.setContent("Successfully paid: " + player.payAsPossible());
        builder.addContent("\n");
        builder.addContent(player.getPrettyString());
        playerDao.insertOrUpdate(player);
        return builder.build();
    }

    @Nonnull
    public MessageCreateData borrowAsPossible(long snowflakeId) {
        Player player = playerDao.getOrCreate(snowflakeId);
        MessageCreateBuilder builder = new MessageCreateBuilder();
        builder.setContent("Successfully borrowed: " + player.borrowAsPossible());
        builder.addContent("\n");
        builder.addContent(player.getPrettyString());
        playerDao.insertOrUpdate(player);
        return builder.build();
    }
}
