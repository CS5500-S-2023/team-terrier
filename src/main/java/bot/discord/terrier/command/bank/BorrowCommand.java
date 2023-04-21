package bot.discord.terrier.command.bank;

import bot.discord.terrier.command.common.SlashHandler;
import bot.discord.terrier.command.common.TerrierCommand;
import bot.discord.terrier.dao.PlayerDao;
import bot.discord.terrier.model.Player;
import com.mongodb.lang.Nullable;
import java.util.List;
import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Singleton;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandGroupData;
import net.dv8tion.jda.api.utils.messages.MessageCreateBuilder;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;

@Singleton
public class BorrowCommand implements TerrierCommand, SlashHandler {
    @Nonnull private static final String OPTION_KEY = "amount";

    @Nonnull
    private static final SubcommandData DESCRIPTOR =
            new SubcommandData("borrow", "Terrier fetches money!")
                    .addOption(OptionType.NUMBER, OPTION_KEY, "Amount of money to borrow", true);

    @Inject BankGroup group;
    @Inject PlayerDao playerDao;

    @Inject
    public BorrowCommand() {
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

        return tryBorrow(snowflakeId, amount);
    }

    @Nonnull
    public MessageCreateData tryBorrow(long snowflakeId, double amount) {
        Player player = playerDao.getOrCreate(snowflakeId);
        MessageCreateBuilder builder = new MessageCreateBuilder();
        if (player.borrow(amount)) {
            playerDao.insertOrUpdate(player);
            builder.setContent("Successfully borrowed: " + amount + "!");
        } else {
            builder.setContent("That's too much...");
        }
        return builder.build();
    }
}
