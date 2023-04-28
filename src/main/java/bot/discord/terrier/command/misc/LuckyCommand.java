package bot.discord.terrier.command.misc;

import bot.discord.terrier.command.common.SlashHandler;
import bot.discord.terrier.command.common.TerrierCommand;
import bot.discord.terrier.dao.PlayerDao;
import bot.discord.terrier.model.Player;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Singleton;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;
import net.dv8tion.jda.api.utils.messages.MessageCreateBuilder;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;

@Singleton
public class LuckyCommand implements TerrierCommand, SlashHandler {
    @Nonnull public static final String SUB_NAME = "lucky";

    @Nonnull
    private static final SubcommandData DESCRIPTOR = new SubcommandData(SUB_NAME, "Roll and win!");

    private static final double ENTRY_COST = 15;
    private static final int MIN_REWARD = 0;
    private static final int MAX_REWARD = 30;

    @Inject PlayerDao playerDao;

    @Inject
    LuckyCommand() {}

    @Override
    @Nonnull
    public SubcommandData getDescriptor() {
        return DESCRIPTOR;
    }

    @Override
    @Nonnull
    public MessageCreateData onSlashInteraction(
            long snowflakeId, @Nonnull List<OptionMapping> options) {
        MessageCreateBuilder builder = new MessageCreateBuilder();

        Player player = playerDao.getOrCreate(snowflakeId);
        double cash = player.getCash();
        if (cash < ENTRY_COST) {
            builder.setContent("Not enough cash to participate");
            return builder.build();
        }

        cash -= ENTRY_COST;
        int reward = ThreadLocalRandom.current().nextInt(MIN_REWARD, MAX_REWARD);
        cash += reward;

        player.setCash(cash);
        playerDao.insertOrUpdate(player);

        builder.setContent("You've received rewards: " + reward);
        return builder.build();
    }
}
