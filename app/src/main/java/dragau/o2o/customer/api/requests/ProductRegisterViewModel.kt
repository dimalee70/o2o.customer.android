package dragau.o2o.customer.api.requests

import android.net.Uri
import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.databinding.ObservableArrayList
import androidx.databinding.library.baseAdapters.BR
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import dragau.o2o.customer.extensions.encodeImage
import dragau.o2o.customer.models.enums.ParameterType
import dragau.o2o.customer.models.objects.BaseParameter
import dragau.o2o.customer.models.objects.ProductBarcode
import dragau.o2o.customer.presentation.presenter.product.ProductRegisterPresenter
import org.joda.time.DateTime

class ProductRegisterViewModel: BaseObservable() {
    @Expose(serialize = false)
    var isEnable: Boolean = true
        @Bindable get
        set(value){
            field = value
            notifyChange()
        }

    @SerializedName("productId")
    var productId: String? = null

    @SerializedName("productCategoryId")
    var productCategoryId: String? = null

    var parameters: ObservableArrayList<BaseParameter>? = null

    @Expose(serialize = false)
    var categoryName: String? = null
        @Bindable get
        set(value){
            field = value
//            notifyChange()
            notifyPropertyChanged(BR.categoryName)
        }

    @SerializedName("manufacturer")
    var produserName: String? = null
        @Bindable get
        set(value){
            field = value
            notifyChange()
            notifyPropertyChanged(BR.produserName)
        }

    @SerializedName("name")
    var title: String? = null
        @Bindable get
        set(value){
            field = value
//            notifyChange()
            notifyPropertyChanged(BR.title)
            notifyPropertyChanged(BR.categoryName)
        }

    @SerializedName("barCode")
    var barCode: String? = null
        @Bindable get
        set(value) {
            field = value
            notifyChange()
//            notifyPropertyChanged(androidx.databinding.library.baseAdapters.BR.barCode)
//            notifyPropertyChanged(androidx.databinding.library.baseAdapters.BR.categoryName)
        }

    var productBarcode: ProductBarcode? = null
        @Bindable get
        set(value) {
            field = value
            notifyChange()
        }

    @SerializedName("description")
    var describe: String? = null
        @Bindable get
        set(value){
            field = value
//            notifyChange()
            notifyPropertyChanged(BR.describe)
        }


    @Expose(serialize = false)
    var imageUri: String? = null
        @Bindable get
        set(value) {
            field = value
            notifyChange()
        }

    @Expose(serialize = false)
    var imageBase64: String? = null

    fun setImageBase64ByClient(stringUri: String){
        val uri = Uri.parse(stringUri)
        imageBase64 = uri.encodeImage()
    }

    fun setImageUriFromServer(string64: String){
        imageBase64 = string64
    }

    @Expose(serialize = false)
    var isVisiblePhoto: Boolean = false
        set(value){
            field = value
            notifyChange()
        }

    fun clearObject(){
        productId = null
        categoryName = null
        produserName = null
        title = null
        barCode = null
        describe = null
        imageUri = null
        isVisiblePhoto = false
        parameters = null
    }
}