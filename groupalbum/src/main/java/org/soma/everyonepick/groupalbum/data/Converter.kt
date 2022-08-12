package org.soma.everyonepick.groupalbum.data

import androidx.room.TypeConverter
import com.google.gson.Gson
import org.soma.everyonepick.common.data.entity.User

class Converter {
    @TypeConverter fun userListToString(value: List<User>): String = Gson().toJson(value)
    @TypeConverter fun stringToUserList(value: String): List<User> = Gson().fromJson(value, Array<User>::class.java).toList()
}