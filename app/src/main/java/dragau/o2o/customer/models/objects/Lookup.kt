package dragau.o2o.customer.models.objects

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "lookup")
class Lookup(
    @PrimaryKey
    @ColumnInfo(name = "lookupId")
    val lookupId: String,
    @ColumnInfo
    val value: String)
{
    @ColumnInfo
    var parentLookupId: String? = null
}
