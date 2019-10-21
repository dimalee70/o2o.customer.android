package dragau.o2o.customer.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.transition.ChangeBounds
import android.transition.ChangeClipBounds
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.transition.Explode
import androidx.transition.Fade
import androidx.transition.SidePropagation
import androidx.transition.Slide

import com.arellomobile.mvp.presenter.InjectPresenter
import dragau.o2o.customer.R
import dragau.o2o.customer.Screens
import dragau.o2o.customer.presentation.presenter.LoginInPresenter
import dragau.o2o.customer.presentation.view.LoginInView
import dragau.o2o.customer.ui.common.BackButtonListener
import dragau.o2o.customer.ui.fragment.login.PhoneNumberFragment
import dragau.o2o.customer.ui.fragment.login.SmsCodeFragment
import ru.terrakok.cicerone.android.support.SupportAppNavigator
import ru.terrakok.cicerone.commands.Command
import ru.terrakok.cicerone.commands.Forward
import ru.terrakok.cicerone.commands.Replace

class LoginInActivity : BaseActivity(), LoginInView {

    private var doubleBackToExitPressedOnce = false

    companion object {
        const val TAG = "LoginInActivity"
        fun getIntent(context: Context): Intent = Intent(context, LoginInActivity::class.java)
        val LOGIN_TRANSITION = "login_transition"
    }

    @InjectPresenter
    lateinit var mLoginInPresenter: LoginInPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_in)


        if (savedInstanceState == null) {
            navigator.applyCommands(arrayOf<Command>(Replace(Screens.PhoneNumberScreen())))
        }
    }

    override fun onResumeFragments() {
        super.onResumeFragments()

        navigatorHolder.setNavigator(navigator)
    }

    override fun onPause() {
        navigatorHolder.removeNavigator()
        super.onPause()
    }

    var navigator:SupportAppNavigator = object : SupportAppNavigator(this, R.id.login_frame_layout) {
        override fun setupFragmentTransaction(
            command: Command?,
            currentFragment: Fragment?,
            nextFragment: Fragment?,
            fragmentTransaction: FragmentTransaction?
        ) {
            if (command is Forward
                && currentFragment is PhoneNumberFragment
                && nextFragment is SmsCodeFragment
            ) {
                setupSharedElement(
                    (currentFragment as PhoneNumberFragment?)!!,
                    (nextFragment as SmsCodeFragment?)!!,
                    fragmentTransaction!!
                )
            }
        }
    }

    private fun setupSharedElement(
        phoneFragment: PhoneNumberFragment,
        smsFragment: SmsCodeFragment,
        fragmentTransaction: FragmentTransaction
    ) {
        /*if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            return
        }

        val changeBounds = ChangeBounds()
            .apply { duration = 1000 }
        smsFragment.sharedElementEnterTransition = changeBounds
        //smsFragment.sharedElementReturnTransition = changeBounds
        //phoneFragment.sharedElementEnterTransition = changeBounds
        phoneFragment.sharedElementReturnTransition = changeBounds

        val view = phoneFragment.binding.logoImage
        view.transitionName = LOGIN_TRANSITION
        fragmentTransaction.addSharedElement(view , LOGIN_TRANSITION)*/

    }

    override fun onBackPressed() {
        val fragment = supportFragmentManager.findFragmentById(R.id.login_frame_layout)
        if (fragment != null
            && fragment is BackButtonListener
            && (fragment as BackButtonListener).onBackPressed()
//            && (fragment is ConfirmCodeFragment ||
//            (fragment is BackButtonListener
//                && (fragment as BackButtonListener).onBackPressed()))
        ) {
            return
        }
//        else if (fragment is RegistrationFragment || fragment is ConfirmCodeFragment)
//        {
//            if(doubleBackToExitPressedOnce){
//                super.onBackPressed()
//                return
//            }
//            else {
//                doubleBackToExitPressedOnce = true
//                Toast.makeText(applicationContext, "Please click BACK again to exit", Toast.LENGTH_SHORT).show()
//                Handler().postDelayed(Runnable { doubleBackToExitPressedOnce = false }, 2000)
//            }
//        }
        else {
            super.onBackPressed()
        }
    }
}
