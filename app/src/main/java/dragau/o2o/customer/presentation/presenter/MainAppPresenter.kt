package dragau.o2o.customer.presentation.presenter

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.iid.FirebaseInstanceId
import com.jakewharton.retrofit2.adapter.rxjava2.HttpException
import dragau.o2o.customer.App
import dragau.o2o.customer.R
import dragau.o2o.customer.Screens
import dragau.o2o.customer.api.ApiManager
import dragau.o2o.customer.api.TokenInterceptor
import dragau.o2o.customer.models.db.UserDao
import dragau.o2o.customer.models.objects.User
import dragau.o2o.customer.models.shared.DataHolder
import dragau.o2o.customer.presentation.view.MainAppView
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import ru.terrakok.cicerone.Router
import timber.log.Timber
import javax.inject.Inject

@InjectViewState
class MainAppPresenter(private val router: Router) : MvpPresenter<MainAppView>() {
    @Inject
    lateinit var client: ApiManager

    @Inject
    lateinit var userDao: UserDao

    @Inject
    lateinit var tokenInterceptor: TokenInterceptor

    init {
        App.appComponent.inject(this)
    }

    private var disposable: Disposable? = null

    fun updateFcmToken()
    {
        FirebaseInstanceId.getInstance().instanceId
            .addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Timber.e(task.exception)
                    return@OnCompleteListener
                }
//                 Get new Instance ID token
                val token = task.result?.token
            })
    }

    fun checkUserToken()
    {
        updateFcmToken()

        if (DataHolder.userId != null) {
            disposable = userDao.get(DataHolder.userId!!)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { user: User ->
//                        router.newRootScreen(Screens.LoginScreen())

                        tokenInterceptor.token = "Bearer " + user.token

//                        router.newRootScreen(Screens.StoreScreen())
                        router.newRootScreen(Screens.HomeScreen())
//                        router.newRootScreen(Screens.ProductScreen())
//                        router.newRootScreen(Screens.LocationMapScreen())
                    },
                    {

                        viewState?.showError(it)
                    },
                    {
                        viewState?.showError(App.appComponent.context().getString(R.string.user_not_found), -1)
                    }
                )
        }
        else
        {
            router.newRootScreen(Screens.LoginScreen())

//            router.newRootScreen(Screens.HomeScreen())

//            router.newRootScreen(Screens.ProductScreen())
//            router.newRootScreen(Screens.StoreScreen())
        }
    }

    fun showLogin()
    {

        /*var id = 0L
        val u = User(username = "ic_arrow_back", id = id)
        u.phone = "7055717177"


        disposable = Observable.fromCallable { userDao.insert(u) }
            .subscribeOn(Schedulers.computation())
            .observeOn(Schedulers.computation())
            .subscribe {
                id = it

                disposable = userDao.get(id)
                    .subscribeOn(Schedulers.computation())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                        { result: User ->
                            router.newRootScreen(Screens.LoginScreen())
                        },
                        {

                            viewState?.showError(it)
                        },
                        {
                            viewState?.showError("Пользователь не найден!", -1)
                        }
                    )
            }*/
    }

    fun auth() {
        viewState?.showProgress()

        /*disposable = client.userAuth(user)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {    result ->
                    run {
                        viewState?.hideProgress()
                        DataHolder.user = result.data.user
                        DataHolder.userId = result.data.user.id

                        viewState?.goToHome()
                    }
                },
                { error ->
                    run {
                        viewState?.hideProgress()
                    }

                    if (error is HttpException)
                    {
                        if (error.code() == 403)
                        {
                            DataHolder.sharedPref.edit().clear().apply()
                            viewState?.showLogin()
                            return@subscribe
                        }
                    }

                    run {
                        viewState?.showError(error)
                    }
                }
            )*/
    }

    fun showTutorial(){

    }
}
