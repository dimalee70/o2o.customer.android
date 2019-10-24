package dragau.o2o.customer.models.db

import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE
import dragau.o2o.customer.models.objects.Lookup
import io.reactivex.Flowable
import io.reactivex.Maybe
import androidx.lifecycle.LiveData
import io.reactivex.Single


@Dao
interface LookupDao {

    @Query("SELECT * from lookup")
    fun getAll(): Maybe<List<Lookup>>

    @Insert(onConflict = REPLACE)
    fun insert(model: Lookup): Long

    @Insert(onConflict = REPLACE)
    fun insertAll(models: ArrayList<Lookup>): LongArray

    @Query("DELETE from lookup")
    fun deleteAll()

    @Query("DELETE from lookup where lookupId = :id")
    fun deleteById(id: String)

    @Query("SELECT * from lookup where lookupId = :id")
    fun get(id: String): Maybe<Lookup>

    @Query("SELECT * from lookup where parentLookupId = :parentId")
    fun getChildren(parentId: String): Flowable<List<Lookup>>

    @Update
    fun update(model: Lookup)

    @Delete
    fun delete(model: Lookup)

    @Query("SELECT COUNT(lookupId) FROM lookup where parentLookupId = :parentId")
    fun getChildrenCount(parentId: String): Single<Int>
}