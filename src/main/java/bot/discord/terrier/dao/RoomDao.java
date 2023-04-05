package bot.discord.terrier.dao;

import bot.discord.terrier.model.Room;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class RoomDao {

    private static final String COLLECTION_NAME = "rooms";
    private static final ReplaceOptions UPSERT = new ReplaceOptions().upsert(true);

    private MongoCollection<Room> rooms;

    @Inject
    public RoomDao(@Nonnull MongoDatabase database) {
        rooms = database.getCollection(COLLECTION_NAME, Room.class);
    }

    @Nullable
    public Room getRoomByName(String roomName) {
        return rooms.find(Filters.eq(roomName)).first();
    }

    public List<Room> getRoomsByRegex(String regex, int num) {
        return rooms.find(Filters.regex("_id", regex)).limit(num).into(new ArrayList<Room>());
    }

    public UpdateResult insertOrUpdate(Room room) {
        return rooms.replaceOne(Filters.eq(room.getRoomName()), room, UPSERT);
    }

    public DeleteResult clearRoom(String roomName) {
        return rooms.deleteOne(Filters.eq(roomName));
    }

    public DeleteResult clearAllRooms() {
        return rooms.deleteMany(Filters.exists("_id"));
    }

    public long countRooms() {
        return rooms.countDocuments();
    }
}
