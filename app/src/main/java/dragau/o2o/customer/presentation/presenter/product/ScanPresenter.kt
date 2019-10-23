package dragau.o2o.customer.presentation.presenter.product

import android.annotation.SuppressLint
import android.content.Intent
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.google.zxing.BarcodeFormat
import com.journeyapps.barcodescanner.BarcodeResult
import dragau.o2o.customer.App
import dragau.o2o.customer.Screens
import dragau.o2o.customer.api.ApiManager
import dragau.o2o.customer.api.requests.ProductRegisterViewModel
import dragau.o2o.customer.models.objects.ProductBarcode
import dragau.o2o.customer.presentation.view.ScanView
import dragau.o2o.customer.ui.activity.product.ProductActivity
import dragau.o2o.customer.ui.activity.product.ScanActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import ru.terrakok.cicerone.Router
import javax.inject.Inject

@InjectViewState
class ScanPresenter(private val router: Router
                    , private  var productRegisterViewModel: ProductRegisterViewModel
) : MvpPresenter<ScanView>() {
    @Inject
    lateinit var client: ApiManager

    init {
        App.appComponent.inject(this)
    }

    private var disposable: Disposable? = null

//    fun goBack()
//    {
////        println("Click IMage view")
////        router.navigateTo(Screens.ProductRegisterScreen())
//        router.navigateTo(Screens.LoginScreen())
//    }
//
//    fun navigateToRegisterScreen(){
//        router.navigateTo(Screens.StoreScreen())
//    }
//
@SuppressLint("CheckResult")
fun  checkProduct(barcode: String, format: BarcodeFormat){
        client.getProductByBarcode(barcode)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    result ->
                    run {
                        if(result.resultObject != null) {
                            productRegisterViewModel.productId = result.resultObject.productId
                            productRegisterViewModel.title = result.resultObject.name
                            productRegisterViewModel.produserName = result.resultObject.manufacturer
                            productRegisterViewModel.describe = result.resultObject.description
                            productRegisterViewModel.categoryName = result.resultObject.productCategoryId
                            productRegisterViewModel.barCode = result.resultObject.barCode
//                            if (result.resultObject.productImageBase64.isNullOrEmpty())
                                productRegisterViewModel.isVisiblePhoto = false
                            productRegisterViewModel.imageUri = result.resultObject.productThumbnails.peek().body
                            productRegisterViewModel.isVisiblePhoto = true
                            productRegisterViewModel.isEnable = false
                            viewState.showProductExistsDialog()
                            return@subscribe
                        }
                        productRegisterViewModel.productBarcode = ProductBarcode(barcode, format)
                        productRegisterViewModel.isVisiblePhoto = false
                        productRegisterViewModel.isEnable = true
                        productRegisterViewModel.barCode = barcode
                        router.navigateTo(Screens.ProductScreen())

                    }
                },
                {
                    error ->
                    run {
                        productRegisterViewModel.isVisiblePhoto = false
                        productRegisterViewModel.isEnable = true
                        viewState.showError(error)
                    }
                }
            )
    }

//    @SuppressLint("CheckResult")
//    private  fun getPhoto(){
//
//        client.getPhoto(productRegisterViewModel.productId!!)
//            .subscribeOn(Schedulers.io())
//            .observeOn(AndroidSchedulers.mainThread())
//            .subscribe(
//                {
//                        result ->
//                    run {
//
//                        println(result)
//                        productRegisterViewModel.isVisiblePhoto = true
//                        productRegisterViewModel.isEnable = false
//                        viewState.showProductExistsDialog()
//
////                       viewState!!.openProduct()
////                        router.navigateTo(Screens.ProductScreen())
//
//                    }
//                },
//                {
//                        error ->
//                    run {
//                        productRegisterViewModel.isVisiblePhoto = false
//                        productRegisterViewModel.isEnable = true
//                        viewState.showError(error)
//                    }
//                }
//            )
//    }
}
