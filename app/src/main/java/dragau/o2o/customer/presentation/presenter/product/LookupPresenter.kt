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
import dragau.o2o.customer.models.objects.Lookup
import dragau.o2o.customer.models.shared.DataHolder
import dragau.o2o.customer.presentation.view.product.LookupView
import io.reactivex.disposables.Disposable
import ru.terrakok.cicerone.Router
import javax.inject.Inject

@InjectViewState
class LookupPresenter(private val parentLookupId: String, private var router: Router, var productRegisterViewModel: ProductRegisterViewModel) : MvpPresenter<LookupView>() {

    @Inject
    lateinit var client: ApiManager

    init {
        App.appComponent.inject(this)
    }

    var lookups: ObservableArrayList<BaseParameter> = ObservableArrayList<BaseParameter>()
    private var disposable: Disposable? = null

    @SuppressLint("CheckResult")
    fun getLookups(id: String)
    {
        lookups?.clear()

        removeLookup(parentLookupId)

        DataHolder.lookups?.filter { it.parentLookupId == id }?.map { lookup ->
            lookups.add(BaseParameter(parentLookupId, ParameterType.LIST, lookup.value, lookup.lookupId))
        }
    }

    private fun removeLookup(parentId: String)
    {
        val lookup = productRegisterViewModel.parameters?.firstOrNull { it.id == parentId }
        if(lookup != null)
        {
            removeLookup(lookup.value.toString()!!)
        }
        productRegisterViewModel.parameters?.removeIf { it.id == parentId }
    }

    fun selectLookup(parameter: BaseParameter)
    {
        if (DataHolder.lookups?.count { it.parentLookupId == parameter.value.toString() } != 0)
        {
            productRegisterViewModel.parameters!!.add(productRegisterViewModel.parameters!!.size - 1, parameter)
        }

        val parent = productRegisterViewModel.parameters?.firstOrNull { it.value == parentLookupId }
        if (parent != null) {
            parent.title = parameter.name
            parent.selectedId = parameter.value.toString()
        }

        router.exit()
    }
}
