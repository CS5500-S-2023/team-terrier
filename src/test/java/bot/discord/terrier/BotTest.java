package bot.discord.terrier;

import com.google.common.truth.Truth;
import org.junit.jupiter.api.Test;

class BotTest {
    @Test
    void testBuild() {
        Bot bot = DaggerBotComponent.create().bot();
        Truth.assertThat(bot).isNotNull();
    }
}
