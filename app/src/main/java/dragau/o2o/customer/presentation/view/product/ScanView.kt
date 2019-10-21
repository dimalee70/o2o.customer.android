package dragau.o2o.customer.presentation.view

import com.arellomobile.mvp.MvpView
import dragau.o2o.customer.presentation.BaseView

interface ScanView : BaseView {
    fun showProductExistsDialog()
    fun openProduct()
}
