package dragau.o2o.customer.di.modules

import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.Retrofit
import com.google.gson.Gson
import okhttp3.OkHttpClient
import dagger.Provides
import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import dagger.Module
import dragau.o2o.customer.Constants
import dragau.o2o.customer.api.ApiManager
import dragau.o2o.customer.di.CustomApplicationScope


@Module(includes = [NetworkModule::class])
class ServiceUtilModule {
    @Provides
    @CustomApplicationScope
    fun getGson(): Gson {
        val gsonBuilder = GsonBuilder()
        return gsonBuilder.create()
    }

    @Provides
    @CustomApplicationScope
    fun getServiceUtil(retrofit: Retrofit): ApiManager {
        return retrofit.create(ApiManager::class.java)
    }

//    @Provides
//    @Singleton
//    fun provideOkhttp(tokenInterceptor: TokenInterceptor): OkHttpClient{
//        val interceptor = HttpLoggingInterceptor()
//        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
//        val okHttpBuilder =
//            OkHttpClient().newBuilder().addInterceptor(interceptor)
//                .addInterceptor(tokenInterceptor)
//        return okHttpBuilder.build()
//    }

    @Provides
    @CustomApplicationScope
    fun getRetrofit(okHttpClient: OkHttpClient, gson: Gson): Retrofit {

        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(gson))
            .baseUrl(Constants.apiEndpoint)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            //.addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
    }
}