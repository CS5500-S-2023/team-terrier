package bot.discord.terrier.model;

import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import org.bson.codecs.pojo.annotations.BsonId;

@Data
public class Room {

    /** Primary key */
    @BsonId private String roomName;

    private List<Long> players;

    public Room(String roomName) {
        this.roomName = roomName;
        this.players = new ArrayList<>();
    }

    public int getSize() {
        return players.size();
    }
}
