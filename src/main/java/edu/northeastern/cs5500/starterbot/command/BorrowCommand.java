package edu.northeastern.cs5500.starterbot.command;

import com.mongodb.lang.Nullable;
import edu.northeastern.cs5500.starterbot.command.group.BankGroup;
import edu.northeastern.cs5500.starterbot.dao.PlayerDao;
import edu.northeastern.cs5500.starterbot.model.Player;
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
    @Inject
    public BorrowCommand() {
        /** Injected default constructor */
    }

    @Inject PlayerDao playerDao;

    @Nonnull private static final String OPTION_KEY = "amount";

    private static final double MAX_BORROW_AMOUNT = 10000;

    @Nonnull
    private static final SubcommandData DESCRIPTOR =
            new SubcommandData("borrow", "Terrier fetches money!")
                    .addOption(OptionType.NUMBER, OPTION_KEY, "Amount of money to borrow", true);

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
            double remain = MAX_BORROW_AMOUNT - player.getBorrowed();
            builder.setContent("That's too much... Remaining balance: " + remain);
        }
        return builder.build();
    }
}
