package dragau.o2o.customer.models.db

import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE
import io.reactivex.Flowable
import io.reactivex.Maybe
import dragau.o2o.customer.models.objects.Parameter

@Dao
interface ParameterDao {

    @Query("SELECT * from parameter")
    fun getAllActive(): Flowable<List<Parameter>>

    @Insert(onConflict = REPLACE)
    fun insert(model: Parameter): Long

    @Insert(onConflict = REPLACE)
    fun insertAll(models: List<Parameter>): LongArray

    @Query("DELETE from parameter")
    fun deleteAll()

    @Query("DELETE from parameter where id = :id")
    fun deleteById(id: Int)

    @Query("SELECT * from parameter where id = :id")
    fun getFlowable(id: Int): Flowable<Parameter>

    @Query("SELECT * from parameter where id = :id")
    fun get(id: String): Parameter

    @Update
    fun update(model: Parameter)

    @Delete
    fun delete(model: Parameter)

    @Query("SELECT * from parameter")
    fun getAll(): List<Parameter>
}