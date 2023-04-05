package bot.discord.terrier.command;

import bot.discord.terrier.dao.PlayerDao;
import bot.discord.terrier.dao.RoomDao;
import bot.discord.terrier.model.Player;
import bot.discord.terrier.model.Room;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Singleton;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.utils.messages.MessageCreateBuilder;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;

@Singleton
public class ListCommand implements TerrierCommand, SlashHandler, ButtonHandler {
    @Nonnull private static final String OPTION_NAME = "regex";

    @Nonnull
    private static final SubcommandData DESCRIPTOR =
            new SubcommandData("list", "Woof! Terrier looks for rooms!")
                    .addOption(OptionType.STRING, OPTION_NAME, "Terrier knows regex!", false);

    @SuppressWarnings("null")
    @Nonnull
    private static final List<String> BUTTON_NAMES =
            List.of(
                    "LIST_BUTTON_0",
                    "LIST_BUTTON_1",
                    "LIST_BUTTON_2",
                    "LIST_BUTTON_3",
                    "LIST_BUTTON_4");

    @Inject RoomDao roomDao;
    @Inject PlayerDao playerDao;

    @Inject
    public ListCommand() {
        /** Injected default constructor */
    }

    @Override
    @Nonnull
    public SubcommandData getDescriptor() {
        return DESCRIPTOR;
    }

    @Override
    @Nonnull
    public List<String> getButtonNames() {
        return BUTTON_NAMES;
    }

    @Override
    @Nonnull
    public MessageCreateData onSlashInteraction(
            long snowflakeId, @Nonnull List<OptionMapping> options) {
        String regex = ".*";
        for (OptionMapping mapping : options) {
            if (OPTION_NAME.equals(mapping.getName())) {
                regex = mapping.getAsString();
                break;
            }
        }
        MessageCreateBuilder builder =
                new MessageCreateBuilder().setContent("Terrier found these rooms:");
        for (Button button : getRooms(regex)) {
            builder.addActionRow(button);
        }
        if (builder.getComponents().isEmpty()) {
            builder.setContent("Terrier couldn't find any rooms!");
        }
        return builder.build();
    }

    @Nonnull
    public List<Button> getRooms(String regex) {
        List<Button> buttons = new ArrayList<>();
        var iterator = BUTTON_NAMES.iterator();
        for (Room room : roomDao.getRoomsByRegex(regex, BUTTON_NAMES.size())) {
            String name = iterator.next();
            if (name == null) {
                throw new NullPointerException();
            }
            buttons.add(Button.success(name, room.getRoomName()));
        }
        return buttons;
    }

    /** Handles user requests for joining a room. */
    @Override
    @Nonnull
    public MessageCreateData onButtonInteraction(long snowflakeId, @Nonnull Button button) {
        MessageCreateBuilder builder = new MessageCreateBuilder();
        String roomName = button.getLabel();

        // Check player state.
        Player player = playerDao.getOrCreate(snowflakeId);
        if (player.getRoomName() != null) {
            builder.setContent(
                    String.format("You're already in room \"%s\".", player.getRoomName()));
            return builder.build();
        }

        // Check room state in 2 steps.
        // Does room still exist?
        Room room = roomDao.getRoomByName(roomName);
        if (room == null) {
            builder.setContent(String.format("Room \"%s\" doesn't exist anymore.", roomName));
            return builder.build();
        }
        // Does the room contain the player even though the player says otherwise.
        // In other words, is the 2-way relationship out of sync?
        if (room.getPlayers().contains(snowflakeId)) {
            player.setRoomName(roomName);
            playerDao.insertOrUpdate(player);
            builder.setContent("You appear to be out of sync. Added you back to the room!");
            return builder.build();
        }

        // Sync 2 ways: room -> player, player -> room
        player.setRoomName(roomName);
        room.getPlayers().add(snowflakeId);
        playerDao.insertOrUpdate(player);
        roomDao.insertOrUpdate(room);
        builder.setContent(String.format("Added you to room \"%s\"!", roomName));

        return builder.build();
    }
}
