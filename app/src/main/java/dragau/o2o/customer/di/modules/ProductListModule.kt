package dragau.o2o.customer.di.modules

import androidx.databinding.ObservableArrayList
import dagger.Module
import dagger.Provides
import dragau.o2o.customer.api.response.ProductResponce
import dragau.o2o.customer.models.objects.Product
import javax.inject.Singleton

@Module
class ProductListModule {

    private var products: ObservableArrayList<Product> = ObservableArrayList()

    @Provides
    @Singleton
    fun provideProductList(): ObservableArrayList<Product>{
        return products
    }
}