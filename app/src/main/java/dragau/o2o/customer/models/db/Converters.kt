package dragau.o2o.customer.models.db

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class Converters {
    companion object {
        val gson = Gson()

        @TypeConverter
        fun fromString(value: String): ArrayList<String> {
            val listType = object : TypeToken<ArrayList<String>>() {

            }.type

            return gson.fromJson<ArrayList<String>>(value, listType)
        }

        @SuppressLint("NewApi")
        private val formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME

        @SuppressLint("NewApi")
        @TypeConverter
        @JvmStatic
        fun toOffsetDateTime(value: String?): OffsetDateTime? {
            return value?.let {
                return formatter.parse(value, OffsetDateTime::from)
            }
        }

        @RequiresApi(Build.VERSION_CODES.O)
        @TypeConverter
        @JvmStatic
        fun fromOffsetDateTime(date: OffsetDateTime?): String? {
            return date?.format(formatter)
        }

        @TypeConverter
        fun fromArrayList(list: ArrayList<String>): String {

            return gson.toJson(list)
        }

        @TypeConverter
        @JvmStatic
        fun toDate(value: Long?): Date? {
            return if (value == null) null else Date(value)
        }

        @TypeConverter
        @JvmStatic
        fun toLong(value: Date?): Long? {
            return (value?.time)
        }
    }
}