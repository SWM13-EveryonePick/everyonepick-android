package org.soma.everyonepick.groupalbum.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.soma.everyonepick.common.data.api.RetrofitFactory
import org.soma.everyonepick.groupalbum.api.GroupAlbumService
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class NetworkModule {
    @Singleton
    @Provides
    fun provideGroupAlbumService(): GroupAlbumService {
        return RetrofitFactory.create(GroupAlbumService::class.java)
    }
}