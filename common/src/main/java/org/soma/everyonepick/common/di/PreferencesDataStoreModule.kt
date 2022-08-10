package org.soma.everyonepick.common.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import org.soma.everyonepick.common.domain.usecase.DataStoreUseCase
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class PreferencesDataStoreModule {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
        name = DataStoreUseCase.DATA_STORE_NAME
    )

    @Singleton
    @Provides
    fun providePreferencesDataStore(@ApplicationContext context: Context) =
        DataStoreUseCase(context.dataStore)
}