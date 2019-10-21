package dragau.o2o.customer.presentation.presenter.product

import android.graphics.Bitmap
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import dragau.o2o.customer.presentation.view.product.AddProductView
import io.reactivex.BackpressureStrategy
import io.reactivex.disposables.Disposable

@InjectViewState
class AddProductPresenter : MvpPresenter<AddProductView>() {
    private var disposable: Disposable? = null
    private var imageUrl: String = ""

    fun pickPhoto(){
        viewState?.pickPhoto()
    }

    fun uploadImage(image: Bitmap){
        viewState?.showLoadedImage(image)
    }
}
