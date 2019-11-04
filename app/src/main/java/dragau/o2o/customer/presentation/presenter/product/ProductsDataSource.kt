package dragau.o2o.customer.presentation.presenter.product

import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import dragau.o2o.customer.api.ApiManager
import dragau.o2o.customer.models.enums.PagingState
import dragau.o2o.customer.models.objects.Product
import dragau.o2o.customer.models.shared.DataHolder
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Action
import io.reactivex.schedulers.Schedulers

class ProductsDataSource(private val client: ApiManager,
                         private  val compositeDisposable: CompositeDisposable): PageKeyedDataSource<Int, Product>() {

    var state: MutableLiveData<PagingState> = MutableLiveData()
    private var retryCompletable: Completable? = null
//    private var indexPage = 0

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, Product>
    ) {
        compositeDisposable.add(
            client.getProductsByContact(DataHolder.user?.id!!, 0)
                .subscribe( { response ->
                    run {
                        updateState(PagingState.DONE)
                        response.resultObject?.let { callback.onResult(it, null, 1) }

                    }
                },
                {
                    error ->
                    run{
                        updateState(PagingState.ERROR)
                        setRetry(Action { loadInitial(params, callback) })
                    }
                }
                )
        )
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Product>) {
        updateState(PagingState.LOADING)
        compositeDisposable.add(
            client.getProductsByContact(DataHolder.user?.id!!, params.key)
                .subscribe( { response ->
                    run {
                        updateState(PagingState.DONE)
                        response.resultObject?.let {
                            callback.onResult(it, params.key + 1) }
                    }
                },
                    {
                        error ->
                        run {
                            updateState(PagingState.ERROR)
                            setRetry(Action { loadAfter(params, callback) })
                        }
                    }
                )
        )
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Product>) {
    }

    fun retry(){
        if (retryCompletable != null) {
            compositeDisposable.add(retryCompletable!!
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe())
        }
    }

    private fun setRetry(action: Action?){
        retryCompletable = if (action == null) null else Completable.fromAction(action)
    }

    private fun updateState(state: PagingState){
        this.state.postValue(state)
    }




}