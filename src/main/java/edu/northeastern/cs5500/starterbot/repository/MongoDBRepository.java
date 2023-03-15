package edu.northeastern.cs5500.starterbot.repository;

import static com.mongodb.client.model.Filters.eq;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import edu.northeastern.cs5500.starterbot.model.Model;
import edu.northeastern.cs5500.starterbot.service.MongoDBService;
import java.util.ArrayList;
import java.util.Collection;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Singleton;

import org.bson.types.ObjectId;

/**
 * Repository wrapper for MongoDB service.
 */
@Singleton
public class MongoDBRepository<T extends Model> implements GenericRepository<T> {

    private static final String MONGODB_ID_FIELD = "_id";

    private MongoCollection<T> collection;

    @Inject
    public MongoDBRepository(Class<T> clazz, MongoDBService mongoDBService) {
        MongoDatabase mongoDatabase = mongoDBService.getMongoDatabase();
        collection = mongoDatabase.getCollection(clazz.getName(), clazz);
    }

    @Nullable
    public T get(@Nonnull ObjectId id) {
        return collection.find(eq(MONGODB_ID_FIELD, id)).first();
    }

    @Override
    public T add(@Nonnull T item) {
        if (item.getId() == null) {
            item.setId(new ObjectId());
        }
        collection.insertOne(item);
        return item;
    }

    @Override
    public T update(@Nonnull T item) {
        return collection.findOneAndReplace(eq(MONGODB_ID_FIELD, item.getId()), item);
    }

    @Override
    public void delete(@Nonnull ObjectId id) {
        collection.deleteOne(eq(MONGODB_ID_FIELD, id));
    }

    @Override
    public Collection<T> getAll() {
        return collection.find().into(new ArrayList<>());
    }

    @Override
    public long count() {
        return collection.countDocuments();
    }
}
