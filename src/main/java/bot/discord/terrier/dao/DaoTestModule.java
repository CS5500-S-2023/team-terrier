package bot.discord.terrier.dao;

import com.mongodb.client.MongoDatabase;
import dagger.Module;
import dagger.Provides;

@Module
public class DaoTestModule {
    @Provides
    public MongoDatabase provideTestDatabase(ConnectionManager manager) {
        return manager.getTestDatabase();
    }
}
