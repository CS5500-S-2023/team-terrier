package edu.northeastern.cs5500.starterbot.command.terrier;

import com.mongodb.lang.Nullable;
import edu.northeastern.cs5500.starterbot.command.terrier.group.BankGroup;
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

    @Nonnull private static final String OPTION_KEY = "amount";

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
        return new MessageCreateBuilder()
                .setContent("Terrier doesn't know how to get money: " + amount)
                .build();
    }
}
