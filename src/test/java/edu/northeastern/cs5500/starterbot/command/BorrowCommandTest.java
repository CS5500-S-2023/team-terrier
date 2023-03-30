package edu.northeastern.cs5500.starterbot.command;

import com.google.common.truth.Truth;
import dagger.Component;
import edu.northeastern.cs5500.starterbot.command.terrier.BorrowCommand;
import edu.northeastern.cs5500.starterbot.command.terrier.TerrierModule;
import edu.northeastern.cs5500.starterbot.database.Database;
import edu.northeastern.cs5500.starterbot.database.DatabaseModule;
import edu.northeastern.cs5500.starterbot.model.Player;
import java.util.ArrayList;
import javax.inject.Singleton;
import org.junit.jupiter.api.Test;

@Component(modules = {TerrierModule.class, DatabaseModule.class})
@Singleton
interface BorrowCommandComponent {
    public BorrowCommand command();

    public Database<Long, Player> database();
}

class BorrowCommandTest {

    @Test
    void testBorrow() {
        BorrowCommand command = DaggerBorrowCommandComponent.create().command();
        Truth.assertThat(command.getGroup().getName()).isEqualTo("bank");
        Truth.assertThat(command.getDescriptor().getName()).isEqualTo("borrow");
        Truth.assertThat(command.onSlashInteraction(0, new ArrayList<>()).getContent()).isNotNull();
    }
}
