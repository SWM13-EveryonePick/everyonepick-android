package org.soma.everyonepick.groupalbum.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.soma.everyonepick.groupalbum.data.GroupAlbumRepository
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class GroupAlbumModule {
    @Singleton
    @Provides
    fun provideGroupAlbumRepository(): GroupAlbumRepository {
        return GroupAlbumRepository()
    }
}