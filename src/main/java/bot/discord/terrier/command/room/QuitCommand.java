package bot.discord.terrier.command.room;

import bot.discord.terrier.command.common.SlashHandler;
import bot.discord.terrier.command.common.TerrierCommand;
import bot.discord.terrier.dao.PlayerDao;
import bot.discord.terrier.dao.RoomDao;
import bot.discord.terrier.model.Player;
import bot.discord.terrier.model.Room;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandGroupData;
import net.dv8tion.jda.api.utils.messages.MessageCreateBuilder;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;

public class QuitCommand implements TerrierCommand, SlashHandler {
    @Nonnull public static final String SUB_NAME = "quit";

    @Nonnull
    private static final SubcommandData DESCRIPTOR = new SubcommandData(SUB_NAME, "Exit room");

    @Inject RoomGroup group;
    @Inject RoomDao roomDao;
    @Inject PlayerDao playerDao;

    @Inject
    QuitCommand() {}

    @Override
    @Nullable
    public SubcommandGroupData getGroup() {
        return group;
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
        MessageCreateBuilder builder = new MessageCreateBuilder();

        Player player = playerDao.getOrCreate(snowflakeId);
        String roomName = player.getRoomName();
        if (roomName == null) {
            builder.setContent("You aren't in any room...");
            return builder.build();
        }

        player.setRoomName(null);
        playerDao.insertOrUpdate(player);

        Room room = roomDao.getRoomByName(roomName);
        if (room == null) {
            builder.setContent("It seems you are out of sync. Removed you from room.");
            return builder.build();
        }

        room.getPlayers().remove(player.getSnowflakeId());
        roomDao.insertOrUpdate(room);

        builder.setContent("Successfully removed you from room!");
        return builder.build();
    }
}
