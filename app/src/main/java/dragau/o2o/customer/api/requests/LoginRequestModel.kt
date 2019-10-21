package dragau.o2o.customer.api.requests

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.databinding.library.baseAdapters.BR
import java.io.Serializable
import java.util.*

class LoginRequestModel: BaseObservable(), Serializable {
    var mobilePhone: String? = null
        @Bindable get
        set(value) {
//            if (value == null)
//            {
                field = value
//                return
//            }
//            val re = Regex("[^+0-9]")
//            val tel = re.replace(value, "")
//            field = tel
            notifyPropertyChanged(BR.phone)
        }

    var smsCode: String = ""
        @Bindable get
        set(value) {
            field = value
            notifyPropertyChanged(BR.smsCode)
        }

    var codeExpiryDate: Date? = null
        @Bindable get
        set(value) {
            field = value
            notifyPropertyChanged(BR.codeExpiryDate)
        }
}