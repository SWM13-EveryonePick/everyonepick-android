package org.soma.everyonepick.groupalbum.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import org.soma.everyonepick.common.util.DATABASE_NAME
import org.soma.everyonepick.groupalbum.data.AppDatabase
import org.soma.everyonepick.groupalbum.data.source.local.GroupAlbumLocalDao
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class DatabaseModule {

    @Singleton
    @Provides
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(context, AppDatabase::class.java, DATABASE_NAME).build()
    }

    @Provides
    fun provideGroupAlbumLocalDao(appDatabase: AppDatabase): GroupAlbumLocalDao {
        return appDatabase.groupAlbumLocalDao()
    }
}