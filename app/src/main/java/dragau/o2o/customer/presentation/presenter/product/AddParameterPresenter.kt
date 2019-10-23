package dragau.o2o.customer.presentation.presenter.product

import android.annotation.SuppressLint
import androidx.databinding.ObservableArrayList
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import dragau.o2o.customer.App
import dragau.o2o.customer.api.ApiManager
import dragau.o2o.customer.api.requests.ProductRegisterViewModel
import dragau.o2o.customer.models.enums.ParameterType
import dragau.o2o.customer.models.objects.BaseParameter
import dragau.o2o.customer.presentation.view.product.AddParameterView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import ru.terrakok.cicerone.Router
import javax.inject.Inject

@InjectViewState
class AddParameterPresenter(private var router: Router, var productRegisterViewModel: ProductRegisterViewModel) : MvpPresenter<AddParameterView>() {

    @Inject
    lateinit var client: ApiManager

    init {
        App.appComponent.inject(this)
    }

    var parameters: ObservableArrayList<BaseParameter> = ObservableArrayList<BaseParameter>()
    private var disposable: Disposable? = null

   /* fun getParameters(){
        parameters.clear()
        var mainList = arrayListOf(
            BaseParameter("1", ParameterType.STRING, "Категория", "" ),
            BaseParameter("2", ParameterType.INT, "Рекомендуемая цена", null, "тг." ),
            BaseParameter("3", ParameterType.DECIMAL, "Вес", null, "кг." ),
            BaseParameter("4", ParameterType.INT, "Срок годности", null, "дн." ),
            BaseParameter("6", ParameterType.STRING, "Описание", "" ),
            BaseParameter("7", ParameterType.STRING, "Состав", "" )
        ).toMutableList()

        mainList.forEach {param ->
            if (productRegisterViewModel.parameters?.count { it.id == param.id } == 0) {
                parameters.add(param)
            }
        }
    }*/

    @SuppressLint("CheckResult")
    fun getParameters(){
       parameters.clear()
        disposable = client.getParameters()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { result ->
                    run {
                        result.resultObject?.forEach { param ->
                            if (productRegisterViewModel.parameters?.count { it.id == param.id } == 0) {
                                parameters.add(param)
                            }
                        }
                    }
                },
                { error ->
                    run {
                        viewState.showError(error)
                    }
                }
            )
    }

    fun addParameterToProduct(parameter: BaseParameter)
    {
        productRegisterViewModel.parameters!!.add(productRegisterViewModel.parameters!!.size - 1 ,parameter)
        router.exit()
    }
}