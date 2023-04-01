package bot.discord.terrier.command;

import bot.discord.terrier.dao.DaoTestModule;
import bot.discord.terrier.dao.PlayerDao;
import bot.discord.terrier.model.Player;
import com.google.common.truth.Truth;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import dagger.Component;
import java.util.ArrayList;
import javax.inject.Singleton;
import org.junit.jupiter.api.Test;

@Component(modules = {TerrierModule.class, DaoTestModule.class})
@Singleton
interface BorrowCommandComponent {
    public BorrowCommand command();

    public MongoDatabase database();

    public PlayerDao playerDao();
}

class BorrowCommandTest {

    @Test
    void testAttributes() {
        BorrowCommandComponent component = DaggerBorrowCommandComponent.create();
        BorrowCommand command = component.command();

        Truth.assertThat(command.getGroup().getName()).isEqualTo("bank");
        Truth.assertThat(command.getDescriptor().getName()).isEqualTo("borrow");
        Truth.assertThat(command.onSlashInteraction(0, new ArrayList<>()).getContent()).isNotNull();
    }

    @Test
    void testBorrow() {
        // Prepare environment.
        BorrowCommandComponent component = DaggerBorrowCommandComponent.create();
        BorrowCommand command = component.command();
        PlayerDao playerDao = component.playerDao();
        MongoCollection<Player> players =
                component.database().getCollection("players", Player.class);
        players.deleteMany(Filters.exists("_id"));

        Truth.assertThat(players.countDocuments()).isEqualTo(0);
        command.tryBorrow(0, 1000);
        Truth.assertThat(players.countDocuments()).isEqualTo(1);
        Player player = playerDao.getOrCreate(0);
        Truth.assertThat(player.getCash()).isEqualTo(1000);
        Truth.assertThat(player.getBorrowed()).isEqualTo(1000);
    }
}
