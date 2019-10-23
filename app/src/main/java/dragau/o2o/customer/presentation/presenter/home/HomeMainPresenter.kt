package dragau.o2o.customer.presentation.presenter.home

import android.annotation.SuppressLint
import android.content.SharedPreferences
import androidx.lifecycle.MutableLiveData
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import dragau.o2o.customer.App
import dragau.o2o.customer.Screens
import dragau.o2o.customer.api.ApiManager
import dragau.o2o.customer.api.response.ProductResponce
import dragau.o2o.customer.api.response.ProductResponceContact
import dragau.o2o.customer.models.objects.Product
import dragau.o2o.customer.presentation.presenter.BasePresenter
import dragau.o2o.customer.presentation.view.home.HomeMainView
import ru.terrakok.cicerone.Router
import ru.terrakok.cicerone.Screen
import timber.log.Timber
import javax.inject.Inject

@InjectViewState
class HomeMainPresenter(private var router: Router) : BasePresenter<HomeMainView>() {

    var liveProducttResponse = MutableLiveData<ProductResponceContact>()

    @Inject
    lateinit var client: ApiManager

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    private var disposable: Disposable? = null

    init {
        App.appComponent.inject(this)

    }
//    fun getOrdersByOtlet(salesOuterId: String){
//        disposables.add(
//        client.getOrdersByOutlet(salesOuterId)
//            .subscribeOn(Schedulers.io())
//            .observeOn(AndroidSchedulers.mainThread())
//            .subscribe(
//                {
//                    result ->
//                        liveOrderByOutletResponse.value = result
//                },
//                {
//                    error ->
//                    run{
//                        viewState!!.showError(error)
//                        Timber.i(error.localizedMessage)
//                    }
//                }
//            )
//        )
//    }
//    fun openCustoms(){
//        viewState!!.openCustomsScreen()
//    }

//    fun observeForOrderByOutletResponseBoundary(): MutableLiveData<OrdersByOutletResponce>{
//        return liveOrderByOutletResponse
//    }

    fun openScan(){
//        viewState!!.openScanActivity()
        getProductByContactId()
//        router.navigateTo(Screens.ScanScreen())
//        router.navigateTo(Screens.ProductScreen())
    }

    fun observeProductResponseBoundary(): MutableLiveData<ProductResponceContact>{
        return liveProducttResponse
    }

    @SuppressLint("CheckResult")
    fun getProductByContactId(){
        client.getProductsByContact("190204ea-fce8-473e-15c2-08d725f7d17c")
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    result ->
                    run {
                        liveProducttResponse.value = result
                    }
                },
                {
                    error ->
                    run {
                        run {
                            viewState.showError(error)
                        }
                    }
                }
            )
    }
}
