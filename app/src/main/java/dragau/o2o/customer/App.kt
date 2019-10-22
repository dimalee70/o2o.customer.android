package dragau.o2o.customer

import android.annotation.SuppressLint
import android.content.Context
import androidx.multidex.MultiDex
import androidx.multidex.MultiDexApplication
import com.navin.flintstones.rxwebsocket.RxWebsocket
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.internal.functions.Functions
import io.reactivex.schedulers.Schedulers
import dragau.o2o.customer.di.components.AppComponent
import dragau.o2o.customer.di.components.DaggerAppComponent
import dragau.o2o.customer.di.modules.ApplicationModule
import dragau.o2o.customer.di.modules.RoomModule
import dragau.o2o.customer.di.modules.WSocketModule
import ru.terrakok.cicerone.Cicerone
import ru.terrakok.cicerone.Router
import timber.log.Timber


class App : MultiDexApplication() {
    private var cicerone: Cicerone<Router>? = null

    override fun attachBaseContext(context: Context) {
        super.attachBaseContext(context)
        MultiDex.install(this)
    }

    private var disposable: Disposable? = null

    override fun onCreate() {
        super.onCreate()

        instance = this

        appComponent = DaggerAppComponent.builder()
            .applicationModule(ApplicationModule(this))
            .roomModule(RoomModule(this))
            .wSocketModule(WSocketModule())
            .build()


//        val crashlyticsKit = Crashlytics.Builder()
//            .core(CrashlyticsCore.Builder().disabled(BuildConfig.DEBUG).build())
//            .build()
//        Fabric.with(this, crashlyticsKit)

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        /*logEvents()

         disposable = App.appComponent.getWSocket()
             .connect()
             .flatMap {
                     open -> open.client().send("Hello")
             }
             .observeOn(AndroidSchedulers.mainThread())
             .subscribe(
                 {event ->
                     when (event) {
                         is RxWebsocket.Open -> {
                             Timber.i("CONNECTED")
                         }
                     }

                 },
                 {e ->
                     Timber.e(e)
                 }
             )*/
    }

    @SuppressLint("CheckResult")
    private fun logEvents() {
        App.appComponent.getWSocket().eventStream()
            .observeOn(AndroidSchedulers.mainThread())
            .doOnNext { event ->
                when (event) {
                    is RxWebsocket.Open -> {
                        Timber.i("CONNECTED")
                    }
                    is RxWebsocket.Closed -> {
                        Timber.i("DISCONNECTED")
                    }
                    is RxWebsocket.QueuedMessage<*> -> {
                        Timber.i("[MESSAGE QUEUED]:" + (event as RxWebsocket.QueuedMessage<*>).message()!!.toString())
                    }
                    is RxWebsocket.Message -> try {
                        Timber.i("[MESSAGE RECEIVED]:" + (event as RxWebsocket.Message).data()!!.toString())
                        //Timber.i("[DE-SERIALIZED MESSAGE RECEIVED]:" + (event as RxWebsocket.Message).data(SampleDataModel::class.java).toString())
                        /*log(
                                    String.format(
                                        "[DE-SERIALIZED MESSAGE RECEIVED][id]:%d",
                                        (event as RxWebsocket.Message).data(SampleDataModel::class.java).id()
                                    )
                                )
                                log(
                                    String.format(
                                        "[DE-SERIALIZED MESSAGE RECEIVED][message]:%s",
                                        (event as RxWebsocket.Message).data(SampleDataModel::class.java).message()
                                    )
                                )*/
                    } catch (throwable: Throwable) {
                        Timber.i("[MESSAGE RECEIVED]:" + (event as RxWebsocket.Message).data()!!.toString())
                    }
                }
            }
            .subscribeOn(Schedulers.io())
            .subscribe(Functions.emptyConsumer())
    }

    companion object
    {
        lateinit var appComponent: AppComponent
        lateinit var instance: App
    }
}