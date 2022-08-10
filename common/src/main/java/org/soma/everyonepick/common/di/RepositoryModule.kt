package org.soma.everyonepick.common.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.soma.everyonepick.common.data.RetrofitFactory
import org.soma.everyonepick.common.data.repository.AuthRepository
import org.soma.everyonepick.common.data.repository.UserRepository
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class RepositoryModule {
    @Singleton
    @Provides
    fun provideAuthRepository(): AuthRepository {
        return RetrofitFactory.create(AuthRepository::class.java)
    }

    @Singleton
    @Provides
    fun provideUserRepository(): UserRepository {
        return RetrofitFactory.create(UserRepository::class.java)
    }
}