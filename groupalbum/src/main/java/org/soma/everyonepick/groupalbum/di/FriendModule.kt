package org.soma.everyonepick.groupalbum.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.soma.everyonepick.groupalbum.data.repository.FriendRepository
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class FriendModule {
    @Singleton
    @Provides
    fun provideFriendRepository(): FriendRepository {
        return FriendRepository()
    }
}