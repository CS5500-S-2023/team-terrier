package bot.discord.terrier.command;

import bot.discord.terrier.dao.PlayerDao;
import bot.discord.terrier.dao.RoomDao;
import bot.discord.terrier.model.Room;
import java.util.List;
import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Singleton;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;
import net.dv8tion.jda.api.utils.messages.MessageCreateBuilder;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;

@Singleton
public class StartCommand implements TerrierCommand, SlashHandler {
    @Nonnull private static final String OPTION_NAME = "name";

    @Nonnull
    private static final SubcommandData DESCRIPTOR =
            new SubcommandData("start", "Woof! Terrier creates new rooms!")
                    .addOption(OptionType.STRING, OPTION_NAME, "Terrier gets the name!", false);

    @Inject RoomDao roomDao;
    @Inject PlayerDao PlayerDao;

    @Inject
    public StartCommand() {
        /** Injected default constructor */
    }

    @Override
    @Nonnull
    public SubcommandData getDescriptor() {
        return DESCRIPTOR;
    }

    @Override
    @Nonnull
    public MessageCreateData onSlashInteraction(
            long snowflakeId, @Nonnull List<OptionMapping> options) {
        String name = "";
        for (OptionMapping mapping : options) {
            if (OPTION_NAME.equals(mapping.getName())) {
                name = mapping.getAsString();
                break;
            }
        }

        if (roomDao.getRoomByName(name) == null) {
            Room room = new Room(name);
            room.getPlayers().add(snowflakeId);
            roomDao.insertOrUpdate(room);
            return new MessageCreateBuilder()
                    .setContent("Terrier creates a new room " + name + "!")
                    .build();
        }
        return new MessageCreateBuilder().setContent("This room has already been created.").build();
    }
}
