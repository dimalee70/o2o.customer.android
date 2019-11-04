package dragau.o2o.customer.presentation.presenter.product

import android.annotation.SuppressLint
import android.util.Log
import androidx.databinding.ObservableArrayList
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import dragau.o2o.customer.App
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
class LookupPresenter(private val parentLookupId: String, private var router: Router, var productRegisterViewModel: ProductRegisterViewModel,
                      private var prevLookupIdList: ArrayList<String>) : MvpPresenter<LookupView>() {

    @Inject
    lateinit var client: ApiManager

    init {
        App.appComponent.inject(this)
    }

    @Inject
    lateinit var lookupDao: LookupDao

    var lookups: ObservableArrayList<Lookup> = ObservableArrayList<Lookup>()
    private var disposable: Disposable? = null

    var isSelected: Boolean = false
    var parameter: BaseParameter? = null

    @SuppressLint("CheckResult")
    fun getLookups()
    {
        lookups.clear()

        //removeLookup(parentLookupId)
        parameter = productRegisterViewModel.parameters?.firstOrNull { it.value == parentLookupId }
        if (parameter?.rootId == null)
        {
            parameter?.rootId = parameter?.value?.toString()
        }

        if(parameter?.rootId == parameter?.value || prevLookupIdList.isNotEmpty()) {
            disposable = lookupDao.getChildren(parentLookupId)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { resList ->
                        lookups.addAll(resList)
                        /*resList.map {  lookup ->
                        lookups.add(BaseParameter(parentLookupId, ParameterType.LIST, lookup.value, lookup.lookupId)) }*/
                    },
                    { error ->
                        Timber.e(error)
                    }
                )
        }
        else
        {
            disposable = lookupDao.getParentId(parentLookupId)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { result ->
                        lookupDao.getChildren(result)
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
                    },
                    { error ->
                        Timber.e(error)
                    }
                )

        }


       /* DataHolder.lookups?.filter { it.parentLookupId == id }?.map { lookup ->
            lookups.add(BaseParameter(parentLookupId, ParameterType.LIST, lookup.value, lookup.lookupId))
        }*/
    }

    private fun removeLookup(parentId: String)
    {
        val lookup = productRegisterViewModel.parameters?.firstOrNull { it.id == parentId }
        if(lookup != null)
        {
            removeLookup(lookup.value.toString())
        }
        productRegisterViewModel.parameters?.removeIf { it.id == parentId }
    }
/*
    private fun removeLookupInverted(parentId: String): String?
    {
        val lookup = productRegisterViewModel.parameters?.firstOrNull { it.selectedId == parentId }
        var id: String? = null
        if (lookup?.isRoot == true)
        {
            id = lookup.id
        }

        if(lookup != null)
        {
            val temp = removeLookupInverted(lookup.value.toString())
            if (temp != null) {
                id = temp
            }
        }

        productRegisterViewModel.parameters?.remove(lookup)
        return id
    }

    fun removeParameterIfAdded()
    {
        productRegisterViewModel.parameters!!.removeIf { it.id == parentLookupId && it.selectedId == null}
    }
*/
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
        //parameter?.selectedId = lookup.lookupId

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
                        router.backTo(Screens.ProductRegisterScreen())
                    }

                    /*val parent = productRegisterViewModel.parameters?.firstOrNull { it.value == parentLookupId }
                    if (parent != null) {
                        parent.title = parameter.name
                        parent.selectedId = parameter.value.toString()
                    }

                    if (count > 0)
                    {
                        var idx: Int = productRegisterViewModel.parameters!!.toMutableList().indexOfFirst{
                            it.id.equals(parentLookupId)
                        }
                        if(idx >= 0 ){
                            productRegisterViewModel.parameters?.set(idx, parameter)
                        }
//                        println(parameter.id)
//                        println(parentLookupId)
                        //productRegisterViewModel.parameters!!.add(productRegisterViewModel.parameters!!.size-1, parameter)
                        prevLookupIdList.add(parameter.id!!)
                        router.navigateTo(Screens.LookupScreen(parameter.value.toString(), prevLookupIdList))
                    }
                    else
                    {
                        val rootId = removeLookupInverted(parameter.id!!)
                        parent?.id = rootId
                        router.backTo(Screens.ProductRegisterScreen())
                    }*/
                },
                { error ->
                    Timber.e(error)
                }
            )
        /*if (DataHolder.lookups?.count { it.parentLookupId == parameter.value.toString() } != 0)
        {
            productRegisterViewModel.parameters!!.add(productRegisterViewModel.parameters!!.size - 1, parameter)
        }*/


    }

    fun onBackPressed()
    {

        if (parameter!!.value != parameter?.rootId) {
            disposable = lookupDao.getParentId(parameter!!.value.toString())
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { result ->
                        if (prevLookupIdList.isNotEmpty()) {
                            prevLookupIdList.removeAt(prevLookupIdList.size - 1)
                        }
                        router.replaceScreen(Screens.LookupScreen(result, prevLookupIdList))
                    },
                    { error ->
                        Timber.e(error)
                    }
                )
        }
        else
        {
            router.backTo(Screens.ProductRegisterScreen())
        }
    }
}
