package dragau.o2o.customer.presentation.presenter.product

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.view.View
import androidx.lifecycle.MutableLiveData
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import dragau.o2o.customer.App
import dragau.o2o.customer.Constants
import dragau.o2o.customer.api.ApiManager
import dragau.o2o.customer.api.response.ProductResponce
import dragau.o2o.customer.models.objects.Product
import dragau.o2o.customer.presentation.presenter.BasePresenter
import dragau.o2o.customer.presentation.view.product.ProductShowView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import ru.terrakok.cicerone.Router
import javax.inject.Inject

@InjectViewState
class ProductShowPresenter(private  var router: Router): BasePresenter<ProductShowView>() {

    var liveProductResponse = MutableLiveData<ProductResponce>()

    @Inject
    lateinit var client: ApiManager

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    init {
        App.appComponent.inject(this)
    }

    fun onPhotoClicked(v: View){
        v.transitionName = Constants.PHOTO_TRANSITION
        router.exit()
    }

    @SuppressLint("CheckResult")
    fun getProductById(productId: String?){
        disposables.add(
        client.getProduct(productId!!)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    result ->
                    run {
                        liveProductResponse.value = result
                    }
                },
                {
                    error ->
                    error.printStackTrace()
                })
        )
    }

    fun observeProductResponseBoundary(): MutableLiveData<ProductResponce>{
        return liveProductResponse
    }

//    client.getProductsByContact(contactId)
//    .subscribeOn(Schedulers.io())
//    .observeOn(AndroidSchedulers.mainThread())
//    .subscribe(
//    {
//        result ->
//        run {
//            liveProducttResponse.value = result
//        }
//    },
//    {
//        error ->
//        run {
//            run {
//                viewState.showError(error)
//            }
//        }
//    }
//    )

}