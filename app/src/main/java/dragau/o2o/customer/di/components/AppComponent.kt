package dragau.o2o.customer.di.components

import android.content.Context
import com.navin.flintstones.rxwebsocket.RxWebsocket
import dagger.Component
import dragau.o2o.customer.App
import dragau.o2o.customer.api.ApiManager
import dragau.o2o.customer.di.ApplicationContext
import dragau.o2o.customer.di.CustomApplicationScope
import dragau.o2o.customer.di.modules.*
import dragau.o2o.customer.models.db.Db
import dragau.o2o.customer.models.db.LookupDao
import dragau.o2o.customer.models.db.UserDao
import dragau.o2o.customer.presentation.presenter.MainAppPresenter
import dragau.o2o.customer.presentation.presenter.home.HomeMainPresenter
import dragau.o2o.customer.presentation.presenter.home.HomePresenter
import dragau.o2o.customer.presentation.presenter.login.PhoneNumberPresenter
import dragau.o2o.customer.presentation.presenter.login.SmsCodePresenter
import dragau.o2o.customer.presentation.presenter.product.*
import dragau.o2o.customer.ui.activity.BaseActivity
import dragau.o2o.customer.ui.activity.MainAppActivity
import dragau.o2o.customer.ui.activity.home.HomeActivity
import dragau.o2o.customer.ui.activity.product.ProductActivity
import dragau.o2o.customer.ui.activity.product.ScanActivity
import dragau.o2o.customer.ui.fragment.home.HomeMainFragment
import dragau.o2o.customer.ui.fragment.login.PhoneNumberFragment
import dragau.o2o.customer.ui.fragment.login.SmsCodeFragment
import dragau.o2o.customer.ui.fragment.product.AddParameterFragment
import dragau.o2o.customer.ui.fragment.product.LookupFragment
import dragau.o2o.customer.ui.fragment.product.ProductRegisterFragment
import dragau.o2o.customer.ui.fragment.product.ProductShowFragment
import javax.inject.Singleton

@Singleton
@CustomApplicationScope
@Component(modules = [ApplicationModule::class, NavigationModule::class,
    ServiceUtilModule::class, RoomModule::class, WSocketModule::class,
    ProductAddModule::class])
interface AppComponent {

    @ApplicationContext
    fun context(): Context

    @ApplicationContext
    fun instance(): App

    fun getServiceUtil(): ApiManager

    fun getWSocket(): RxWebsocket

    fun glideComponentBuilder(): GlideComponent.Builder

    fun userDao(): UserDao

    fun lookupDao(): LookupDao

    fun getDb(): Db

    fun inject(activity: BaseActivity)
    fun inject(activity: MainAppActivity)
    fun inject(activity: ProductActivity)
    fun inject(activity: ScanActivity)
//    fun inject(activity: StoreActivity)
//    fun inject(activty: ShowImageActivity)
    fun inject(activity: HomeActivity)
//    fun inject(activity: CustomsActivity)
    fun inject(fragment: PhoneNumberFragment)
    fun inject(fragment: SmsCodeFragment)
//    fun inject(fragment: ConfirmCodeFragment)
//    fun inject(fragment: RegistrationFragment)
//    fun inject(fragment: LocationMapFragment)
    fun inject(fragment: ProductRegisterFragment)
//    fun inject(fragment: StoreRegisterFragment)
//    fun inject(fragment: ShowImageFragment)
//    fun inject(fragment: ImageViewPagerFragment)
    fun inject(fragment: HomeMainFragment)
    fun inject(fragment: AddParameterFragment)
    fun inject(fragment: ProductShowFragment)
    fun inject(fragment: LookupFragment)
//    fun inject(fragment: OnlineCustomsFragment)

    fun inject(presenter: MainAppPresenter)

//    fun inject(mainAppPresenter: MainAppPresenter)
    fun inject(presenter: PhoneNumberPresenter)
    fun inject(presenter: SmsCodePresenter)
//    fun inject(presenter: ConfirmCodePresenter)
//    fun inject(presenter: RegistrationPresenter)
//    fun inject(presenter: LocationMapPresenter)
    fun inject(presenter: ProductRegisterPresenter)
    fun inject(presenter: ScanPresenter)
    fun inject(presenter: AddParameterPresenter)

//    fun inject(presenter: RegisterStorePresenter)
//    fun inject(presenter: StorePresenter)
//    fun inject(presenter: ShowImagePresenter)
//    fun inject(presenter: ShowImageFragmentPressenter)
//    fun inject(pressenter: ImageViewPagerPresenter)
    fun inject(pressenter: HomePresenter)
    fun inject(pressenter: HomeMainPresenter)
    fun inject (presenter: ProductShowPresenter)
    fun inject (presenter: LookupPresenter)
//    fun inject(pressenter: CustomsPresenter)
//    fun inject(pressenter: OnlineCustomsPresenter)

}