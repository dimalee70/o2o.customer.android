package dragau.o2o.customer.presentation.presenter.product

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import dragau.o2o.customer.api.ApiManager
import dragau.o2o.customer.models.objects.Product
import io.reactivex.disposables.CompositeDisposable

class ProductsDataSourceFactory(private var compositeDisposable: CompositeDisposable,
                                private  var client: ApiManager)
    : DataSource.Factory<Int, Product>() {

    val productsDataSourceLiveData = MutableLiveData<ProductsDataSource>()

    override fun create(): DataSource<Int, Product> {
        val newDataSourse = ProductsDataSource(client, compositeDisposable)
        productsDataSourceLiveData.postValue(newDataSourse)
        return newDataSourse
    }
}