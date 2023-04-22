package bot.discord.terrier.model;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nonnull;
import lombok.Data;
import org.bson.codecs.pojo.annotations.BsonId;

@Data
public class Room {
    /**
     * Why initialize `roomName` with a magic string? 1. `roomName` should definitely be
     * **Nonnull**. 2. MongoDB POJO Codec needs a no-args constructor. So `roomName` always needs to
     * initialized with an arbitrary string.
     */
    @Nonnull @BsonId private String roomName = "INVALID";

    @Nonnull private List<Long> players = new ArrayList<>();

    // Needed by MongoDB for deserialization.
    public Room() {}

    public Room(@Nonnull String roomName) {
        this.roomName = roomName;
    }
}
