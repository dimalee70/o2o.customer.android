package dragau.o2o.customer.models.db

import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE
import io.reactivex.Flowable
import io.reactivex.Maybe
import dragau.o2o.customer.models.objects.User

@Dao
interface UserDao {

    @Query("SELECT * from user")
    fun getAllActive(): Flowable<List<User>>

    @Insert(onConflict = REPLACE)
    fun insert(model: User): Long

    @Insert(onConflict = REPLACE)
    fun insertAll(models: List<User>): LongArray

    @Query("DELETE from user")
    fun deleteAll()

    @Query("DELETE from user where u_id = :id")
    fun deleteById(id: Int)

    @Query("SELECT * from user where u_id = :id")
    fun getFlowable(id: Int): Flowable<User>

    @Query("SELECT * from user where u_id = :id")
    fun get(id: String): Maybe<User>

    @Update
    fun update(model: User)

    @Delete
    fun delete(model: User)
}