package bot.discord.terrier.model;

import com.google.common.truth.Truth;
import org.junit.jupiter.api.Test;

class RoomTest {
    @Test
    void testEquals() {
        Room room = new Room("room");
        Truth.assertThat(room).isEqualTo(room);
        Truth.assertThat(room).isEqualTo(new Room("room"));
        Truth.assertThat(room).isNotEqualTo(null);
        Truth.assertThat(room).isNotEqualTo(new Room(""));
    }

    @Test
    void testHashCode() {
        Truth.assertThat(new Room("room").hashCode()).isEqualTo(new Room("room").hashCode());
    }
}
