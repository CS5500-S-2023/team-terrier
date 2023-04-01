package bot.discord.terrier.command;

import bot.discord.terrier.dao.DaoTestModule;
import com.google.common.truth.Truth;
import dagger.Component;
import java.util.ArrayList;
import javax.inject.Singleton;
import org.junit.Test;

@Component(modules = {TerrierModule.class, DaoTestModule.class})
@Singleton
interface PayCommandComponent {
    public PayCommand command();
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
