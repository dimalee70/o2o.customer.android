package dragau.o2o.customer.presentation.view.product

import com.arellomobile.mvp.viewstate.strategy.SkipStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import dragau.o2o.customer.presentation.BaseView


interface ProductRegisterView : BaseView {

    @StateStrategyType(SkipStrategy::class)
    fun showPictureDialog()
}
