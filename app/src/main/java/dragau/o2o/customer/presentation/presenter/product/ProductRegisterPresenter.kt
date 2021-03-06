package dragau.o2o.customer.presentation.presenter.product

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import androidx.core.content.ContextCompat.startActivity
import androidx.databinding.ObservableArrayList
import androidx.lifecycle.MutableLiveData
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.google.gson.JsonObject
import com.google.zxing.integration.android.IntentIntegrator
import dragau.o2o.customer.App
import dragau.o2o.customer.Constants
import dragau.o2o.customer.Screens
import dragau.o2o.customer.api.ApiManager
import dragau.o2o.customer.api.requests.ProductRegisterRequest
import dragau.o2o.customer.api.requests.ProductRegisterViewModel
import dragau.o2o.customer.api.response.ProductCategoriesResponce
import dragau.o2o.customer.models.db.LookupDao
import dragau.o2o.customer.models.db.ParameterDao
import dragau.o2o.customer.models.enums.ParameterType
import dragau.o2o.customer.models.objects.BaseParameter
import dragau.o2o.customer.models.objects.Product
import dragau.o2o.customer.models.objects.ProductBarcode
import dragau.o2o.customer.models.objects.ProductImage
import dragau.o2o.customer.models.shared.DataHolder
import dragau.o2o.customer.presentation.presenter.BasePresenter
import dragau.o2o.customer.presentation.view.product.ProductRegisterView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import org.joda.time.DateTime
import ru.terrakok.cicerone.Router
import timber.log.Timber
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList
import kotlin.collections.LinkedHashSet

@InjectViewState
class ProductRegisterPresenter(private var router: Router, var productRegisterViewModel: ProductRegisterViewModel,
                               private var products: ObservableArrayList<Product>): BasePresenter<ProductRegisterView>() {

    @Inject
    lateinit var client: ApiManager

    @Inject
    lateinit var parametersDao: ParameterDao

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    var liveProductCategoriesResponse = MutableLiveData<ProductCategoriesResponce>()

    var productsLinkedList: LinkedList<Product>

    private var disposable: Disposable? = null

    init {
        App.appComponent.inject(this)

        productsLinkedList = LinkedList(products.toMutableList())

        var oldList: MutableList<BaseParameter>? = null
        if (!productRegisterViewModel.parameters.isNullOrEmpty())
        {
            productRegisterViewModel.parameters!!.forEach {
                val value = parametersDao.get(it.id!!).value
                if (value != null) {
                    it.rootId = value.toString()
                }
            }
            oldList = productRegisterViewModel.parameters!!.toMutableList()
        }

        productRegisterViewModel.parameters = ObservableArrayList<BaseParameter>()
        val header = BaseParameter("-2", ParameterType.HEADER, "", null)
        header.presenter = this
        productRegisterViewModel.parameters?.add(header)

        if (oldList != null)
        {
            productRegisterViewModel.parameters?.addAll(oldList)
        }

        productRegisterViewModel.parameters?.add(BaseParameter("5", ParameterType.BARCODE, "Штрих-код", productRegisterViewModel.productBarcode))
        val footer = BaseParameter("-1", ParameterType.FOOTER, "", null)
        footer.presenter = this
        productRegisterViewModel.parameters?.add(footer)
    }

    @SuppressLint("CheckResult")
    fun registerStore(){
        viewState.showLoading()

        if (productRegisterViewModel.productId != null){
            updateProduct()
        }
        else {
            createProduct()
        }
    }

    @SuppressLint("CheckResult")
    private fun createProduct(){
        client.createProduct(ProductRegisterRequest.from(productRegisterViewModel))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { result ->
                    run {
                        productRegisterViewModel.productId = result.resultObject
////                        if (productRegisterViewModel.imageUri != null)
//                        products.add(Product(productId = productRegisterViewModel.productId,
//                            name = productRegisterViewModel.title))
                        if (!uploadPhoto())
                        {
                            viewState?.hideLoading()
                            router.exit()
                        }
//
                    }
                },
                { error ->
                    run {

                        viewState.showError(error)
                    }
                }
            )
    }

    @SuppressLint("CheckResult")
    private fun updateProduct(){
        client.updateProduct(ProductRegisterRequest.from(productRegisterViewModel))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { result ->
                    run {
//                        updatePhoto()
//                        println(result)
                        if (!uploadPhoto())
                        {
                            viewState?.hideLoading()
                            router.exit()
                        }
                    }
                },
                { error ->
                    run {
                        viewState.hideLoading()
                        viewState.showError(error)
                    }
                }
            )
    }

    fun makePhoto() {
        viewState?.showPictureDialog()
    }

    fun showParameters() {
        router.navigateTo(Screens.AddParameterScreen())
    }

    fun showLookup(item: BaseParameter) {
        item.presenter = this
        router.navigateTo(Screens.LookupScreen(item.value.toString(), ArrayList()))
    }

    fun getProductCategoris(){
        disposables.add(
            client.getProductategories()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        result ->
                        liveProductCategoriesResponse.value = result
                    },
                    {
                        error ->
                        run{
                            viewState.showError(error)
                        }
                    }
                )
        )
    }

    fun observeForProductCategoriesResponseBoundary(): MutableLiveData<ProductCategoriesResponce>{
        return liveProductCategoriesResponse
    }

    @SuppressLint("CheckResult")
    private fun uploadPhoto(): Boolean{
        if (productRegisterViewModel.imageUri == null)
        {
            products.add(Product(productId = productRegisterViewModel.productId,
                name = productRegisterViewModel.title))
            return false
        }

        val jsonObject = JsonObject()
        jsonObject.addProperty(Constants.objectIdKey, productRegisterViewModel.productId)
        jsonObject.addProperty(Constants.base64BodyKey, productRegisterViewModel.imageUri)
        jsonObject.addProperty(Constants.mimeTypeKey, Constants.mimeType)

        client.uploadProductPhoto(jsonObject)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    result ->
                    run {
                        viewState.hideLoading()
                        var photo = Stack<ProductImage>()
                        photo.push(ProductImage(annotationId = result.resultObject, body = productRegisterViewModel.imageUri))
//                        products.filter {
//                            s -> s.productId == productRegisterViewModel.productId
//                        }.single().productThumbnails = photo

//                        var product = products.filter {
//                            it.productId == productRegisterViewModel.productId
//                        }

                        var idx: Int = products.indexOfFirst{
                            it.productId == productRegisterViewModel.productId
                        }
                        if(idx < 0 ){
                            productsLinkedList.addFirst(
                                Product(productId = productRegisterViewModel.productId,
                                    name = productRegisterViewModel.title, productThumbnails = photo)
                            )
                        }else
                        {
                            productsLinkedList.set(idx,
                                Product(productId = productRegisterViewModel.productId,
                                    name = productRegisterViewModel.title, productThumbnails = photo)
                            )
                        }

                        products.clear()
                        products.addAll(productsLinkedList)
//                        if (product)




//                        products.add(Product(productId = productRegisterViewModel.productId,
//                            name = productRegisterViewModel.title, productThumbnails = photo))

                        router.exit()
                    }
                },
                {
                    error ->
                    run {
                        viewState.hideLoading()
                        viewState.showError(error)
                }
                }
            )

        return true
    }

    @SuppressLint("CheckResult")
    private fun updatePhoto(){
        val jsonObject = JsonObject()
        jsonObject.addProperty(Constants.objectIdKey, productRegisterViewModel.productId)
        jsonObject.addProperty(Constants.base64BodyKey, productRegisterViewModel.imageUri)
        jsonObject.addProperty(Constants.mimeTypeKey, Constants.mimeType)

        client.updatePhoto(jsonObject)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { _ ->
                    run {
                        viewState.hideLoading()
                        router.exit()
                    }
                },
                {
                        error ->
                    run {
                        viewState.hideLoading()
                        viewState.showError(error)
                    }
                }
            )
    }


}

