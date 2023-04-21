package bot.discord.terrier.command.room;

import bot.discord.terrier.command.TerrierModule;
import bot.discord.terrier.dao.DaoTestModule;
import bot.discord.terrier.dao.PlayerDao;
import bot.discord.terrier.dao.RoomDao;
import bot.discord.terrier.model.Player;
import bot.discord.terrier.model.Room;
import com.google.common.truth.Truth;
import com.mongodb.client.MongoDatabase;
import dagger.Component;
import java.util.ArrayList;
import javax.inject.Singleton;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

@Component(modules = {TerrierModule.class, DaoTestModule.class})
@Singleton
interface ListCommandComponent {
    public ListCommand command();

    public RoomDao roomDao();

    public PlayerDao playerDao();

    public MongoDatabase database();
}

class ListCommandTest {
    private final ListCommandComponent component = DaggerListCommandComponent.create();
    // Commands are stateless.
    private final ListCommand command = component.command();
    // DAOs are stateless.
    private final RoomDao roomDao = component.roomDao();
    private final PlayerDao playerDao = component.playerDao();
    private final MongoDatabase database = component.database();

    @BeforeEach
    @AfterEach
    void clearState() {
        database.drop();
    }

    @Test
    void testAttributes() {
        Truth.assertThat(command.getGroup()).isNotNull();
        Truth.assertThat(command.getDescriptor().getName()).isEqualTo("list");
    }

    @Test
    void testInteraction() {
        // No rooms to show.
        var reply = command.onSlashInteraction(0, new ArrayList<>());
        Truth.assertThat(reply.getComponents()).isEmpty();
        Truth.assertThat(reply.getContent()).contains("couldn't find");

        // One room to show.
        roomDao.insertOrUpdate(new Room("room0"));
        reply = command.onSlashInteraction(0, new ArrayList<>());
        Truth.assertThat(reply.getComponents()).isNotEmpty();
        Truth.assertThat(reply.getContent()).contains("found");
    }

    @Test
    void testGetByRegex() {
        // No rooms found.
        Truth.assertThat(command.getRooms(".*")).isEmpty();

        // 1 room found.
        roomDao.insertOrUpdate(new Room("room0"));
        Truth.assertThat(command.getRooms(".*")).hasSize(1);

        for (int i = 1; i < 5; i++) {
            roomDao.insertOrUpdate(new Room("other" + String.valueOf(i)));
        }
        Truth.assertThat(command.getRooms(".*")).hasSize(5);
        Truth.assertThat(command.getRooms("room")).hasSize(1);
        Truth.assertThat(command.getRooms("other")).hasSize(4);
    }

    @Test
    void testJoin() {
        // Normal join room.
        roomDao.insertOrUpdate(new Room("test"));
        Truth.assertThat(roomDao.countRooms()).isEqualTo(1);
        var reply = command.onButtonInteraction(0, Button.success("LIST_BUTTON_0", "test"));
        Truth.assertThat(reply.getContent()).contains("Added");
        Truth.assertThat(playerDao.countPlayers()).isEqualTo(1);
        Player player = playerDao.getOrCreate(0);
        Truth.assertThat(player.getRoomName()).isEqualTo("test");
        Room room = roomDao.getRoomByName("test");
        Truth.assertThat(room.getPlayers()).hasSize(1);

        // Already in-room player trying to join another room.
        reply = command.onButtonInteraction(0, Button.success("LIST_BUTTON_0", "other"));
        Truth.assertThat(reply.getContent()).contains("already");
        player = playerDao.getOrCreate(0);
        Truth.assertThat(player.getRoomName()).isEqualTo("test");
        room = roomDao.getRoomByName("test");
        Truth.assertThat(room.getPlayers()).hasSize(1);

        // Not-in-room player trying to join a non-existent room.
        reply = command.onButtonInteraction(1, Button.success("LIST_BUTTON_0", "other"));
        Truth.assertThat(reply.getContent()).contains("doesn't");
        // New player created.
        Truth.assertThat(playerDao.countPlayers()).isEqualTo(2);
        player = playerDao.getOrCreate(1);
        Truth.assertThat(player.getRoomName()).isNull();

        // Out of sync: player says he isn't in a room, but room says he is.
        // Should follow what the room says to prevent players from hacking.
        player = playerDao.getOrCreate(0);
        player.setRoomName(null);
        playerDao.insertOrUpdate(player);
        reply = command.onButtonInteraction(0, Button.success("LIST_BUTTON_0", "test"));
        Truth.assertThat(reply.getContent()).contains("sync");
        player = playerDao.getOrCreate(0);
        Truth.assertThat(player.getRoomName()).isEqualTo("test");
        room = roomDao.getRoomByName("test");
        Truth.assertThat(room.getPlayers()).hasSize(1);
    }
}
