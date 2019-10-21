package dragau.o2o.customer.di.modules

import dagger.Module
import dagger.Provides
import dragau.o2o.customer.api.requests.ProductRegisterViewModel
import javax.inject.Singleton

@Module
class ProductAddModule {

    private var productRegisterViewModel: ProductRegisterViewModel = ProductRegisterViewModel()

    @Provides
    @Singleton
    fun provideProductRegisterViewModel(): ProductRegisterViewModel{
        return productRegisterViewModel
    }
}