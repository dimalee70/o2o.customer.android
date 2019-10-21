package dragau.o2o.customer.di.modules

import com.navin.flintstones.rxwebsocket.RxWebsocket
import dagger.Module
import dagger.Provides
import dragau.o2o.customer.di.CustomApplicationScope
import okhttp3.OkHttpClient

@Module(includes = [NetworkModule::class])
class WSocketModule {
    @Provides
    @CustomApplicationScope
    fun getWebSocket(client: OkHttpClient): RxWebsocket {
        return RxWebsocket.Builder()
            //.addConverterFactory(//YOUR OWN CONVERTOR)
            // .addReceiveInterceptor(data -> //Intercept the received data)
            .build(client, "wss://echo.websocket.org")
    }
}