package bot.discord.terrier.dao.common;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import com.mongodb.lang.NonNull;
import java.util.concurrent.ThreadLocalRandom;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Singleton;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

@Singleton
public class ConnectionManager {
    @Nonnull private static final String PROD_DB_NAME = "terrier";
    @Nonnull private static final String TEST_DB_PREFIX = "test-";

    @NonNull private final MongoClient client;
    @Nullable private MongoDatabase testDatabase = null;

    /** Sets up POJO codec for automatic translation between Java objects and MongoDB data types. */
    @Inject
    public ConnectionManager() {
        CodecRegistry codecRegistry =
                CodecRegistries.fromRegistries(
                        MongoClientSettings.getDefaultCodecRegistry(),
                        CodecRegistries.fromProviders(
                                PojoCodecProvider.builder().automatic(true).build()));

        MongoClientSettings settings =
                MongoClientSettings.builder()
                        .codecRegistry(codecRegistry)
                        .applyConnectionString(new ConnectionString(getMongoDBUrl()))
                        .build();

        client = MongoClients.create(settings);
    }

    /**
     * Tries to get connection string from environment variable. Fails fast if not set.
     *
     * @return connection string.
     */
    @Nonnull
    private String getMongoDBUrl() {
        String url = new ProcessBuilder().environment().get("MONGODB_URL");
        if (url == null || url.isEmpty()) {
            throw new IllegalArgumentException(
                    "The MONGODB_URL environment variable is not defined.");
        }
        return url;
    }

    /**
     * Get production database.
     *
     * @return
     */
    @NonNull
    public MongoDatabase getProdDatabase() {
        return client.getDatabase(PROD_DB_NAME);
    }

    /**
     * Get randomized test database.
     *
     * @return
     */
    @NonNull
    public MongoDatabase getTestDatabase() {
        if (testDatabase == null) {
            testDatabase =
                    client.getDatabase(TEST_DB_PREFIX + ThreadLocalRandom.current().nextInt());
        }
        return testDatabase;
    }
}
