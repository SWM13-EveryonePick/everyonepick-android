package org.soma.everyonepick.groupalbum.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.soma.everyonepick.groupalbum.data.FriendRepository
import org.soma.everyonepick.groupalbum.data.FriendUseCase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class FriendModule {
    @Provides
    @Singleton
    fun provideFriendUseCase(friendRepository: FriendRepository) = FriendUseCase(friendRepository)

    @Provides
    @Singleton
    fun provideFriendRepository() = FriendRepository()
}