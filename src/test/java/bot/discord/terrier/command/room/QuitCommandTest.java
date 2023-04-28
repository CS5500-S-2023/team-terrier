package bot.discord.terrier.command.room;

import bot.discord.terrier.command.CommandModule;
import bot.discord.terrier.dao.PlayerDao;
import bot.discord.terrier.dao.RoomDao;
import bot.discord.terrier.dao.common.DaoTestModule;
import bot.discord.terrier.model.Player;
import bot.discord.terrier.model.Room;
import com.google.common.truth.Truth;
import com.mongodb.client.MongoDatabase;
import dagger.Component;
import java.util.ArrayList;
import javax.inject.Singleton;
import org.junit.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

@Component(modules = {CommandModule.class, DaoTestModule.class})
@Singleton
interface QuitCommandComponent {
    public QuitCommand command();

    public RoomDao roomDao();

    public PlayerDao playerDao();

    public MongoDatabase database();
}

class QuitCommandTest {
    private final QuitCommandComponent component = DaggerQuitCommandComponent.create();
    private final QuitCommand command = component.command();
    private final RoomDao roomDao = component.roomDao();
    private final PlayerDao playerDao = component.playerDao();
    private final MongoDatabase database = component.database();

    @BeforeEach
    @AfterEach
    void setup() {
        database.drop();
    }

    @Test
    void testAttributes() {
        Truth.assertThat(command.getGroup()).isNotNull();
        Truth.assertThat(command.getDescriptor().getName()).isEqualTo("quit");
    }

    @Test
    void testQuitRoom() {
        Player player = new Player(10);
        Room room = new Room("something");

        player.setRoomName("something");
        room.getPlayers().add(player.getSnowflakeId());

        playerDao.insertOrUpdate(player);
        roomDao.insertOrUpdate(room);

        command.onSlashInteraction(10, new ArrayList<>());

        player = playerDao.getOrCreate(10);
        room = roomDao.getRoomByName("something");

        Truth.assertThat(player.getRoomName()).isNull();
        Truth.assertThat(room.getPlayers()).isEmpty();
    }
}
