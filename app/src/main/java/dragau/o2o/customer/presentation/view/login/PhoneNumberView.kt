package dragau.o2o.customer.presentation.view.login

import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import dragau.o2o.customer.presentation.BaseView

@StateStrategyType(OneExecutionStateStrategy::class)
interface PhoneNumberView : BaseView {
    fun verifyPhoneNumber(phoneNumber: String)
    fun clearFocus()
}
