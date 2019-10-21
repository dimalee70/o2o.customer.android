package dragau.o2o.customer.ui.fragment.login

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil

import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.google.firebase.auth.PhoneAuthProvider
import dragau.o2o.customer.App
import dragau.o2o.customer.Constants
import dragau.o2o.customer.R
import dragau.o2o.customer.databinding.FragmentPhoneNumberBinding
import dragau.o2o.customer.presentation.presenter.login.PhoneNumberPresenter
import dragau.o2o.customer.presentation.view.login.PhoneNumberView
import dragau.o2o.customer.ui.fragment.BaseMvpFragment
import kotlinx.android.synthetic.main.fragment_phone_number.*
import net.danlew.android.joda.JodaTimeAndroid
import ru.terrakok.cicerone.Router
//import ru.tinkoff.decoro.MaskImpl
//import ru.tinkoff.decoro.slots.PredefinedSlots
//import ru.tinkoff.decoro.watchers.FormatWatcher
//import ru.tinkoff.decoro.watchers.MaskFormatWatcher
import javax.inject.Inject
import java.util.concurrent.TimeUnit


class PhoneNumberFragment : BaseMvpFragment(), PhoneNumberView {
    override fun clearFocus()
    {
        phoneEt.clearFocus()
        smsEt.requestFocus()
    }

    companion object {
        const val TAG = "PhoneNumberFragment"

        fun newInstance(): PhoneNumberFragment {
            val fragment: PhoneNumberFragment = PhoneNumberFragment()
            val args: Bundle = Bundle()
            fragment.arguments = args
            return fragment
        }
    }

    @Inject
    lateinit var router: Router

    @InjectPresenter
    lateinit var mPhoneNumberPresenter: PhoneNumberPresenter

    @ProvidePresenter
    fun providePresenter(): PhoneNumberPresenter
    {
        return PhoneNumberPresenter(router, smsSent = false)
    }

    lateinit var binding: FragmentPhoneNumberBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        App.appComponent.inject(this)
        super.onCreate(savedInstanceState)
        JodaTimeAndroid.init(context)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_phone_number, container , false)
        val frView : View  = binding.flMain
        binding.loginViewModel = mPhoneNumberPresenter.userRequstModel
        binding.presenter = mPhoneNumberPresenter
        return frView
    }

    override fun verifyPhoneNumber(phoneNumber: String) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
            phoneNumber, // Phone number to verify
            Constants.smsVerificationDelay, // Timeout duration
            TimeUnit.SECONDS, // Unit of timeout
            this.activity as Activity, // Activity (for callback binding)
            mPhoneNumberPresenter.mCallbacks
        ) // OnVerificationStateChangedCallbacks
    }
}
