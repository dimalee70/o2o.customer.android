package dragau.o2o.customer.presentation.presenter.product

import android.annotation.SuppressLint
import androidx.databinding.ObservableArrayList
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import dragau.o2o.customer.App
import dragau.o2o.customer.api.ApiManager
import dragau.o2o.customer.api.requests.ProductRegisterViewModel
import dragau.o2o.customer.models.db.ParameterDao
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

    @Inject
    lateinit var parametersDao: ParameterDao

    init {
        App.appComponent.inject(this)
    }

    var parameters: ObservableArrayList<BaseParameter> = ObservableArrayList<BaseParameter>()
    private var disposable: Disposable? = null

    @SuppressLint("CheckResult")
    fun getParameters(){
//       parameters.clear()
        disposable = parametersDao.getAllActive()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { result ->
                    run {
                        result.forEach { param ->
                            if (productRegisterViewModel.parameters?.count { it.id == param.id } == 0) {
                                parameters.add(BaseParameter(param.id, param.type, param.name, param.value, param.uom))
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
        if (parameter.type == ParameterType.LIST)
        {
            parameter.isRoot = true
        }
        productRegisterViewModel.parameters!!.add(productRegisterViewModel.parameters!!.size - 1 ,parameter)
        router.exit()
    }


}
