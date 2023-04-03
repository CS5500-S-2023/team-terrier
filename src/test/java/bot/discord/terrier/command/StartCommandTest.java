package bot.discord.terrier.command;

import bot.discord.terrier.dao.DaoTestModule;
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
}

public class StartCommandTest {
    private final StartCommand command = DaggerStartCommandComponent.create().command();
    private final RoomDao roomDao = DaggerStartCommandComponent.create().roomDao();

    @Test
    void testAttributes() {
        Truth.assertThat(command.getGroup()).isNull();
        Truth.assertThat(command.getDescriptor().getName()).isEqualTo("start");
    }

    @Test
    void testInteraction() {
        roomDao.clearAllRooms();
        Truth.assertThat(roomDao.countRooms()).isEqualTo(0);

        var reply = command.onSlashInteraction(0, new ArrayList<>());
        Truth.assertThat(reply.getContent()).isNotNull();
    }
}
