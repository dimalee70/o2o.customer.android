package dragau.o2o.customer.ui.adapters

import android.content.res.Resources
import android.graphics.Color
import android.net.Uri
import android.util.Base64
import android.view.View
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.request.RequestOptions
import com.tiper.MaterialSpinner
import dragau.o2o.customer.models.objects.ProductCategories
import java.text.NumberFormat
import java.util.*
import java.util.regex.Pattern
import javax.inject.Inject
import com.bumptech.glide.load.engine.DiskCacheStrategy
import dragau.o2o.customer.R


//@BindingAdapter("setImage")
//fun CircleImageView.setImage(url: String?){
//    if(url != null) {
//        Glide.with(context)
//            .load(url)
//            .into(this)
//    }
//}

@BindingAdapter("money")
fun TextView.setMoney(money: Int){
    val format = NumberFormat.getCurrencyInstance()
    format.maximumFractionDigits = 0
    format.currency = Currency.getInstance(Locale("kk", "KZ"))
    this.text = format.format(money)
}

//@BindingAdapter("fillColor")
//fun CircleImageView.fillColor(color: String){
//    setColorFilter(Color.parseColor(color))
//}

@BindingAdapter("setImageUri")
fun ImageView.setImageUri(uri: String?){ //, newImageAttrChanged: InverseBindingListener){
    if(uri.isNullOrEmpty()) {
        Glide.with(context).clear(this)
        this.setImageDrawable(null)
        return
    }

//    if (){

        val options = RequestOptions()
            .centerCrop()
            .placeholder(R.drawable.progress_animation)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .skipMemoryCache(true)
            .priority(Priority.HIGH)
            .dontAnimate()
            .dontTransform()

//        val circularProgressDrawable = CircularProgressDrawable(context)
//        circularProgressDrawable.strokeWidth = 5f
//        circularProgressDrawable.centerRadius = 30f
//        circularProgressDrawable.start()
//
//        val thumbnailRequest = Glide.with(this)
//            .load("https://picsum.photos/50/50?image=0")
        val imageAsBytes = Base64.decode(uri.toByteArray(), Base64.DEFAULT)
        Glide.with(context)
            .load(imageAsBytes)
            .apply(options)
//            .placeholder(circularProgressDrawable)
            .into(this)
//        return
//    }

//    if(uri.endsWith("=")){

//    }
//    println(Base64.isBase64(uri))
//    if(Base64.isBase64(uri)){
//        return
//    }
//    Glide.with(context).load(uri).into(this)

}

@BindingAdapter(value = ["selectedValue", "selectedValueAttrChanged"], requireAll = false)
fun MaterialSpinner.bindSpinnerData(newSelectedValue: String?, newTextAttrChanged: InverseBindingListener){
    this.onItemSelectedListener = object:
        MaterialSpinner.OnItemSelectedListener {
        override fun onItemSelected(parent: MaterialSpinner, view: View?, position: Int, id: Long) {
            if(newSelectedValue != null && newSelectedValue.equals(parent.selectedItem))
                return
            newTextAttrChanged.onChange()
        }

        override fun onNothingSelected(parent: MaterialSpinner) {
        }
    }
}

@InverseBindingAdapter(attribute = "selectedValue", event = "selectedValueAttrChanged")
fun MaterialSpinner.captureSelectedValue(): String{
    return (selectedItem as ProductCategories).productCategoryId.toString()
}



//@BindingAdapter(value = ["selectedValue", "selectedValueAttrChanged"], requireAll = false)
//fun MaterialSpinner.bindSpinnerData(newSelectedValue: String?, newTextAttrChanged: InverseBindingListener){
//    this.onItemSelectedListener = object:
//        MaterialSpinner.OnItemSelectedListener {
//        override fun onItemSelected(parent: MaterialSpinner, view: View?, position: Int, id: Long) {
//            if(newSelectedValue != null && newSelectedValue.equals(parent.selectedItem))
//                return
//                newTextAttrChanged.onChange()
//        }
//
//        override fun onNothingSelected(parent: MaterialSpinner) {
////            newTextAttrChanged.onChange()
//        }
//    }
//}
//
//@InverseBindingAdapter(attribute = "selectedValue", event = "selectedValueAttrChanged")
//fun MaterialSpinner.captureSelectedValue(): String{
////    return (selectedItem as ProductCategories).productCategoryId.toString()
//    return selectedItem.toString()
//}