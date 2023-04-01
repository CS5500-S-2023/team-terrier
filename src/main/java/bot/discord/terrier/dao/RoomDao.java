package bot.discord.terrier.dao;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Singleton;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import bot.discord.terrier.model.Room;

@Singleton
public class RoomDao {

    private static final String COLLECTION_NAME = "rooms";
    private static final ReplaceOptions UPSERT = new ReplaceOptions().upsert(true);

    private MongoCollection<Room> rooms;

    @Inject
    public RoomDao(@Nonnull MongoDatabase database) {
        rooms = database.getCollection(COLLECTION_NAME, Room.class);
    }

    @Nonnull
    public Room createRoom(String roomName) {
        Room room = new Room(roomName);
        rooms.insertOne(room);
        return room;
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
}