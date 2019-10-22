package dragau.o2o.customer.models.objects

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.google.gson.Gson
import dragau.o2o.customer.BR
import dragau.o2o.customer.models.enums.ParameterType
import dragau.o2o.customer.presentation.presenter.product.ProductRegisterPresenter

class BaseParameter(val id: String?, val type: ParameterType, val name: String, private var _value: Any?): BaseObservable()
{
    var value: Any?
        @Bindable get() = _value
        set(value) {
            when (type) {
                ParameterType.INT -> _value = if (value is Int) {
                    value
                    } else {
                        if (value?.toString()?.toIntOrNull() != null) { value.toString().toIntOrNull() } else _value
                    }
                ParameterType.DECIMAL -> _value = if (value is Double) {
                    value
                } else {
                    if (value?.toString()?.toDoubleOrNull() != null) { value.toString().toDoubleOrNull() } else _value
                }
                else -> _value = value
            }
            notifyPropertyChanged(BR.value)
        }

    var presenter: ProductRegisterPresenter? = null
}
