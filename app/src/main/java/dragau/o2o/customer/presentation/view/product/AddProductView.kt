package dragau.o2o.customer.presentation.view.product

import android.graphics.Bitmap
import com.arellomobile.mvp.MvpView

interface AddProductView : MvpView {
    fun pickPhoto()
    fun showLoadedImage(image: Bitmap)
}
