package dragau.o2o.customer.models.objects

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import dragau.o2o.customer.models.enums.ParameterType

@Entity(tableName = "parameter")
data class Parameter
(
    @PrimaryKey
    @ColumnInfo
    val id: String,
    @ColumnInfo
    val type: ParameterType,
    @ColumnInfo
    val name: String,
    @ColumnInfo
    val value: Any?,
    @ColumnInfo
    val uom: String?
)