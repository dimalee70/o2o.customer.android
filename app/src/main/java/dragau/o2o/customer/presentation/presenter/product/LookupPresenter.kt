package dragau.o2o.customer.presentation.presenter.product

import android.annotation.SuppressLint
import android.util.Log
import androidx.databinding.ObservableArrayList
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import dragau.o2o.customer.App
import dragau.o2o.customer.BR
import dragau.o2o.customer.Screens
import dragau.o2o.customer.api.ApiManager
import dragau.o2o.customer.api.requests.ProductRegisterViewModel
import dragau.o2o.customer.models.db.LookupDao
import dragau.o2o.customer.models.enums.ParameterType
import dragau.o2o.customer.models.objects.BaseParameter
import dragau.o2o.customer.models.objects.Lookup
import dragau.o2o.customer.models.shared.DataHolder
import dragau.o2o.customer.presentation.view.product.LookupView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import ru.terrakok.cicerone.Router
import timber.log.Timber
import javax.inject.Inject

@InjectViewState
class LookupPresenter(private var parentLookupId: String, private var router: Router, var productRegisterViewModel: ProductRegisterViewModel,
                      private var prevLookupIdList: ArrayList<String>) : MvpPresenter<LookupView>() {

    @Inject
    lateinit var client: ApiManager

    init {
        App.appComponent.inject(this)
    }

    @Inject
    lateinit var lookupDao: LookupDao

    var lookups: ObservableArrayList<Lookup> = ObservableArrayList()
    private var disposable: Disposable? = null

    var parameter: BaseParameter? = null

    @SuppressLint("CheckResult")
    fun getLookups()
    {
        lookups.clear()

        parameter = productRegisterViewModel.parameters?.firstOrNull { it.value == parentLookupId }

        if (prevLookupIdList.isEmpty() && parameter?.value != parameter?.rootId)
        {
            parentLookupId = lookupDao.getParentId(parentLookupId)
        }

        disposable = lookupDao.getChildren(parentLookupId)
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { resList ->
                    lookups.addAll(resList)
                },
                { error ->
                    Timber.e(error)
                }
            )
    }

    fun getTitle()
    {
        if (!prevLookupIdList.isNullOrEmpty())
        {
            viewState.setTitle(lookupDao.getValue(parentLookupId))
        }
    }

    fun selectLookup(lookup: Lookup)
    {
        parameter?.value = lookup.lookupId
        parameter?.title = lookup.value

        disposable = lookupDao.getChildrenCount(lookup.lookupId)
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe (
                { count ->

                    if (count > 0)
                    {
                        prevLookupIdList.add(lookup.parentLookupId!!)
                        router.replaceScreen(Screens.LookupScreen(lookup.lookupId, prevLookupIdList))
                    }
                    else
                    {
                        parameter?.notifyPropertyChanged(BR.data)
                        router.backTo(Screens.ProductRegisterScreen())
                    }
                },
                { error ->
                    Timber.e(error)
                }
            )
    }

    fun onBackPressed(): Boolean
    {
        if (parameter?.value != parameter?.rootId) {
            val p1 = lookupDao.getParentId(parameter!!.value.toString())
            if (prevLookupIdList.isNotEmpty()) {
                prevLookupIdList.remove(prevLookupIdList.last())
            }
            parameter?.value = p1
            parameter?.title = null
            router.replaceScreen(Screens.LookupScreen(p1, prevLookupIdList))
            return false
        }
        else
        {
            return true
        }
    }
}
