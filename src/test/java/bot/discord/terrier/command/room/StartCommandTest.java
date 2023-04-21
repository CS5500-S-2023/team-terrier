package bot.discord.terrier.command.room;

import bot.discord.terrier.command.CommandModule;
import bot.discord.terrier.dao.DaoTestModule;
import bot.discord.terrier.dao.RoomDao;
import com.google.common.truth.Truth;
import com.mongodb.client.MongoDatabase;
import dagger.Component;
import java.util.ArrayList;
import javax.inject.Singleton;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

@Component(modules = {CommandModule.class, DaoTestModule.class})
@Singleton
interface StartCommandComponent {
    public StartCommand command();

    public RoomDao roomDao();

    public MongoDatabase database();
}

class StartCommandTest {
    private final StartCommandComponent component = DaggerStartCommandComponent.create();
    private final StartCommand command = component.command();
    private final RoomDao roomDao = component.roomDao();
    private final MongoDatabase database = component.database();

    @BeforeEach
    @AfterEach
    void setup() {
        database.drop();
    }

    @Test
    void testAttributes() {
        Truth.assertThat(command.getGroup()).isNotNull();
        Truth.assertThat(command.getDescriptor().getName()).isEqualTo("start");
    }

    @Test
    void testInteraction() {
        boolean thrown = false;
        try {
            command.onSlashInteraction(0, new ArrayList<>());
        } catch (NullPointerException e) {
            thrown = true;
        }
        Truth.assertThat(thrown).isTrue();
    }

    @Test
    void testCreateByName() {
        // Normal create.
        var reply = command.createNewRoom(0, "test");
        Truth.assertThat(reply.getContent()).contains("Successfully");
        Truth.assertThat(roomDao.countRooms()).isEqualTo(1);

        // Already in-room person trying to create another room.
        reply = command.createNewRoom(0, "other");
        Truth.assertThat(reply.getContent()).contains("already");
        Truth.assertThat(roomDao.countRooms()).isEqualTo(1);

        // Second person trying to create room with same name.
        reply = command.createNewRoom(1, "test");
        Truth.assertThat(reply.getContent()).contains("exists");
        Truth.assertThat(roomDao.countRooms()).isEqualTo(1);
    }
}
