package org.soma.everyonepick.groupalbum.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.soma.everyonepick.common.data.RetrofitFactory
import org.soma.everyonepick.groupalbum.domain.usecase.FriendUseCase
import org.soma.everyonepick.groupalbum.data.source.remote.GroupAlbumService
import org.soma.everyonepick.groupalbum.data.source.remote.PhotoService
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
class NetworkModule {
    @Singleton
    @Provides
    fun provideFriendUseCase(): FriendUseCase {
        return FriendUseCase()
    }

    @Singleton
    @Provides
    fun providePhotoService(): PhotoService {
        return RetrofitFactory.create(PhotoService::class.java)
    }

    @Singleton
    @Provides
    fun provideGroupAlbumService(): GroupAlbumService {
        return RetrofitFactory.create(GroupAlbumService::class.java)
    }
}