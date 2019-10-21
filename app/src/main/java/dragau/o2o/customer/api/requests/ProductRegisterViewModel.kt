package dragau.o2o.customer.api.requests

import android.net.Uri
import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import dragau.o2o.customer.extensions.encodeImage

class ProductRegisterViewModel: BaseObservable() {

    var isEnable: Boolean = true
        @Bindable get
        set(value){
            field = value
            notifyChange()
        }

    var productId: String? = null

    var productCategoryId: String? = null

    var categoryName: String? = null
        @Bindable get
        set(value){
            field = value
//            notifyChange()
            notifyPropertyChanged(androidx.databinding.library.baseAdapters.BR.categoryName)
        }
    var produserName: String? = null
        @Bindable get
        set(value){
            field = value
            notifyChange()
            notifyPropertyChanged(androidx.databinding.library.baseAdapters.BR.produserName)
        }
    var title: String? = null
        @Bindable get
        set(value){
            field = value
//            notifyChange()
            notifyPropertyChanged(androidx.databinding.library.baseAdapters.BR.title)
            notifyPropertyChanged(androidx.databinding.library.baseAdapters.BR.categoryName)
        }
    var barCode: String? = null
        @Bindable get
        set(value) {
            field = value
            notifyChange()
//            notifyPropertyChanged(androidx.databinding.library.baseAdapters.BR.barCode)
//            notifyPropertyChanged(androidx.databinding.library.baseAdapters.BR.categoryName)
        }
    var describe: String? = null
        @Bindable get
        set(value){
            field = value
//            notifyChange()
            notifyPropertyChanged(androidx.databinding.library.baseAdapters.BR.describe)
        }
    var imageUri: String? = null
        @Bindable get
        set(value) {
            field = value
            notifyChange()
        }

    var imageBase64: String? = null

    fun setImageBase64ByClient(stringUri: String){
        val uri = Uri.parse(stringUri)
        imageBase64 = uri.encodeImage()
    }

    fun setImageUriFromServer(string64: String){
        imageBase64 = string64
    }
    var isVisiblePhoto: Boolean = false
        set(value){
            field = value
            notifyChange()
        }

    fun clearObject(){
        categoryName = null
        produserName = null
        title = null
        barCode = null
        describe = null
        imageUri = null
        isVisiblePhoto = false
    }
}