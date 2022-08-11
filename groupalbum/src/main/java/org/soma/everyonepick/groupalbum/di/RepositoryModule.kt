package org.soma.everyonepick.groupalbum.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.soma.everyonepick.common.data.RetrofitFactory
import org.soma.everyonepick.groupalbum.domain.usecase.FriendUseCase
import org.soma.everyonepick.groupalbum.data.repository.GroupAlbumRepository
import org.soma.everyonepick.groupalbum.data.repository.PhotoRepository
import org.soma.everyonepick.groupalbum.domain.usecase.PhotoUseCase
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
class RepositoryModule {
    @Singleton
    @Provides
    fun provideFriendUseCase(): FriendUseCase {
        return FriendUseCase()
    }

    @Singleton
    @Provides
    fun providePhotoRepository(): PhotoRepository {
        return RetrofitFactory.create(PhotoRepository::class.java)
    }

    @Singleton
    @Provides
    fun provideGroupAlbumRepository(): GroupAlbumRepository {
        return RetrofitFactory.create(GroupAlbumRepository::class.java)
    }
}