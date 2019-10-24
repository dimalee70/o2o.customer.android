package dragau.o2o.customer.di.modules

import dagger.Module
import androidx.room.Room
import dagger.Provides
import dragau.o2o.customer.App
import dragau.o2o.customer.models.db.Db
import dragau.o2o.customer.models.db.LookupDao
import dragau.o2o.customer.models.db.UserDao
import javax.inject.Singleton


@Module(includes = [ApplicationModule::class])
class RoomModule(private val mApplication: App){
    private val roomDatabase: Db = Room.databaseBuilder(mApplication, Db::class.java, "o2o_customers")
        .fallbackToDestructiveMigration()
        .allowMainThreadQueries()
        .build()

    @Singleton
    @Provides
    fun providesRoomDatabase(): Db {
        return roomDatabase
    }

    @Singleton
    @Provides
    fun providesProductDao(roomDatabase: Db): UserDao {
        return roomDatabase.getUserDao()
    }


    @Singleton
    @Provides
    fun providesLookupDao(roomDatabase: Db): LookupDao {
        return roomDatabase.getLookupDao()
    }
}