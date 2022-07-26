package org.soma.everyonepick.groupalbum.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.soma.everyonepick.groupalbum.repository.PhotoRepository
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class PhotoModule {
    @Singleton
    @Provides
    fun providePhotoRepository(): PhotoRepository {
        return PhotoRepository()
    }
}