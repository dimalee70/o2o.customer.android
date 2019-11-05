package dragau.o2o.customer.extensions

import android.content.Context
import android.net.ConnectivityManager
import androidx.appcompat.app.AlertDialog
import dragau.o2o.customer.moxy.MvpAppCompatFragment
import dragau.o2o.customer.presentation.presenter.dialogs.ConfirmDialogPresenter
import dragau.o2o.customer.presentation.presenter.dialogs.ErrorDialogPresenter


inline fun MvpAppCompatFragment.showErrorAlertDialog(func: ErrorDialogPresenter.() -> Unit, title: String?, message: String?): AlertDialog =
    ErrorDialogPresenter(this.context!!, title, message).apply {
        func()
    }.create(null)

inline fun MvpAppCompatFragment.showConfirmAlertDialog(func: ConfirmDialogPresenter.() -> Unit, title: Int, message: Int): AlertDialog =
    ConfirmDialogPresenter(this.context!!, title, message).apply{
        func()
    }.create(null)

inline fun Context.isConnected(): Boolean{
        return (getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager)
            .activeNetworkInfo?.isConnected == true
    }