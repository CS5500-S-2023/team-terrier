package bot.discord.terrier.model;

import com.google.common.truth.Truth;
import org.junit.jupiter.api.Test;

class PlayerTest {
    @Test
    void testEquals() {
        Player player = new Player(0);
        Truth.assertThat(player).isEqualTo(player);
        Truth.assertThat(player).isEqualTo(new Player(0));
        Truth.assertThat(player).isNotEqualTo(null);
        Truth.assertThat(player).isNotEqualTo(new Player(1));
    }

    @Test
    void testHashCode() {
        Truth.assertThat(new Player(0).hashCode()).isEqualTo(new Player(0).hashCode());
    }
}
