package org.soma.everyonepick.groupalbum.data

import android.content.Context
import androidx.room.*
import org.soma.everyonepick.common.util.DATABASE_NAME
import org.soma.everyonepick.groupalbum.data.source.local.GroupAlbumLocalDao
import org.soma.everyonepick.groupalbum.data.entity.GroupAlbumLocal

@Database(entities = [GroupAlbumLocal::class], version = 1, exportSchema = false)
@TypeConverters(Converter::class)
abstract class AppDatabase: RoomDatabase() {
    abstract fun groupAlbumLocalDao(): GroupAlbumLocalDao
}