package dragau.o2o.customer.presentation.presenter

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.arellomobile.mvp.MvpPresenter
import com.arellomobile.mvp.MvpView
import io.reactivex.disposables.CompositeDisposable

abstract class BasePresenter<T : MvpView> : LifecycleObserver, MvpPresenter<T>()  {


    val disposables = CompositeDisposable()

    fun attachLifecycle(lifecycle: Lifecycle) {
        lifecycle.addObserver(this)
    }

    fun detachLifecycle(lifecycle: Lifecycle) {
        lifecycle.removeObserver(this)
    }

    @OnLifecycleEvent(value = Lifecycle.Event.ON_DESTROY)
    fun onPresenterDestroy() {
        disposables.clear()
    }
}