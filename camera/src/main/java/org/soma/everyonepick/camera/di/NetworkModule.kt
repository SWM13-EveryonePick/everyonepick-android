package org.soma.everyonepick.camera.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.soma.everyonepick.camera.data.source.PoseService
import retrofit2.Retrofit
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class NetworkModule {
    @Singleton
    @Provides
    fun providePoseService(retrofit: Retrofit): PoseService {
        return retrofit.create(PoseService::class.java)
    }
}