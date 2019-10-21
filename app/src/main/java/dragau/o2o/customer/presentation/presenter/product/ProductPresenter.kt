package dragau.o2o.customer.presentation.presenter.product

import android.net.Uri
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import dragau.o2o.customer.api.requests.ProductRegisterViewModel
import dragau.o2o.customer.presentation.view.product.ProductView
import io.reactivex.disposables.Disposable

import ru.terrakok.cicerone.Router
import javax.inject.Inject

@InjectViewState
class ProductPresenter(private  var router: Router, private var productRegisterViewModel: ProductRegisterViewModel) : MvpPresenter<ProductView>() {

    fun changeImage(uri: String){
        productRegisterViewModel.imageUri = uri
        productRegisterViewModel.isVisiblePhoto = true
    }
}
