package bot.discord.terrier.command.bank;

import bot.discord.terrier.command.common.SlashHandler;
import bot.discord.terrier.command.common.TerrierCommand;
import bot.discord.terrier.dao.PlayerDao;
import bot.discord.terrier.model.Player;
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
    @Nonnull public static final String SUB_NAME = "pay";
    @Nonnull private static final String OPTION_KEY = "amount";

    @Nonnull
    private static final SubcommandData DESCRIPTOR =
            new SubcommandData(SUB_NAME, "Terrier Pays!")
                    .addOption(OptionType.NUMBER, OPTION_KEY, "Amount of money to pay", true);

    @Inject PlayerDao playerDao;
    @Inject BankGroup group;

    @Inject
    public PayCommand() {
        /** Injected default constructor */
    }

    @Override
    @Nonnull
    public SubcommandData getDescriptor() {
        return DESCRIPTOR;
    }

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
        return tryPay(snowflakeId, amount);
    }

    @Nonnull
    public MessageCreateData tryPay(long snowflakeId, double amount) {
        Player player = playerDao.getOrCreate(snowflakeId);
        MessageCreateBuilder builder = new MessageCreateBuilder();
        if (player.pay(amount)) {
            playerDao.insertOrUpdate(player);
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
