package org.soma.everyonepick.login.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.soma.everyonepick.common.api.Retrofit2Factory
import org.soma.everyonepick.login.api.AuthService
import org.soma.everyonepick.login.api.UserService
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class NetworkModule {
    @Singleton
    @Provides
    fun provideAuthService(): AuthService {
        return Retrofit2Factory.create(AuthService::class.java)
    }
}