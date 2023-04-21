package bot.discord.terrier.dao;

import bot.discord.terrier.dao.common.DaoTestModule;
import bot.discord.terrier.model.Room;
import com.google.common.truth.Truth;
import com.mongodb.client.MongoDatabase;
import dagger.Component;
import java.util.List;
import javax.inject.Singleton;
import org.junit.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

@Component(modules = {DaoTestModule.class})
@Singleton
interface RoomDaoComponent {
    public RoomDao roomDao();

    public MongoDatabase database();
}

public class RoomDaoTest {
    private final RoomDaoComponent component = DaggerRoomDaoComponent.create();
    private final RoomDao roomDao = component.roomDao();
    private final MongoDatabase database = component.database();

    @BeforeEach
    @AfterEach
    void clearState() {
        database.drop();
    }

    @BeforeAll
    void setup() {
        roomDao.clearAllRooms();
        roomDao.insertOrUpdate(new Room("test1"));
        roomDao.insertOrUpdate(new Room("test2"));
    }

    @Test
    void testGetRooms() {
        List<Room> rooms = roomDao.getRoomsByRegex("test", 0);
        Truth.assertThat(rooms.size()).isEqualTo(2);
        Truth.assertThat(roomDao.getRoomByName("test1")).isEqualTo(1);
    }

    @Test
    void testInsertOrUpdate() {
        // test insert
        Truth.assertThat(roomDao.countRooms()).isEqualTo(2);

        // test update
        Room newRoom = new Room("test1");
        newRoom.getPlayers().add(0L);
        roomDao.insertOrUpdate(newRoom);
        Truth.assertThat(roomDao.getRoomByName("test1").getSize()).isEqualTo(1);
    }

    @Test
    void testClear() {
        roomDao.clearRoom("test1");
        Truth.assertThat(roomDao.countRooms()).isEqualTo(1);
    }
}
