package org.soma.everyonepick.groupalbum.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.soma.everyonepick.groupalbum.data.repository.FriendRepository
import org.soma.everyonepick.groupalbum.data.repository.PhotoRepository
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
class RepositoryModule {
    @Singleton
    @Provides
    fun provideFriendRepository(): FriendRepository {
        return FriendRepository()
    }

    @Singleton
    @Provides
    fun providePhotoRepository(): PhotoRepository {
        return PhotoRepository()
    }
}