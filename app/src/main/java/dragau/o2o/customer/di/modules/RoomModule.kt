package dragau.o2o.customer.di.modules

import dagger.Module
import androidx.room.Room
import dagger.Provides
import dragau.o2o.customer.App
import dragau.o2o.customer.models.db.Db
import dragau.o2o.customer.models.db.UserDao
import javax.inject.Singleton


@Module(includes = [ApplicationModule::class])
class RoomModule(private val mApplication: App){
    private val demoDatabase: Db = Room.databaseBuilder(mApplication, Db::class.java, "o2o_customers")
        .fallbackToDestructiveMigration()
        .allowMainThreadQueries()
        .build()

    @Singleton
    @Provides
    fun providesRoomDatabase(): Db {
        return demoDatabase
    }

    @Singleton
    @Provides
    fun providesProductDao(demoDatabase: Db): UserDao {
        return demoDatabase.getUserDao()
    }
}