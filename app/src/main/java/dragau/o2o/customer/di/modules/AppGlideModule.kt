package dragau.o2o.customer.di.modules

import android.content.Context
import com.bumptech.glide.Glide
import com.bumptech.glide.Registry
import com.bumptech.glide.annotation.Excludes
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.integration.okhttp3.OkHttpLibraryGlideModule
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader
import com.bumptech.glide.module.AppGlideModule
import okhttp3.OkHttpClient
import com.bumptech.glide.load.model.GlideUrl
import dagger.Module
import java.io.InputStream
import dagger.Subcomponent
import javax.inject.Inject
import dagger.Provides
import dragau.o2o.customer.App
import java.util.concurrent.TimeUnit
import javax.inject.Named


@Excludes(OkHttpLibraryGlideModule::class)
@GlideModule
@Module(includes = [NetworkModule::class])
class AppGlideModule : AppGlideModule() {
    @Inject
    lateinit var okHttpClient: OkHttpClient

    init {
        App.appComponent
            .glideComponentBuilder()
            .glideModule(GlideDaggerModule())
            .build()
            .inject(this)
    }

    override fun isManifestParsingEnabled() = false

    override fun registerComponents(context: Context, glide: Glide, registry: Registry) {
        super.registerComponents(context, glide, registry)
        registry.replace(GlideUrl::class.java, InputStream::class.java, OkHttpUrlLoader.Factory(okHttpClient))
    }
}

@Subcomponent(modules = [(GlideDaggerModule::class)])
interface GlideComponent {

    fun inject(newOkHttpLibraryGlideModule: dragau.o2o.customer.di.modules.AppGlideModule)

    @Subcomponent.Builder
    interface Builder {

        fun glideModule(glideModule: GlideDaggerModule): Builder

        fun build(): GlideComponent
    }
}

@Module
class GlideDaggerModule



/*@GlideModule
class AppGlideModule : AppGlideModule() {

    override fun registerComponents(context: Context, glide: Glide, registry: Registry) {
        val client = OkHttpClient.Builder()
            .connectTimeout(Constants.connectTimeout, TimeUnit.SECONDS)
            .writeTimeout(Constants.writeTimeout, TimeUnit.SECONDS)
            .readTimeout(Constants.readTimeout, TimeUnit.SECONDS)
            .build()

        val factory = OkHttpUrlLoader.Factory(client)

        glide.registry.replace(GlideUrl::class.java, InputStream::class.java, factory)
    }
}*/