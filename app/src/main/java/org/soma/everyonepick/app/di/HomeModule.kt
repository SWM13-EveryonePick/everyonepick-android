package org.soma.everyonepick.app.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.soma.everyonepick.app.ui.HomeActivity
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class HomeModule {
    @Singleton
    @Provides
    fun provideHomeActivityClass(): Class<*> = HomeActivity::class.java
}