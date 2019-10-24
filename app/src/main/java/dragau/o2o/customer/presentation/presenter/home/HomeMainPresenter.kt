package dragau.o2o.customer.presentation.presenter.home

import android.annotation.SuppressLint
import android.content.SharedPreferences
import androidx.databinding.ObservableBoolean
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
import dragau.o2o.customer.models.db.LookupDao
import dragau.o2o.customer.models.objects.Product
import dragau.o2o.customer.models.shared.DataHolder
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

    @Inject
    lateinit var lookupDao: LookupDao

    var isLoading = ObservableBoolean()

    init {
        App.appComponent.inject(this)
//        getProductByContactId(DataHolder.user!!.id)

    }

    fun openProductShow(productId: String?, productName: String?){
        router.navigateTo(Screens.ProductShowScreen(productId, productName))
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
//        getProductByContactId(DataHolder.user!!.id)
        router.navigateTo(Screens.ScanScreen())
//        router.navigateTo(Screens.ProductScreen())
    }

    fun observeProductResponseBoundary(): MutableLiveData<ProductResponceContact>{
        return liveProducttResponse
    }

    /*@SuppressLint("CheckResult")
    fun getProductByContactId(contactId: String){

        disposables.add(
        client.getProductsByContact(contactId)
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
        )
    }


    @SuppressLint("CheckResult")
    fun getCategories(){
    }*/

    fun reloadData() = reloadData(showRefresh = true)

    fun reloadData(showRefresh: Boolean) {
        isLoading.set(showRefresh)

        disposables.add(
            client.getProductsByContact(DataHolder.user!!.id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                            result ->
                        run {
                            liveProducttResponse.value = result
                        }

                        disposables.add(client.getCategories()
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(
                                {
                                        result ->
                                    if (result.resultObject != null) {
                                        lookupDao.insertAll(result.resultObject)
                                    }
                                },
                                {
                                        error ->
                                    run {
                                        viewState?.showError(error)
                                    }
                                },
                                {
                                    isLoading.set(false)
                                }
                            ))
                    },
                    {
                        error ->
                        run {
                            isLoading.set(false)
                           viewState?.showError(error)
                        }
                    }
                )
        )

    }
}
