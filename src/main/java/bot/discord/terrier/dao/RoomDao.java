package bot.discord.terrier.dao;

import bot.discord.terrier.model.Room;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import com.mongodb.lang.NonNull;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class RoomDao {
    @Nonnull private static final String COLLECTION_NAME = "rooms";
    @NonNull private static final ReplaceOptions UPSERT = new ReplaceOptions().upsert(true);

    @NonNull private MongoCollection<Room> rooms;

    @Inject
    public RoomDao(@Nonnull MongoDatabase database) {
        rooms = database.getCollection(COLLECTION_NAME, Room.class);
    }

    /**
     * Returns the room that strictly matches the name.
     *
     * @param roomName
     * @return Room object containing players or null.
     */
    @Nullable
    public Room getRoomByName(String roomName) {
        return rooms.find(Filters.eq(roomName)).first();
    }

    /**
     * Returns the top |num| rooms with names matching the regex.
     *
     * @param regex
     * @param num
     * @return list of rooms that may be empty.
     */
    @NonNull
    public List<Room> getRoomsByRegex(String regex, int num) {
        return rooms.find(Filters.regex("_id", regex)).limit(num).into(new ArrayList<Room>());
    }

    /**
     * Sync this room into persistent storage.
     *
     * @param room
     * @return results of this transaction.
     */
    @NonNull
    public UpdateResult insertOrUpdate(Room room) {
        return rooms.replaceOne(Filters.eq(room.getRoomName()), room, UPSERT);
    }

    /**
     * Delete this room from persistent storage.
     *
     * @param roomName
     * @return results of this transaction.
     */
    @NonNull
    public DeleteResult clearRoom(String roomName) {
        return rooms.deleteOne(Filters.eq(roomName));
    }

    /**
     * Drop collection from database.
     *
     * @return results of this transaction.
     */
    @NonNull
    public DeleteResult clearAllRooms() {
        return rooms.deleteMany(Filters.exists("_id"));
    }

    /**
     * @return number of rooms in collection.
     */
    public long countRooms() {
        return rooms.countDocuments();
    }
}
