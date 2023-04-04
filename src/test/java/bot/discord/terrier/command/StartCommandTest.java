package bot.discord.terrier.command;

import bot.discord.terrier.dao.DaoTestModule;
import bot.discord.terrier.dao.PlayerDao;
import bot.discord.terrier.dao.RoomDao;
import com.google.common.truth.Truth;
import dagger.Component;
import java.util.ArrayList;
import javax.inject.Singleton;
import org.junit.Test;

@Component(modules = {TerrierModule.class, DaoTestModule.class})
@Singleton
interface StartCommandComponent {
    public StartCommand command();

    public RoomDao roomDao();

    public PlayerDao playerDao();
}

public class StartCommandTest {
    private final StartCommand command = DaggerStartCommandComponent.create().command();
    private final RoomDao roomDao = DaggerStartCommandComponent.create().roomDao();
    private final PlayerDao playerDao = DaggerStartCommandComponent.create().playerDao();

    @Test
    void testAttributes() {
        Truth.assertThat(command.getGroup()).isNull();
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
        // Clear state.
        roomDao.clearAllRooms();
        Truth.assertThat(roomDao.countRooms()).isEqualTo(0);
        playerDao.clearPlayers();
        Truth.assertThat(playerDao.countPlayers()).isEqualTo(0);

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
