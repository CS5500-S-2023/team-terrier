package edu.northeastern.cs5500.starterbot.database;

import dagger.Module;
import dagger.Provides;
import edu.northeastern.cs5500.starterbot.model.Player;

@Module
public class DatabaseModule {
    @Provides
    public Database<Long, Player> providePlayerDatabase(InMemoryDatabase<Long, Player> players) {
        return players;
    }
}
