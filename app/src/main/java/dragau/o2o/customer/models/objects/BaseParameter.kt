package dragau.o2o.customer.models.objects

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.databinding.library.baseAdapters.BR
import com.google.gson.Gson
import dragau.o2o.customer.models.enums.ParameterType
import dragau.o2o.customer.presentation.presenter.product.ProductRegisterPresenter

class BaseParameter(val id: String?, val type: ParameterType, var name: String, private var _value: Any?, val Uom: String? = null): BaseObservable()
{
    var value: Any?
        @Bindable get() = _value
        set(value) {
            when (type) {
                ParameterType.INT -> _value = if (value is Int) {
                    value
                    } else {
                    value?.toString()?.toIntOrNull()//if (value?.toString()?.toIntOrNull() != null) { value.toString().toIntOrNull() } else _value
                    }
                ParameterType.DECIMAL -> _value = if (value is Double) {
                    value
                } else {
                    value?.toString()?.toDoubleOrNull()//if (value?.toString()?.toDoubleOrNull() != null) { value.toString().toDoubleOrNull() } else _value
                }
                else -> _value = value
            }
            notifyPropertyChanged(BR.value)
        }

    var selectedId: String? = null
        @Bindable get

    var title: String? = null
        @Bindable get

    var isRoot: Boolean = false

    var presenter: ProductRegisterPresenter? = null

    fun hideKeyboard(){
        println("Click ok")
    }

    val hideKeyboard: Function<Unit> = this::hideKeyboard
}
