package dragau.o2o.customer.presentation.presenter.home

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
import dragau.o2o.customer.presentation.presenter.BasePresenter
import dragau.o2o.customer.presentation.view.home.HomeMainView
import ru.terrakok.cicerone.Router
import ru.terrakok.cicerone.Screen
import timber.log.Timber
import javax.inject.Inject

@InjectViewState
class HomeMainPresenter(private var router: Router) : BasePresenter<HomeMainView>() {

//    var liveOrderByOutletResponse = MutableLiveData<OrdersByOutletResponce>()

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
        viewState!!.openScanActivity()
//        router.navigateTo(Screens.ScanScreen())
    }
}
