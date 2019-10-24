package dragau.o2o.customer

import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment
import dragau.o2o.customer.ui.activity.home.HomeActivity
import dragau.o2o.customer.api.requests.LoginRequestModel
import dragau.o2o.customer.ui.activity.LoginInActivity
import dragau.o2o.customer.ui.activity.MainAppActivity
import dragau.o2o.customer.ui.activity.product.ProductActivity
import dragau.o2o.customer.ui.activity.product.ScanActivity
import dragau.o2o.customer.ui.fragment.home.HomeMainFragment
import dragau.o2o.customer.ui.fragment.login.PhoneNumberFragment
import dragau.o2o.customer.ui.fragment.login.SmsCodeFragment
import dragau.o2o.customer.ui.fragment.product.AddParameterFragment
import dragau.o2o.customer.ui.fragment.product.LookupFragment
import dragau.o2o.customer.ui.fragment.product.ProductRegisterFragment
import dragau.o2o.customer.ui.fragment.product.ProductShowFragment
import ru.terrakok.cicerone.android.support.SupportAppScreen

class Screens {
    class MainScreen : SupportAppScreen() {
        override fun getActivityIntent(context: Context?): Intent {
            return Intent(context, MainAppActivity::class.java)
        }
    }

//    class ImagesScreen(private var position: Int? = null) : SupportAppScreen(){
//        override fun getActivityIntent(context: Context?): Intent {
//            position?.let {
//                val intent = Intent(context, ShowImageActivity::class.java)
//                intent.putExtra(Constants.PHOTO_POSITION, position!!)
//                return intent
//            }
//            return Intent(context, ShowImageActivity::class.java)
//        }
//    }

    class LoginScreen : SupportAppScreen() {
        override fun getActivityIntent(context: Context?): Intent {
            return Intent(context, LoginInActivity::class.java)
        }
    }


    class PhoneNumberScreen : SupportAppScreen() {

        init {
            this.screenKey = javaClass.simpleName
        }

        override fun getFragment(): Fragment {
            return PhoneNumberFragment.newInstance()
        }
    }

    class SmsCodeScreen(userRequstModel: LoginRequestModel?): SupportAppScreen() {
        var userRequstModel : LoginRequestModel? = null
        init {
            this.screenKey = javaClass.simpleName
            this.userRequstModel = userRequstModel
        }

        override fun getFragment(): Fragment {
            return SmsCodeFragment.newInstance(userRequstModel)
        }
    }

    class LookupScreen(var parentId: String): SupportAppScreen() {
        init {
            this.screenKey = javaClass.simpleName
        }

        override fun getFragment(): Fragment {
            return LookupFragment.newInstance(parentId)
        }
    }

    class ProductScreen: SupportAppScreen(){
        override fun getActivityIntent(context: Context?): Intent {
            return Intent(context, ProductActivity::class.java)
        }
    }

    class ScanScreen: SupportAppScreen(){
        override fun getActivityIntent(context: Context?): Intent {
            return Intent(context, ScanActivity::class.java)
        }
    }

//    class StoreScreen: SupportAppScreen(){
//        override fun getActivityIntent(context: Context?): Intent {
//            return Intent(context, StoreActivity::class.java)
//        }
//    }

    class HomeScreen: SupportAppScreen(){
        override fun getActivityIntent(context: Context?): Intent {
            return Intent(context, HomeActivity::class.java)
        }
    }

//    class CustomScreen: SupportAppScreen(){
//        override fun getActivityIntent(context: Context?): Intent {
//            return Intent(context, CustomsActivity::class.java)
//        }
//    }

//    class PhoneNumberScreen : SupportAppScreen() {
//
//        init {
//            this.screenKey = javaClass.simpleName
//        }
//
//        override fun getFragment(): Fragment {
//            return PhoneNumberFragment.newInstance()
//        }
//    }

//    class AddProductScreen : SupportAppScreen() {
//        override fun getActivityIntent(context: Context?): Intent {
//            return Intent(context, AddProductActivity::class.java)
//        }
//    }

//    class SmsCodeScreen(userRequstModel: LoginRequestModel?): SupportAppScreen() {
//        var userRequstModel : LoginRequestModel? = null
//        init {
//            this.screenKey = javaClass.simpleName
//            this.userRequstModel = userRequstModel
//        }
//
//        override fun getFragment(): Fragment {
//            return SmsCodeFragment.newInstance(userRequstModel)
//        }
//    }

//    class ConfirmCodeScreen(userRequstModel: LoginRequestModel?): SupportAppScreen()
//    {
//        var userRequstModel : LoginRequestModel? = null
//        init {
//            this.screenKey = javaClass.simpleName
//            this.userRequstModel = userRequstModel
//        }
//
//        override fun getFragment(): Fragment {
//            return ConfirmCodeFragment.newInstance(userRequstModel)
//        }
//    }

//    class RegistrationScreen(userRequstModel: LoginRequestModel?): SupportAppScreen() {
//        var userRequstModel: LoginRequestModel? = null
//        init {
//            this.screenKey = javaClass.simpleName
//            this.userRequstModel = userRequstModel
//        }
//
//        override fun getFragment(): Fragment {
//            return RegistrationFragment.newInstance(userRequstModel)
//        }
//    }

//    class LocationMapScreen : SupportAppScreen() {
//        init {
//            this.screenKey = javaClass.simpleName
//        }
//
//        override fun getFragment(): Fragment {
//            return LocationMapFragment.newInstance()
//        }
//    }

    class ProductRegisterScreen: SupportAppScreen(){
        init {
            this.screenKey = javaClass.simpleName
        }

        override fun getFragment(): Fragment {
            return ProductRegisterFragment.newInstance()
        }
    }

    class AddParameterScreen: SupportAppScreen(){
        init {
            this.screenKey = javaClass.simpleName
        }

        override fun getFragment(): Fragment {
            return AddParameterFragment.newInstance()
        }
    }

    class ProductShowScreen(private var productId: String?): SupportAppScreen(){
        init {
            this.screenKey = javaClass.simpleName
        }

        override fun getFragment(): Fragment {
            return ProductShowFragment.newInstance(productId)
        }
    }
//    class StoreRegisterScreen: SupportAppScreen(){
//        init {
//            this.screenKey = javaClass.simpleName
//        }
//
//        override fun getFragment(): Fragment {
//            return StoreRegisterFragment.newInstance()
//        }
//    }
//
//    class ShowImagesScreen : SupportAppScreen(){
//        init {
//            this.screenKey =  javaClass.simpleName
//        }
//
//        override fun getFragment(): Fragment {
//            return ShowImageFragment.newInstance()
//        }
//    }

//    class ImageViewPagerScreen(private var position: Int): SupportAppScreen(){
//        init {
//            this.screenKey = javaClass.simpleName
//        }
//
//        override fun getFragment(): Fragment {
//            return ImageViewPagerFragment.newInstance(position)
//        }
//    }

    class HomeMainScreen: SupportAppScreen(){
        init {
            this.screenKey = javaClass.simpleName
        }

        override fun getFragment(): Fragment {
            return HomeMainFragment.newInstance()
        }
    }

//    class OnlineCustomsScreen: SupportAppScreen(){
//        init {
//            this.screenKey = javaClass.simpleName
//        }
//
//        override fun getFragment(): Fragment {
//            return OnlineCustomsFragment.newInstance()
//        }
//    }

}