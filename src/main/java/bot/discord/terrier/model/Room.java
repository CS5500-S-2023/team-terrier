package bot.discord.terrier.model;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nonnull;
import lombok.Data;
import org.bson.codecs.pojo.annotations.BsonId;

@Data
public class Room {

    /** Primary key */
    @Nonnull @BsonId private String roomName = "invalid";

    @Nonnull private List<Long> players = new ArrayList<>();

    /** Needed by MongoDB for deserialization. */
    public Room() {}

    public Room(@Nonnull String roomName) {
        this.roomName = roomName;
    }

    public int getSize() {
        return players.size();
    }
}
