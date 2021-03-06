package dragau.o2o.customer.ui.fragment.product

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import dragau.o2o.customer.R
import dragau.o2o.customer.presentation.view.product.LookupView
import dragau.o2o.customer.presentation.presenter.product.LookupPresenter

import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import dragau.o2o.customer.App
import dragau.o2o.customer.BR
import dragau.o2o.customer.Constants
import dragau.o2o.customer.api.requests.ProductRegisterViewModel
import dragau.o2o.customer.databinding.FragmentLookupBinding
import dragau.o2o.customer.models.objects.BaseParameter
import dragau.o2o.customer.models.objects.Lookup
import dragau.o2o.customer.ui.adapters.RecyclerBindingAdapter
import dragau.o2o.customer.ui.common.BackButtonListener
import dragau.o2o.customer.ui.fragment.BaseMvpFragment
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.activity_product.*
import kotlinx.android.synthetic.main.activity_product.pageTv
import kotlinx.android.synthetic.main.fragment_product_show.view.*
import ru.terrakok.cicerone.Router
import javax.inject.Inject

class LookupFragment : BaseMvpFragment(), LookupView, RecyclerBindingAdapter.OnItemClickListener<Lookup>,
    BackButtonListener {
    companion object {
        const val TAG = "LookupFragment"

        fun newInstance(parentId: String, prevLookupIdList: ArrayList<String>): LookupFragment {
            val fragment: LookupFragment = LookupFragment()
            val args: Bundle = Bundle()
            args.putString(Constants.EXTRA_LOOKUP_ID, parentId)
            args.putStringArrayList(Constants.EXTRA_LOOKUP_PREV_NAMES, prevLookupIdList)
            fragment.arguments = args
            return fragment
        }
    }

    @Inject
    lateinit var router: Router

    @Inject
    lateinit var productRegisterViewModel: ProductRegisterViewModel

    @InjectPresenter
    lateinit var mLookupPresenter: LookupPresenter

    @ProvidePresenter
    fun providePresenter(): LookupPresenter{
        return LookupPresenter(parentId!!, router, productRegisterViewModel, prevLookupIdList)
    }

    var parentId: String? = null
    lateinit var prevLookupIdList: ArrayList<String>

    lateinit var binding: FragmentLookupBinding

    lateinit var recyclerBindingAdapter: RecyclerBindingAdapter<Lookup>
    private var onItemClickListenerRecycler: RecyclerBindingAdapter.OnItemClickListener<Lookup>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        this.parentId = arguments!!.getString(Constants.EXTRA_LOOKUP_ID, "")
        this.prevLookupIdList = arguments!!.getStringArrayList(Constants.EXTRA_LOOKUP_PREV_NAMES)!!
        App.appComponent.inject(this)
        super.onCreate(savedInstanceState)
        recyclerBindingAdapter = RecyclerBindingAdapter(R.layout.add_lookup_item, BR.data, context!!)

        if (onItemClickListenerRecycler != null) {
            recyclerBindingAdapter.setOnItemClickListener(onItemClickListenerRecycler!!)
        }

        mLookupPresenter.getLookups()
        recyclerBindingAdapter.setItems(mLookupPresenter.lookups)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_lookup, container, false)
        binding.productRegisterViewModel = productRegisterViewModel
        binding.presenter = mLookupPresenter

            /* binding.recyclerview.layoutManager = androidx.recyclerview.widget.GridLayoutManager(
            context,
            3
        )*/
        binding.recyclerview.adapter = recyclerBindingAdapter

        if (!prevLookupIdList.isNullOrEmpty())
            activity!!.pageTv.text = prevLookupIdList.last()

        mLookupPresenter.getTitle()
        return binding.root
    }



    override fun onItemClick(position: Int, item: Lookup) {
        mLookupPresenter.selectLookup(item)
    }

    override fun onDetach() {
        super.onDetach()
        onItemClickListenerRecycler = null
        //mLookupPresenter.removeParameterIfAdded()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            onItemClickListenerRecycler = this
        } catch (e: Throwable) {
            throw ClassCastException(context.toString())
        }
    }

    override fun setTitle(value: String) {
        activity!!.pageTv.text = value
    }

    override fun onBackPressed(): Boolean {
        return mLookupPresenter.onBackPressed()
    }
}
