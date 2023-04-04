package bot.discord.terrier.command;

import bot.discord.terrier.dao.PlayerDao;
import bot.discord.terrier.dao.RoomDao;
import bot.discord.terrier.model.Player;
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
                    .addOption(OptionType.STRING, OPTION_NAME, "Terrier gets the name!", true);

    @Inject RoomDao roomDao;
    @Inject PlayerDao playerDao;

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
        String name = null;
        for (OptionMapping mapping : options) {
            if (OPTION_NAME.equals(mapping.getName())) {
                name = mapping.getAsString();
                break;
            }
        }
        if (name == null) {
            throw new NullPointerException();
        }

        return createNewRoom(snowflakeId, name);
    }

    @Nonnull
    public MessageCreateData createNewRoom(long snowflakeId, @Nonnull String name) {
        MessageCreateBuilder builder = new MessageCreateBuilder();

        // Check room state.
        Room room = roomDao.getRoomByName(name);
        if (room != null) {
            builder.setContent(String.format("Room \"%s\" already exists.", name));
            return builder.build();
        }

        // Check player state.
        Player player = playerDao.getOrCreate(snowflakeId);
        if (player.getRoomName() != null) {
            builder.setContent(
                    String.format(
                            "You're already in room \"%s\". Please leave that room first.",
                            player.getRoomName()));
            return builder.build();
        }

        // Create room in-memory.
        room = new Room(name);
        room.getPlayers().add(snowflakeId);
        player.setRoomName(name);
        // Sync to database.
        roomDao.insertOrUpdate(room);
        playerDao.insertOrUpdate(player);
        builder.setContent(String.format("Successfully created room \"%s\"", name));

        return builder.build();
    }
}
