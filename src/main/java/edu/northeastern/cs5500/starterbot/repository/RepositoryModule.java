package edu.northeastern.cs5500.starterbot.repository;

import dagger.Module;
import dagger.Provides;
import edu.northeastern.cs5500.starterbot.model.UserPreference;

/**
 * This module is the provider of repositories during runtime injection. It accomplishes this by
 * declaring an annotated function with a concrete subclass as input and the interface as the return
 * value.
 */
@Module
public class RepositoryModule {
    /**
     * Only providing in memory repos for now. TODO: Setup MongoDB environment.
     *
     * @param repository In memory repo.
     * @return
     */
    @Provides
    public GenericRepository<UserPreference> provideUserPreferencesRepository(
            InMemoryRepository<UserPreference> repository) {
        return repository;
    }
}
