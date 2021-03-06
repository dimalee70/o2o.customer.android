package dragau.o2o.customer.presentation.presenter.home

import android.annotation.SuppressLint
import android.content.SharedPreferences
import androidx.databinding.ObservableBoolean
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
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
import dragau.o2o.customer.models.db.ParameterDao
import dragau.o2o.customer.models.enums.PagingState
import dragau.o2o.customer.models.objects.Product
import dragau.o2o.customer.models.shared.DataHolder
import dragau.o2o.customer.presentation.presenter.BasePresenter
import dragau.o2o.customer.presentation.presenter.product.ProductsDataSource
import dragau.o2o.customer.presentation.presenter.product.ProductsDataSourceFactory
import dragau.o2o.customer.presentation.view.home.HomeMainView
import io.reactivex.rxkotlin.Observables
import ru.terrakok.cicerone.Router
import ru.terrakok.cicerone.Screen
import timber.log.Timber
import java.lang.Exception
import javax.inject.Inject

@InjectViewState
class HomeMainPresenter(private var router: Router) : BasePresenter<HomeMainView>() {

//    var liveProducttResponse = MutableLiveData<ProductResponceContact>()

    var liveProducttResponse: LiveData<PagedList<Product>>

    @Inject
    lateinit var client: ApiManager

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    @Inject
    lateinit var lookupDao: LookupDao

    @Inject
    lateinit var parameterDao: ParameterDao

    var isLoading = ObservableBoolean()

    private val pageSize = 10

    private val productsDataSourceFactory: ProductsDataSourceFactory

    init {
        App.appComponent.inject(this)
        productsDataSourceFactory = ProductsDataSourceFactory(disposables, client)
        val config = PagedList.Config.Builder()
            .setPageSize(pageSize)
            .setInitialLoadSizeHint(pageSize)
            .setEnablePlaceholders(false)
            .build()
        liveProducttResponse = LivePagedListBuilder<Int, Product>(productsDataSourceFactory, config).build()
        sync()
    }

//    fun initPagging(){
//        val config = PagedList.Config.Builder()
//            .setPageSize(pageSize)
//            .setInitialLoadSizeHint(pageSize)
//            .setEnablePlaceholders(false)
//            .build()
//        liveProducttResponse = LivePagedListBuilder<Int, Product>(productsDataSourceFactory, config).build()
//    }

    fun getState(): LiveData<PagingState> = Transformations.switchMap<ProductsDataSource,
        PagingState>(productsDataSourceFactory.productsDataSourceLiveData, ProductsDataSource::state)

    fun retry(){
        productsDataSourceFactory.productsDataSourceLiveData.value?.retry()
    }

    fun listIsEmpty(): Boolean{
        return liveProducttResponse.value?.isEmpty()?:true
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

    fun observeProductResponseBoundary(): LiveData<PagedList<Product>>{
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
        viewState.clearItems()
        productsDataSourceFactory.productsDataSourceLiveData.value?.invalidate()
        sync()
    }



    fun sync()
    {
        disposables.add(Observables
            .zip( client.getCategories(), client.getParameters())
            { cats, params ->
                lookupDao.deleteAll()
                lookupDao.insertAll(cats.resultObject!!)

                parameterDao.deleteAll()
                parameterDao.insertAll(params.resultObject!!)
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                run {
                    viewState?.hideProgress()
                }
            },
                { error ->
                    run {
                        viewState?.showError(error)
                    }
                    isLoading.set(false)
                },
                {
                    isLoading.set(false)
                }))
     }
}


