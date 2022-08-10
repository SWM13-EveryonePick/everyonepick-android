package org.soma.everyonepick.common.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.soma.everyonepick.common.data.api.RetrofitFactory
import org.soma.everyonepick.common.api.AuthService
import org.soma.everyonepick.common.data.repository.UserRepository
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class NetworkModule {
    @Singleton
    @Provides
    fun provideAuthService(): AuthService {
        return RetrofitFactory.create(AuthService::class.java)
    }

    @Singleton
    @Provides
    fun provideUserRepository(): UserRepository {
        return RetrofitFactory.create(UserRepository::class.java)
    }
}