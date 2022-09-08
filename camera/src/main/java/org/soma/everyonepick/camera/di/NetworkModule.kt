package org.soma.everyonepick.camera.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.soma.everyonepick.camera.data.source.PosePackService
import org.soma.everyonepick.camera.data.source.PoseService
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class NetworkModule {
    @Singleton
    @Provides
    fun providePosePackService(): PosePackService {
        // TODO: RetrofitFactory.create(PosePackRepository::class.java)
        return PosePackService()
    }

    @Singleton
    @Provides
    fun providePoseService(): PoseService {
        // TODO: RetrofitFactory.create(PoseRepository::class.java)
        return PoseService()
    }
}