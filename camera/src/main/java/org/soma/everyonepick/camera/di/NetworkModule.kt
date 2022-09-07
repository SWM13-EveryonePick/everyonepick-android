package org.soma.everyonepick.camera.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.soma.everyonepick.camera.data.repository.PosePackRepository
import org.soma.everyonepick.camera.data.repository.PoseRepository
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class NetworkModule {
    @Singleton
    @Provides
    fun providePosePackRepository(): PosePackRepository {
        // TODO: RetrofitFactory.create(PosePackRepository::class.java)
        return PosePackRepository()
    }

    @Singleton
    @Provides
    fun providePoseRepository(): PoseRepository {
        // TODO: RetrofitFactory.create(PoseRepository::class.java)
        return PoseRepository()
    }
}