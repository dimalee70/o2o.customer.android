package dragau.o2o.customer.extensions

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