package dragau.o2o.customer.ui.activity

import android.content.Context
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AlertDialog
import com.bumptech.glide.load.engine.GlideException
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dragau.o2o.customer.App
import dragau.o2o.customer.Constants
import dragau.o2o.customer.R
import dragau.o2o.customer.api.response.ErrorResponse
import dragau.o2o.customer.extensions.showErrorAlertDialog
import dragau.o2o.customer.moxy.MvpActivity
import dragau.o2o.customer.presentation.BaseView
import dragau.o2o.customer.presentation.presenter.dialogs.DelayedProgressDialog
import dragau.o2o.customer.presentation.presenter.dialogs.LoadingDialog
import retrofit2.HttpException
import ru.terrakok.cicerone.NavigatorHolder
import timber.log.Timber
import java.io.IOException
import java.net.SocketTimeoutException
import javax.inject.Inject


open class BaseActivity : MvpActivity(), BaseView {

    val BASE_TAG: String = "BaseActivity"

    private lateinit var currentTheme: String

    @Inject
    internal lateinit var navigatorHolder: NavigatorHolder

    @Inject
    lateinit var sharedPref: SharedPreferences

    //internal var navigator: SupportAppNavigator? = null

    private lateinit var firebaseAnalytics: FirebaseAnalytics

    protected var isDynamicThemingOn: Boolean = Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP
    protected var isFullScreen: Boolean = false

    private var progressDialog: DelayedProgressDialog? = null

    private var loadingDialog: LoadingDialog? = null

    var errorDialog: AlertDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        App.appComponent.inject(this)
        super.onCreate(savedInstanceState)

        firebaseAnalytics = FirebaseAnalytics.getInstance(this)

        if (!isDynamicThemingOn && isFullScreen)
        {
            return
        }

        //sharedPref = PreferenceManager.getDefaultSharedPreferences(this)
        currentTheme = if (isDynamicThemingOn)
        {
            sharedPref.getString(Constants.themePrefsKey, Constants.lightTheme)!!
        } else {
            Constants.lightTheme
        }
        setAppTheme(currentTheme)
    }

    override fun onResume() {
        super.onResume()

        if (!isDynamicThemingOn && isFullScreen)
        {
            return
        }


        if (isDynamicThemingOn)
        {
            val theme = sharedPref.getString(Constants.themePrefsKey, Constants.lightTheme)
            if (currentTheme != theme)
                recreate()
        }
        else
        {
            if (currentTheme != Constants.lightTheme) {
                recreate()
            }
        }
    }

    fun setAppTheme(currentTheme: String) {
        when (currentTheme)
        {
            Constants.lightTheme -> {
                setTheme(R.style.Theme_App_Light)
            }
            else -> {
                setTheme(R.style.Theme_App_Dark)
            }
        }
    }

    override fun showError(exception: Throwable) {
        runOnUiThread {
            if (errorDialog == null || (errorDialog != null && !errorDialog!!.isShowing)) {
                val responseBody = if (exception is HttpException){
                    exception.response()!!.errorBody()?.string()
                } else{
                    null
                }
                errorDialog = showErrorAlertDialog({
                    cancelable = true
                    closeIconClickListener {
                        Timber.d("Error dialog close button clicked")
                    }
                }, getNetworkErrorTitle(exception, responseBody), getErrorMessage(exception, responseBody))
                errorDialog?.show()
            }
        }
    }

    override fun showError(message: String?, codeError: Int) {
        var msg = message
        if (msg == null)
        {
            msg = getString(codeError)
        }
        runOnUiThread {
            if (errorDialog == null) {
                errorDialog = showErrorAlertDialog({
                    cancelable = true
                    closeIconClickListener {
                        Timber.d("Error dialog close button clicked")
                    }
                }, "Ошибка", msg)
                errorDialog?.show()
            }
        }
    }

    override fun hideProgress() {
        runOnUiThread {
            window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
            progressDialog?.cancel()
            progressDialog = null
        }
    }

    override fun showProgress() {
        runOnUiThread {
            if (progressDialog == null)
                progressDialog = DelayedProgressDialog(this)

            window.setFlags(
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
            )
            progressDialog?.show()
        }
    }

    override fun showLoading() {
        runOnUiThread{
            if (loadingDialog == null)
                loadingDialog = LoadingDialog(this)
            window.setFlags(
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
            )
            loadingDialog?.show()
        }
    }

    override fun hideLoading() {
        runOnUiThread{
            window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
            loadingDialog?.cancel()
            loadingDialog = null
        }
    }

    override fun showRequestSuccessfully(message: String) {

    }

    fun getNetworkErrorTitle(error: Throwable, responseBody: String?): String
    {
        if (error is HttpException)
        {
            when (error.code())
            {
                404 -> return getString(R.string.bad_server_response)
                500 -> return getString(R.string.default_unexpected_error_message)
                502 -> return getString(R.string.default_error_message)
            }
            return getErrorTitle(responseBody)
        } else if (error is SocketTimeoutException)
        {
            return getString(R.string.timed_out)
        } else if (error is IOException)
        {
            return getString(R.string.network_connection_lost)
        }
        else if (error is GlideException)
        {
            return getString(R.string.bad_connection)
        }

        return if (error.localizedMessage != null) getString(R.string.unknown_error) else ""
    }

    private fun getErrorTitle(responseBody: String?): String {
        try {
            if (responseBody ==  null) {
                return getString(R.string.unknown_error_from_backend)
            }

            val gson = Gson()
            val errorRespType = object : TypeToken<ErrorResponse>() {}.type
            val resp = gson.fromJson<ErrorResponse>(responseBody, errorRespType)
            if (resp.messageList?.isNotEmpty() == true) {
                return resp.messageList!!.joinToString(separator = "\n") {"\'${it.message}\'" }
            }

            return getString(R.string.unknown_error_from_backend)
        } catch (e: Exception) {
            return e.localizedMessage!!
        }
    }


    private fun getErrorMessage(exception: Throwable, responseBody: String?): String? {
        if (exception !is HttpException) {

            return null
        }
        try {

            val gson = Gson()
            val errorRespType = object : TypeToken<ErrorResponse>() {}.type
            val resp = gson.fromJson<ErrorResponse>(responseBody!!, errorRespType)


            if (resp.messageList?.isNotEmpty() == true) {
                return resp.messageList!!.joinToString(separator = "\n") {"\'${it.description}\'" }
            }

            return null
        } catch (e: Exception) {
            return null
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        if (progressDialog != null)
        {
            progressDialog?.dialog?.setOnDismissListener(null)
            progressDialog?.cancel()
        }

        if (errorDialog != null)
        {
            errorDialog?.setOnDismissListener(null)
            errorDialog?.dismiss()
        }
    }

    override fun hideKeyboard() {

        val inputManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        if (getCurrentFocus() != null && getCurrentFocus()?.getWindowToken() != null) {
            inputManager.hideSoftInputFromWindow(getCurrentFocus()?.getWindowToken(), 0)
        }
    }
}