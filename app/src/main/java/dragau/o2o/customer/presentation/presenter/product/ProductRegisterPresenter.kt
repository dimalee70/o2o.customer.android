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
import dragau.o2o.customer.api.ApiManager
import dragau.o2o.customer.api.requests.ProductRegisterViewModel
import dragau.o2o.customer.api.response.ProductCategoriesResponce
import dragau.o2o.customer.models.enums.ParameterType
import dragau.o2o.customer.models.objects.BaseParameter
import dragau.o2o.customer.models.objects.ProductBarcode
import dragau.o2o.customer.presentation.presenter.BasePresenter
import dragau.o2o.customer.presentation.view.product.ProductRegisterView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import org.joda.time.DateTime
import ru.terrakok.cicerone.Router
import timber.log.Timber
import javax.inject.Inject

@InjectViewState
class ProductRegisterPresenter(private var router: Router, var productRegisterViewModel: ProductRegisterViewModel): BasePresenter<ProductRegisterView>() {

    @Inject
    lateinit var client: ApiManager

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    var liveProductCategoriesResponse = MutableLiveData<ProductCategoriesResponce>()

    init {
        App.appComponent.inject(this)



        productRegisterViewModel.parameters = ObservableArrayList<BaseParameter>()
        productRegisterViewModel.parameters!!.addAll(arrayListOf(
            BaseParameter("2", ParameterType.STRING, "Название", "" ),
            BaseParameter("1", ParameterType.STRING, "Категория", "" ),
            BaseParameter("2", ParameterType.STRING, "Производитель", "" ),
            BaseParameter("4", ParameterType.INT, "Рекомендуемая цена", 0 ),
            BaseParameter("2", ParameterType.BARCODE, "Штрих-код", productRegisterViewModel.productBarcode),
            BaseParameter("3", ParameterType.STRING, "Описание", "" )
        ))
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
        val body: JsonObject = JsonObject()
        body.addProperty("productId", productRegisterViewModel.productId)
        body.addProperty("productCategoryId", productRegisterViewModel.categoryName)
        body.addProperty("name", productRegisterViewModel.title)
        body.addProperty("barCode", productRegisterViewModel.barCode)
        body.addProperty("manufacturer", productRegisterViewModel.produserName)
        body.addProperty("description", productRegisterViewModel.describe)

        client.createProduct(body)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { result ->
                    run {
                        productRegisterViewModel.productId = result.resultObject
//                        if (productRegisterViewModel.imageUri != null)
                            uploadPhoto()
//                        viewState.hideLoading()
//                        router.exit()
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

    @SuppressLint("CheckResult")
    private fun updateProduct(){
        val body: JsonObject = JsonObject()
        body.addProperty("productCategoryId", productRegisterViewModel.categoryName)
//            println("categoryId")
//            println(productRegisterViewModel.categoryName)
//            body.addProperty("productImageBase64", productRegisterViewModel.imageUri)
        body.addProperty("name", productRegisterViewModel.title)
        body.addProperty("barCode", productRegisterViewModel.barCode)
        body.addProperty("manufacturer", productRegisterViewModel.produserName)
        body.addProperty("description", productRegisterViewModel.describe)

        client.updateProduct(body)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { result ->
                    run {
                        updatePhoto()
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
    private fun uploadPhoto(){
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
                        router.exit()
                    }
                },
                {
                    error ->
                    run {
                        viewState.showError(error)
                }
                }
            )
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
                {
                        result ->
                    run {
                        viewState.hideLoading()
                        router.exit()
                    }
                },
                {
                        error ->
                    run {
                        viewState.showError(error)
                    }
                }
            )
    }


}

