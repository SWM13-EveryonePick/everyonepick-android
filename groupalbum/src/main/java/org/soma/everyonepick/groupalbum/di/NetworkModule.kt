package org.soma.everyonepick.groupalbum.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.soma.everyonepick.groupalbum.data.source.remote.GroupAlbumPhotoService
import org.soma.everyonepick.groupalbum.data.source.remote.GroupAlbumPickService
import org.soma.everyonepick.groupalbum.domain.usecase.FriendUseCase
import org.soma.everyonepick.groupalbum.data.source.remote.GroupAlbumService
import retrofit2.Retrofit
import retrofit2.create
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
    fun provideGroupAlbumService(retrofit: Retrofit): GroupAlbumService {
        return retrofit.create(GroupAlbumService::class.java)
    }

    @Singleton
    @Provides
    fun provideGroupAlbumPickService(retrofit: Retrofit): GroupAlbumPickService {
        return retrofit.create(GroupAlbumPickService::class.java)
    }

    @Singleton
    @Provides
    fun provideGroupAlbumPhotoService(retrofit: Retrofit): GroupAlbumPhotoService {
        return retrofit.create(GroupAlbumPhotoService::class.java)
    }
}