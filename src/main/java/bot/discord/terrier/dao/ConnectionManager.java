package bot.discord.terrier.dao;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import java.util.concurrent.ThreadLocalRandom;
import javax.inject.Inject;
import javax.inject.Singleton;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

@Singleton
public class ConnectionManager {
    private static String getMongoDBUrl() {
        String url = new ProcessBuilder().environment().get("MONGODB_URL");
        if (url == null || url.isEmpty()) {
            throw new IllegalArgumentException(
                    "The MONGODB_URL environment variable is not defined.");
        }
        return url;
    }

    private static final String PROD_DB_NAME = "terrier";
    private static final String TEST_DB_PREFIX = "test-";

    private final MongoClient client;
    private MongoDatabase testDatabase = null;

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

    public MongoDatabase getProdDatabase() {
        return client.getDatabase(PROD_DB_NAME);
    }

    public MongoDatabase getTestDatabase() {
        if (testDatabase == null) {
            testDatabase =
                    client.getDatabase(TEST_DB_PREFIX + ThreadLocalRandom.current().nextInt());
        }
        return testDatabase;
    }
}
