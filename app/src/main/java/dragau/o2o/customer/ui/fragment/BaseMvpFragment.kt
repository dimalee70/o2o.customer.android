package dragau.o2o.customer.ui.fragment

import android.content.Context
import android.util.Log
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat.getSystemService
import com.bumptech.glide.load.engine.GlideException
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.jakewharton.retrofit2.adapter.rxjava2.HttpException
import dragau.o2o.customer.R
import dragau.o2o.customer.api.response.ErrorResponse
import dragau.o2o.customer.extensions.showErrorAlertDialog
import dragau.o2o.customer.moxy.MvpAppCompatFragment
import dragau.o2o.customer.presentation.BaseView
import dragau.o2o.customer.presentation.presenter.dialogs.DelayedProgressDialog
import dragau.o2o.customer.presentation.presenter.dialogs.LoadingDialog
import java.io.IOException
import java.net.SocketTimeoutException
import org.json.JSONObject
import timber.log.Timber


open class BaseMvpFragment: MvpAppCompatFragment(), BaseView
{
    val BASE_TAG: String = "BaseMvpFragment"

    private var progressDialog: DelayedProgressDialog? = null
    private var loadingDialog: LoadingDialog? = null
    var errorDialog: AlertDialog? = null

    override fun showError(exception: Throwable) {
        if (errorDialog == null || (errorDialog != null && !errorDialog!!.isShowing))
        {
            //необходимо вытащить responseBody, т.к после сериализации(вызова) response(), он будет всегда возвращать null
            val responseBody = if (exception is HttpException){
                exception.response().errorBody()?.string()
            } else{
                null
            }
            errorDialog = showErrorAlertDialog({
                cancelable = true
                closeIconClickListener {
                    Log.d(BASE_TAG, "Error dialog close button clicked")
                }
            }, getNetworkErrorTitle(exception, responseBody), getErrorMessage(exception, responseBody))
            errorDialog?.show()
        }
    }

    open fun getFragmentTag(): String?
    {
        return null
    }

    override fun showError(message: String?, codeError: Int) {
        if (errorDialog == null)
        {
            var msg = message
            if (msg == null)
            {
                msg = getString(codeError)
            }

            errorDialog = showErrorAlertDialog({
                cancelable = true
                closeIconClickListener {
//                    Timber.d(BASE_TAG, "Error dialog close button clicked")
                }
            }, "Ошибка", msg)
            errorDialog?.show()
        }
    }

    override fun hideProgress() {
        activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)

        progressDialog?.cancel()
        progressDialog = null
    }

    override fun showProgress() {
        activity?.window?.setFlags(
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)

        if (progressDialog == null)
            progressDialog = DelayedProgressDialog(this.context!!)

        progressDialog?.show()
    }

    override fun hideLoading() {
        activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)

        loadingDialog?.cancel()
        loadingDialog = null
    }

    override fun showLoading() {
        activity?.window?.setFlags(
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)

        if (loadingDialog == null)
            loadingDialog = LoadingDialog(this.context!!)

        loadingDialog?.show()
    }

    override fun showRequestSuccessfully(message: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private fun getNetworkErrorTitle(error: Throwable, responseBody: String?): String
    {
        when (error) {
            is HttpException -> {
                when (error.code())
                {
                    404 -> return getString(R.string.bad_server_response)
                    500 -> return getString(R.string.default_unexpected_error_message)
                    502 -> return getString(R.string.default_error_message)
                }

                return getErrorTitle(responseBody)
            }
            is SocketTimeoutException -> return getString(R.string.timed_out)
            is IOException -> return getString(R.string.network_connection_lost)
            is GlideException -> return getString(R.string.bad_connection)

            else -> return if (error.localizedMessage != null) getString(R.string.unknown_error) else ""
        }

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

        val inputManager = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        if (activity?.getCurrentFocus() != null && activity?.getCurrentFocus()?.getWindowToken() != null) {
            inputManager.hideSoftInputFromWindow(activity?.getCurrentFocus()?.getWindowToken(), 0)
        }
    }
}