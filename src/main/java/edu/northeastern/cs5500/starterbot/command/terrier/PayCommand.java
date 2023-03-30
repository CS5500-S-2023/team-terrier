package edu.northeastern.cs5500.starterbot.command.terrier;

import edu.northeastern.cs5500.starterbot.command.terrier.group.BankGroup;
import edu.northeastern.cs5500.starterbot.database.Database;
import edu.northeastern.cs5500.starterbot.model.Player;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Singleton;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandGroupData;
import net.dv8tion.jda.api.utils.messages.MessageCreateBuilder;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;

@Singleton
public class PayCommand implements TerrierCommand, SlashHandler {
    @Inject
    public PayCommand() {
        /** Injected default constructor */
    }

    @Inject Database<Long, Player> players;

    @Nonnull private static final String OPTION_KEY = "amount";

    @Nonnull
    private static final SubcommandData DESCRIPTOR =
            new SubcommandData("pay", "Terrier Pays!")
                    .addOption(OptionType.NUMBER, OPTION_KEY, "Amount of money to pay", true);

    @Override
    @Nonnull
    public SubcommandData getDescriptor() {
        return DESCRIPTOR;
    }

    @Inject BankGroup group;

    @Override
    @Nullable
    public SubcommandGroupData getGroup() {
        return group;
    }

    @Override
    @Nonnull
    public MessageCreateData onSlashInteraction(
            long snowflakeId, @Nonnull List<OptionMapping> options) {
        double amount = 0.0;
        for (OptionMapping mapping : options) {
            if (OPTION_KEY.equals(mapping.getName())) {
                amount = mapping.getAsDouble();
                break;
            }
        }
        Player player = players.get(snowflakeId);
        MessageCreateBuilder builder = new MessageCreateBuilder();
        if (player == null) {
            builder.setContent("Terrier doesn't know who you are. Try command /terrier welcome");
        } else if (player.pay(amount)) {
            players.update(player);
            builder.setContent(
                    "Successfully paid "
                            + amount
                            + "!"
                            + " Remaining loan: "
                            + player.getBorrowed());
        } else {
            builder.setContent(
                    "Transaction failed. Current loan: "
                            + player.getBorrowed()
                            + ", current cash: "
                            + player.getCash());
        }
        return builder.build();
    }
}
