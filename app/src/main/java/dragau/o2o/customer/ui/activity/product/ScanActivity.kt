package dragau.o2o.customer.ui.activity.product

import android.content.Intent
import android.os.Bundle

import com.arellomobile.mvp.presenter.InjectPresenter
import com.google.zxing.BarcodeFormat

import android.view.KeyEvent
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.google.zxing.ResultPoint
import com.journeyapps.barcodescanner.BarcodeCallback
import com.journeyapps.barcodescanner.BarcodeResult
import com.journeyapps.barcodescanner.CaptureManager
import dragau.o2o.customer.App
import dragau.o2o.customer.R
import dragau.o2o.customer.Screens
import dragau.o2o.customer.api.requests.ProductRegisterViewModel
import dragau.o2o.customer.databinding.ActivityScanBinding
import dragau.o2o.customer.extensions.showExistsAlertDialog
import dragau.o2o.customer.presentation.presenter.product.ScanPresenter
import dragau.o2o.customer.presentation.view.ScanView
import dragau.o2o.customer.ui.activity.BaseActivity
import kotlinx.android.synthetic.main.activity_scan.*
import kotlinx.android.synthetic.main.content_scanner.*
import ru.terrakok.cicerone.Router
import ru.terrakok.cicerone.Screen
import javax.inject.Inject


class ScanActivity : BaseActivity(), ScanView,
    BarcodeCallback
{
    override fun openProduct() {
        var intent = Intent(this, ProductActivity::class.java)
        this.startActivity(intent)

//        router.navigateTo(Screens.ProductScreen())
    }

    @Inject
    lateinit var router: Router

    @InjectPresenter
    lateinit var mScanPresenter: ScanPresenter

    @Inject
    lateinit var productRegisterViewModel: ProductRegisterViewModel

    @ProvidePresenter
    fun providePresenter(): ScanPresenter
    {
        return ScanPresenter(router, productRegisterViewModel)
    }

    lateinit var binding: ActivityScanBinding

    private var productExistDialog: AlertDialog? = null

    override fun barcodeResult(result: BarcodeResult?) {

//        if(result!!.barcodeFormat == BarcodeFormat.CODE_128
//            || result.barcodeFormat == BarcodeFormat.CODE_39
//            || result.barcodeFormat == BarcodeFormat.EAN_8
//        ) {
//            Toast.makeText(applicationContext, result.text, Toast.LENGTH_SHORT).show()
            productRegisterViewModel.clearObject()
            mScanPresenter.checkProduct(result!!.text)
//            finish()
//            productRegisterViewModel.barCode = result.text
//            Timber.i("Result from barcode " + result.text)
//            router.let {
//                it.navigateTo(Screens.ProductRegisterScreen())
//            }
//            router.navigateTo(Screens.ProductScreen())
//            mScanPresenter.navigateToRegisterScreen()
//            finish()
//            router.exit()
//        }
//        else
//        {
////            println("Error")
//            Toast.makeText(applicationContext, "Error", Toast.LENGTH_SHORT).show()
//        }
//        .setGravity(Gravity.LEFT,200,200).show()
    }

    override fun possibleResultPoints(resultPoints: MutableList<ResultPoint>?) {
    }

    private var capture: CaptureManager? = null
    private var isFlashLightOn = false

//    override fun onTorchOn() {
//        switchFlashlight.setText(R.string.turn_off_flashlight);
//    }
//
//    override fun onTorchOff() {
//        switchFlashlight.setText(R.string.turn_on_flashlight);
//    }



    override fun onCreate(savedInstanceState: Bundle?) {
        App.appComponent.inject(this)
        super.onCreate(savedInstanceState)

//        setContentView(R.layout.activity_scan)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_scan) as ActivityScanBinding
        binding.presenter = mScanPresenter

        back_button_iv.setOnClickListener(){
//            println("Click")
            super.onBackPressed()
        }
//        zxingBarcodeScanner.decodeContinuous(this)
//        zxingBarcodeScanner.viewFinder.
//        zxingBarcodeScanner.setTorchListener(this)


//        if (!hasFlash()) {
//            switchFlashlight.setVisibility(View.GONE)
//        } else {
//            switchFlashlight.setOnClickListener(object : View.OnClickListener {
//                override fun onClick(view: View) {
//                    switchFlashlight()
//                }
//            })
//        }

        capture = CaptureManager(this, zxingBarcodeScanner)
        capture!!.initializeFromIntent(intent, savedInstanceState)
        zxingBarcodeScanner.decodeContinuous(this)
//        zxingBarcodeScanner.decodeSingle(this)
//        capture!!.decode()
    }

//    fun switchFlashlight() {
//        if (isFlashLightOn) {
//            zxingBarcodeScanner.setTorchOff()
//            isFlashLightOn = false
//        } else {
//            zxingBarcodeScanner.setTorchOn()
//            isFlashLightOn = true
//        }
//    }

//    private fun hasFlash(): Boolean {
//        return applicationContext.packageManager
//            .hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)
//    }

    override fun onResume() {
        super.onResume()
        capture!!.onResume()
    }

    override fun onPause() {
        super.onPause()
        capture!!.onPause()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        capture!!.onSaveInstanceState(outState)
    }

    override fun onDestroy() {
        super.onDestroy()
        capture!!.onDestroy()
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        return zxingBarcodeScanner.onKeyDown(keyCode, event) || super.onKeyDown(keyCode, event)
    }

    override fun showProductExistsDialog() {
        if (productExistDialog == null){
            productExistDialog = showExistsAlertDialog {
                cancelable = true
                okBtnClicklistener{
                    dismissFunc
//                    router.exit()
                }
                showBtnClickListener {
                    openProduct()

//                    router.navigateTo(Screens.ProductScreen())
                }
            }
        }
        productExistDialog?.show()
    }

//
//    override fun showConfirm() {
//        if(confirmDialog == null){
//            confirmDialog = showConfirmAlertDialog ({
//                cancelable = true
//                yesBtnClickListener{
//                    imageList.images!!.removeAt(binding.imageVp.currentItem)
//                    if(imageList.images!!.size == 0){
//                        router.exit()
//                    }
//                    binding.imageVp.adapter!!.notifyDataSetChanged()
//                    activity!!.pageTv!!.text = (binding.imageVp.currentItem + 1).toString() + " из " + imageList.images!!.size
//                }
//                noBtnClickListener(dismissFunc)
//            }, R.string.confirm_title,  R.string.confirm_message)
//        }
//        confirmDialog?.show()
//    }






//    private var mScannerView: ZXingScannerView? = null
//
//    companion object {
//        const val TAG = "ScanActivity"
//        fun getIntent(context: Context): Intent = Intent(context, ScanActivity::class.java)
//    }
//
//    @InjectPresenter
//    lateinit var mScanPresenter: ScanPresenter
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        mScannerView = ZXingScannerView(this)
//        setContentView(mScannerView)
//        println("OnCreate")
//    }
//
//    override fun handleResult(p0: Result?) {
////        if(p0!!.barcodeFormat == BarcodeFormat.CODE_128)
//        Timber.i("Result from qr code " + p0!!.text)
//
//        onBackPressed()
//    }
//
//    override fun onPause() {
//        super.onPause()
//        mScannerView!!.stopCamera()
//    }
//
//    override fun onResume() {
//        super.onResume()
//        mScannerView!!.setResultHandler(this)
//        mScannerView!!.startCamera()
//    }
}
