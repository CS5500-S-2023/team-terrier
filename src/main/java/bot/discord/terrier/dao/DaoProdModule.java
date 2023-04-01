package bot.discord.terrier.dao;

import com.mongodb.client.MongoDatabase;
import dagger.Module;
import dagger.Provides;

@Module
public class DaoProdModule {
    @Provides
    public MongoDatabase provideProdDatabase(ConnectionManager manager) {
        return manager.getProdDatabase();
    }
}
