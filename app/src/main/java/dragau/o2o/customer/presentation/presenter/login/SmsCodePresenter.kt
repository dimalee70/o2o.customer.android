package dragau.o2o.customer.presentation.presenter.login

import android.content.SharedPreferences
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import io.reactivex.disposables.Disposable
import dragau.o2o.customer.App
import dragau.o2o.customer.Screens
import dragau.o2o.customer.api.ApiManager
import dragau.o2o.customer.api.requests.LoginRequestModel
import dragau.o2o.customer.presentation.view.login.SmsCodeView
import ru.terrakok.cicerone.Router
import javax.inject.Inject

@InjectViewState
class SmsCodePresenter(private val router: Router, private val userRequestModel: LoginRequestModel? ) : MvpPresenter<SmsCodeView>() {
    @Inject
    lateinit var client: ApiManager

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    private var disposable: Disposable? = null

    init {
        App.appComponent.inject(this)
    }

    fun onBackPressed() {
        router.exit()
    }

    fun validateCode()
    {
        router.navigateTo(Screens.HomeScreen())
    }
}
