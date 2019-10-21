package dragau.o2o.customer.ui.activity.product

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.transition.ChangeBounds
import android.util.Base64
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction

import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.google.zxing.integration.android.IntentIntegrator
import com.google.zxing.integration.android.IntentResult
import com.theartofdev.edmodo.cropper.CropImage
import dragau.o2o.customer.App
import dragau.o2o.customer.R
import dragau.o2o.customer.Screens
import dragau.o2o.customer.api.requests.ProductRegisterViewModel
import dragau.o2o.customer.presentation.presenter.product.ProductPresenter
import dragau.o2o.customer.presentation.view.product.ProductView
import dragau.o2o.customer.ui.activity.BaseActivity
import dragau.o2o.customer.ui.fragment.product.ProductRegisterFragment
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.activity_scan.*
import ru.terrakok.cicerone.Router
import ru.terrakok.cicerone.android.support.SupportAppNavigator
import ru.terrakok.cicerone.commands.Command
import ru.terrakok.cicerone.commands.Forward
import ru.terrakok.cicerone.commands.Replace
import timber.log.Timber
import java.io.ByteArrayOutputStream
import javax.inject.Inject


class ProductActivity : BaseActivity(), ProductView {
    companion object {
        const val TAG = "ProductActivity"
        fun getIntent(context: Context): Intent = Intent(context, ProductActivity::class.java)
        val PRODUCT_TRANSITION = "product_transition"
    }

    @Inject
    lateinit var router: Router

    @Inject
    lateinit var productRegisterViewModel: ProductRegisterViewModel

    @InjectPresenter
    lateinit var mProductPresenter: ProductPresenter

    @ProvidePresenter
    fun providePresenter(): ProductPresenter
    {
        return ProductPresenter(router, productRegisterViewModel)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        App.appComponent.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product)
//        pageTv.text = "HELP ME"
        setSupportActionBar(prod_toolbar)
        back_button_iv.setOnClickListener{
            //            println("Click")
            onBackPressed()
        }
        if(savedInstanceState == null){
            navigator.applyCommands(arrayOf<Command>(Replace(Screens.ProductRegisterScreen())))
        }
    }

    fun encodeImage(bm: Bitmap): String {
        var baos = ByteArrayOutputStream()
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        return Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT)
    }

    override fun onResumeFragments() {
        super.onResumeFragments()
        navigatorHolder.setNavigator(navigator)
    }

    override fun onPause() {
        navigatorHolder.removeNavigator()
        super.onPause()
    }


    var navigator: SupportAppNavigator = object : SupportAppNavigator(this, R.id.activity_product_frame_layout) {
        override fun setupFragmentTransaction(
            command: Command?,
            currentFragment: Fragment?,
            nextFragment: Fragment?,
            fragmentTransaction: FragmentTransaction?
        ) {
            if (command is Forward
                && currentFragment is ProductRegisterFragment
                && nextFragment == null
            ) {
                setupSharedElement(
                    (currentFragment as ProductRegisterFragment?)!!,
                    nextFragment,
                    fragmentTransaction!!
                )
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data)
            if (resultCode == Activity.RESULT_OK) {
//                avatarIv.setImageURI(result.uri)
                val inputStream = contentResolver.openInputStream(result.uri)
                val selectedImage = BitmapFactory.decodeStream(inputStream)
                val encodedImage = encodeImage(selectedImage)
                mProductPresenter.changeImage(encodedImage)
//                mStorePresenter.changeImage(result.uri)
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Toast.makeText(this, "Croppinf failed: " + result.error, Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun setupSharedElement(
        productRegisterFragment: ProductRegisterFragment,
        nextFragment: Nothing? = null,
        fragmentTransaction: FragmentTransaction
    ) {
        val changeBounds = ChangeBounds()//.apply { duration = 10000 }
        productRegisterFragment.sharedElementEnterTransition = changeBounds
        productRegisterFragment.sharedElementReturnTransition = changeBounds
//
//        val view = productRegisterFragment.binding.makePhotoBtn
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            view.transitionName = LoginInActivity.LOGIN_TRANSITION
//        }
//        fragmentTransaction.addSharedElement(view , ProductActivity.PRODUCT_TRANSITION)
    }

    override fun onBackPressed() {
        super.onBackPressed()
//        productRegisterViewModel = ProductRegisterViewModel()
    }
}
