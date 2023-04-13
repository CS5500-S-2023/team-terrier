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
        String full = player.toString();
        String pretty =
                full.substring(full.indexOf("(") + 1, full.indexOf(")")).replace(", ", "\n");
        builder.setContent(pretty);
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

    @Override
    @Nonnull
    public MessageCreateData onButtonInteraction(long snowflakeId, @Nonnull Button button) {
        switch (button.getLabel()) {
            case "Pay As Possible":
                return payAsPossible(snowflakeId);
            case "Borrow As Possible":
                return borrowAsPossible(snowflakeId);
            default:
                return new MessageCreateBuilder().setContent("Invalid input").build();
        }
    }

    @Nonnull
    public MessageCreateData payAsPossible(long snowflakeId) {
        Player player = playerDao.getOrCreate(snowflakeId);
        MessageCreateBuilder builder = new MessageCreateBuilder();
        double cash = player.getCash();
        double borrowed = player.getBorrowed();

        if (borrowed == 0) {
            return builder.setContent("There's no debts. No need to pay.").build();
        }
        if (cash == 0) {
            return builder.setContent("There's no cash. Transaction failed.").build();
        }

        if (cash >= borrowed) {
            player.setCash(cash - borrowed);
            player.setBorrowed(0);
            builder.setContent("Successfully clear all debts.");
        } else {
            player.setCash(0);
            player.setBorrowed(borrowed - cash);
            builder.setContent("Paid partially. Cash is now 0.");
        }
        playerDao.insertOrUpdate(player);
        return builder.build();
    }

    @Nonnull
    public MessageCreateData borrowAsPossible(long snowflakeId) {
        Player player = playerDao.getOrCreate(snowflakeId);
        MessageCreateBuilder builder = new MessageCreateBuilder();
        double borrowed = player.getBorrowed();
        double max = player.getMaxBorrow();

        if (borrowed < max) {
            player.setBorrowed(max);
            builder.setContent("Successfully borrow max amount.");
        } else {
            builder.setContent("Already borrowed max amount.");
        }
        playerDao.insertOrUpdate(player);
        return builder.build();
    }
}
