package dragau.o2o.customer.ui.fragment.login

import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import androidx.databinding.DataBindingUtil
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import dragau.o2o.customer.App
import dragau.o2o.customer.Constants
import dragau.o2o.customer.R
import dragau.o2o.customer.api.requests.LoginRequestModel
import dragau.o2o.customer.databinding.FragmentSmsCodeBinding
import dragau.o2o.customer.presentation.presenter.login.SmsCodePresenter
import dragau.o2o.customer.presentation.view.login.SmsCodeView
import dragau.o2o.customer.ui.activity.LoginInActivity
import dragau.o2o.customer.ui.common.BackButtonListener
import dragau.o2o.customer.ui.fragment.BaseMvpFragment
import ru.terrakok.cicerone.Router
import javax.inject.Inject

class SmsCodeFragment : BaseMvpFragment(), SmsCodeView, BackButtonListener {
    var userRequestModel: LoginRequestModel? = null
    companion object {
        const val EXTRA_CODE_PHONE = "EXTRA_CODE_PHONE"
        const val TAG = "SmsCodeFragment"

        fun newInstance(userRequestModel: LoginRequestModel?): SmsCodeFragment {
            val fragment: SmsCodeFragment = SmsCodeFragment()
            val args: Bundle = Bundle()
            args.putSerializable(Constants.EXTRA_CODE_PHONE, userRequestModel)
            fragment.arguments = args
            return fragment
        }
    }

    @InjectPresenter
    lateinit var mSmsCodePresenter: SmsCodePresenter

    @ProvidePresenter
    fun providePresenter(): SmsCodePresenter
    {
        return SmsCodePresenter(router, userRequestModel)
    }


    @Inject
    lateinit var router: Router

    lateinit var binding: FragmentSmsCodeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        this.userRequestModel = arguments?.getSerializable(Constants.EXTRA_CODE_PHONE) as LoginRequestModel?
        App.appComponent.inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_sms_code, container , false)
        val frView : View  = binding.flMain

        binding.presenter = mSmsCodePresenter
        return frView
    }

    override fun onBackPressed(): Boolean {
        mSmsCodePresenter.onBackPressed()
        return true
    }


    lateinit var sms: ImageView
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sms = view.findViewById<View>(R.id.avatar_imageView) as ImageView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            sms.transitionName = LoginInActivity.LOGIN_TRANSITION
        }
    }
}
