package org.soma.everyonepick.groupalbum.data

import android.content.Context
import androidx.room.*
import org.soma.everyonepick.common.util.DATABASE_NAME
import org.soma.everyonepick.groupalbum.data.dao.GroupAlbumLocalDao
import org.soma.everyonepick.groupalbum.data.entity.GroupAlbumLocal

@Database(entities = [GroupAlbumLocal::class], version = 1, exportSchema = false)
@TypeConverters(Converter::class)
abstract class AppDatabase: RoomDatabase() {
    abstract fun groupAlbumLocalDao(): GroupAlbumLocalDao

    companion object {
        // RoomDatabase는 리소스가 매우 크기 때문에 Singleton 패턴을 따라야 합니다.
        // 인스턴스가 1개만 있음을 보장하기 위해 @Volatile과 동기화를 사용합니다.
        @Volatile private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context): AppDatabase {
            return Room.databaseBuilder(context, AppDatabase::class.java, DATABASE_NAME)
                .build()
        }
    }
}