package dragau.o2o.customer.ui.fragment.product

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import dragau.o2o.customer.R
import dragau.o2o.customer.presentation.view.product.AddParameterView
import dragau.o2o.customer.presentation.presenter.product.AddParameterPresenter

import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import dragau.o2o.customer.App
import dragau.o2o.customer.BR
import dragau.o2o.customer.api.requests.ProductRegisterViewModel
import dragau.o2o.customer.databinding.FragmentAddParameterBinding
import dragau.o2o.customer.models.objects.BaseParameter
import dragau.o2o.customer.ui.adapters.RecyclerBindingAdapter
import dragau.o2o.customer.ui.fragment.BaseMvpFragment
import ru.terrakok.cicerone.Router
import javax.inject.Inject

class AddParameterFragment : BaseMvpFragment(), AddParameterView, RecyclerBindingAdapter.OnItemClickListener<BaseParameter> {
    companion object {
        const val TAG = "AddParameterFragment"

        fun newInstance(): AddParameterFragment {
            val fragment: AddParameterFragment = AddParameterFragment()
            val args: Bundle = Bundle()
            fragment.arguments = args
            return fragment
        }
    }

    @Inject
    lateinit var router: Router

    @Inject
    lateinit var productRegisterViewModel: ProductRegisterViewModel

    @InjectPresenter
    lateinit var mAddParameterPresenter: AddParameterPresenter

    @ProvidePresenter
    fun providePresenter(): AddParameterPresenter{
        return AddParameterPresenter(router, productRegisterViewModel)
    }


    lateinit var binding: FragmentAddParameterBinding

    lateinit var recyclerBindingAdapter: RecyclerBindingAdapter<BaseParameter>
    private var onItemClickListenerRecycler: RecyclerBindingAdapter.OnItemClickListener<BaseParameter>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        App.appComponent.inject(this)
        super.onCreate(savedInstanceState)
        recyclerBindingAdapter = RecyclerBindingAdapter(R.layout.add_parameter_item, BR.data, context!!)

        if (onItemClickListenerRecycler != null) {
            recyclerBindingAdapter.setOnItemClickListener(onItemClickListenerRecycler!!)
        }

        recyclerBindingAdapter.setItems(mAddParameterPresenter.parameters)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_parameter, container, false)
        binding.productRegisterViewModel = productRegisterViewModel
        binding.presenter = mAddParameterPresenter
        mAddParameterPresenter.getParameters()

        binding.recyclerview.layoutManager = androidx.recyclerview.widget.GridLayoutManager(
            context,
            3
        )
        binding.recyclerview.adapter = recyclerBindingAdapter

        return binding.root
    }

    override fun onItemClick(position: Int, item: BaseParameter) {
        mAddParameterPresenter.addParameterToProduct(item)
    }

    override fun onDetach() {
        super.onDetach()
        onItemClickListenerRecycler = null
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            onItemClickListenerRecycler = this
        } catch (e: Throwable) {
            throw ClassCastException(context.toString())
        }

    }
}
