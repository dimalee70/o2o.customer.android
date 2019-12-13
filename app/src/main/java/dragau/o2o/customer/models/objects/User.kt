package dragau.o2o.customer.models.objects

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.room.*
import java.time.OffsetDateTime
import java.util.*

@Entity(tableName = "user", indices = [Index(value = ["phone"], unique = true) ])
data class User(
    @PrimaryKey
    @ColumnInfo(name = "u_id")
    var id: String,
    @ColumnInfo
    var username: String?,
    @ColumnInfo
    var token: String?,
    @ColumnInfo
    val expireDate: Date? = null
) : BaseObservable()
{

    @ColumnInfo
    var birthday: String? = null
        @Bindable get
        set(value) {
            field = value
            //notifyPropertyChanged(BR.birthday)
        }
    @ColumnInfo
    var phone: String? = null
        @Bindable get
        set(value) {
            field = value
            //notifyPropertyChanged(BR.phone)
        }

    override fun toString(): String {
        return "User(username=$username, token=$token, expireDate=$expireDate, id=$id)"
    }
}