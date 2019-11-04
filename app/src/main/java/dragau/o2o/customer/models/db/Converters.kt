package dragau.o2o.customer.models.db

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dragau.o2o.customer.models.enums.ParameterType
import org.joda.time.DateTime
import java.text.SimpleDateFormat
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

        @SuppressLint("NewApi", "SimpleDateFormat")
        private val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.ZZZZZZZ'Z'")
//            .ISO_OFFSET_DATE_TIME

        @TypeConverter
        @JvmStatic
        fun toOffsetDateTime(value: String?): Date? {
            return value?.let {
                return DateTime.parse(value.substring(0,value.indexOf("."))).toDate()
            }
        }

        @RequiresApi(Build.VERSION_CODES.O)
        @TypeConverter
        @JvmStatic
        fun fromOffsetDateTime(date: Date?): String? {
            return dateFormat.format(date)
        }
//        @SuppressLint("NewApi")
//        @TypeConverter
//        @JvmStatic
//        fun toOffsetDateTime(value: String?): OffsetDateTime? {
//            return value?.let {
//                target = scheduledDate.toInstant().atOffset( ZoneOffset.UTC );
//                return formatter.parse(value)
////                return formatter.parse(value, OffsetDateTime::from)
//            }
//        }

//        @RequiresApi(Build.VERSION_CODES.O)
//        @TypeConverter
//        @JvmStatic
//        fun fromOffsetDateTime(date: OffsetDateTime?): String? {
//            return date?.format(formatter)
//        }

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


        @TypeConverter
        @JvmStatic
        fun fromParameterType(obj: ParameterType?): String? = obj?.name

        @TypeConverter
        @JvmStatic
        fun toParameterType(s: String?): ParameterType? = if (s == null) null else ParameterType.valueOf(s)


        @JvmStatic
        @TypeConverter
        fun fromAny(value: Any?): String? {
            val type = object : TypeToken<Any?>() {}.type
            return gson.toJson(value, type)
        }

        @JvmStatic
        @TypeConverter
        fun toAny(valuesString: String?): Any? {
            if (valuesString == null) {
                return null
            }

            val type = object : TypeToken<Any?>() {}.type
            return gson.fromJson(valuesString, type)
        }

    }
}