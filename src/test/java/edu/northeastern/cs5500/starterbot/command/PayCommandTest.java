package edu.northeastern.cs5500.starterbot.command;

import com.google.common.truth.Truth;
import dagger.Component;
import edu.northeastern.cs5500.starterbot.command.terrier.PayCommand;
import edu.northeastern.cs5500.starterbot.command.terrier.TerrierModule;
import edu.northeastern.cs5500.starterbot.database.Database;
import edu.northeastern.cs5500.starterbot.database.DatabaseModule;
import edu.northeastern.cs5500.starterbot.model.Player;
import java.util.ArrayList;
import javax.inject.Singleton;
import org.junit.Test;

@Component(modules = {TerrierModule.class, DatabaseModule.class})
@Singleton
interface PayCommandComponent {
    public PayCommand command();

    public Database<Long, Player> database();
}

public class PayCommandTest {

    @Test
    public void testPay() {
        PayCommand command = DaggerPayCommandComponent.create().command();
        Truth.assertThat(command.getGroup().getName()).isEqualTo("bank");
        Truth.assertThat(command.getDescriptor().getName()).isEqualTo("pay");
        Truth.assertThat(command.onSlashInteraction(0, new ArrayList<>()).getContent()).isNotNull();
    }
}
