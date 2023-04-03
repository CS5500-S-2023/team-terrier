package bot.discord.terrier.command;

import bot.discord.terrier.dao.DaoTestModule;
import bot.discord.terrier.dao.RoomDao;
import bot.discord.terrier.model.Room;
import com.google.common.truth.Truth;
import dagger.Component;
import java.util.ArrayList;
import javax.inject.Singleton;
import org.junit.jupiter.api.Test;

@Component(modules = {TerrierModule.class, DaoTestModule.class})
@Singleton
interface ListCommandComponent {
    public ListCommand command();

    public RoomDao roomDao();
}

class ListCommandTest {
    // Commands are stateless.
    private final ListCommand command = DaggerListCommandComponent.create().command();
    // DAOs are stateless.
    private final RoomDao roomDao = DaggerListCommandComponent.create().roomDao();

    @Test
    void testAttributes() {
        Truth.assertThat(command.getGroup()).isNull();
        Truth.assertThat(command.getDescriptor().getName()).isEqualTo("list");
    }

    @Test
    void testInteraction() {
        // Clear collection.
        roomDao.clearAllRooms();
        Truth.assertThat(roomDao.countRooms()).isEqualTo(0);

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
        // Clear collection.
        roomDao.clearAllRooms();
        Truth.assertThat(roomDao.countRooms()).isEqualTo(0);

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
}
