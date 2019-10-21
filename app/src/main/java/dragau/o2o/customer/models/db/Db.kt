package dragau.o2o.customer.models.db

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import dragau.o2o.customer.models.objects.User


@Database(entities = [User::class],
    version = 2, exportSchema = false)
@TypeConverters(Converters::class)
abstract class Db : RoomDatabase() {
    /*companion object {
        var instance: Db = synchronized(Db::class) {
            Room.databaseBuilder(App.appComponent.context(), Db::class.java, "o2o.db")
                .fallbackToDestructiveMigration()
                .build()
        }
    }*/

    abstract fun getUserDao(): UserDao
}