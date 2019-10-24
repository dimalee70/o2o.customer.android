package dragau.o2o.customer.ui.fragment.product

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.Observer
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import com.tiper.MaterialSpinner
import dragau.o2o.customer.App
import dragau.o2o.customer.BR
import dragau.o2o.customer.Constants
import dragau.o2o.customer.R
import dragau.o2o.customer.api.requests.ProductRegisterViewModel
import dragau.o2o.customer.api.response.ProductCategoriesResponce
import dragau.o2o.customer.databinding.FragmentProductRegisterBinding
import dragau.o2o.customer.models.enums.ParameterType
import dragau.o2o.customer.models.objects.BaseParameter
import dragau.o2o.customer.models.objects.ProductCategories
import dragau.o2o.customer.presentation.presenter.product.ProductRegisterPresenter
import dragau.o2o.customer.presentation.view.product.ProductRegisterView
import dragau.o2o.customer.ui.adapters.RecyclerBindingAdapter
import dragau.o2o.customer.ui.fragment.BaseMvpFragment
import kotlinx.android.synthetic.main.activity_product.*
import ru.terrakok.cicerone.Router
import timber.log.Timber
import javax.inject.Inject

class ProductRegisterFragment : BaseMvpFragment(), ProductRegisterView, RecyclerBindingAdapter.OnItemClickListener<BaseParameter> {

    companion object {
        const val TAG = "ProductRegisterFragment"

        fun newInstance(): ProductRegisterFragment {
            val fragment: ProductRegisterFragment = ProductRegisterFragment()
            val args: Bundle = Bundle()
            fragment.arguments = args
            return fragment
        }


    }

    @Inject
    lateinit var router: Router

    @InjectPresenter
    lateinit var mProductRegisterPresenter: ProductRegisterPresenter

    @Inject
    lateinit var productRegisterViewModel: ProductRegisterViewModel

    lateinit var binding: FragmentProductRegisterBinding

    lateinit var recyclerBindingAdapter: RecyclerBindingAdapter<BaseParameter>
    private var onItemClickListenerRecycler: RecyclerBindingAdapter.OnItemClickListener<BaseParameter>? = null

    private val lifecycleRegistry = LifecycleRegistry(this)

    @ProvidePresenter
    fun providePresenter(): ProductRegisterPresenter{
        return ProductRegisterPresenter(router, productRegisterViewModel)
    }

    private val listener by lazy {
        object : MaterialSpinner.OnItemSelectedListener {
            @SuppressLint("TimberArgCount")
            override fun onItemSelected(parent: MaterialSpinner, view: View?, position: Int, id: Long) {
                Timber.d("MaterialSpinner", "onItemSelected parent=${parent.id}, position=$position")
                parent.focusSearch(View.FOCUS_UP)?.requestFocus()
            }

            @SuppressLint("TimberArgCount")
            override fun onNothingSelected(parent: MaterialSpinner) {
                Timber.d("MaterialSpinner", "onNothingSelected parent=${parent.id}")
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        App.appComponent.inject(this)
        super.onCreate(savedInstanceState)
        recyclerBindingAdapter = RecyclerBindingAdapter(R.layout.product_parameter_item, BR.data, context!!)

        if (onItemClickListenerRecycler != null) {
            recyclerBindingAdapter.setOnItemClickListener(onItemClickListenerRecycler!!)
        }

        mProductRegisterPresenter.attachLifecycle(lifecycleRegistry)
        mProductRegisterPresenter.observeForProductCategoriesResponseBoundary()
            .observe(this, Observer {
                response -> response.let {
                    setProductCategories(response)
            }
            })

        recyclerBindingAdapter.setItems(productRegisterViewModel.parameters)
    }

    private fun setProductCategories(response: ProductCategoriesResponce){

        val productCategories = response.resultObject
        /*ArrayAdapter<ProductCategories>(context!!, android.R.layout.simple_list_item_1, productCategories!!)
            .let {
                it.setDropDownViewResource(R.layout.item_spinner_simple)
                binding.productCategoryMs.apply {
                    adapter = it
                    onItemSelectedListener = listener
                }
            }

        binding.productCategoryMs.selection = productCategories.indexOfFirst { it.productCategoryId ==  productRegisterViewModel.categoryName }*/

    }

    private fun MaterialSpinner.onClick() {
        error = if (error.isNullOrEmpty()) resources.getText(R.string.error) else null
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
//        pageTv.let { it.setText(R.string.titleRegisterProduct) }
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_product_register, container, false)
//        val frVew = binding.flMain
        binding.productRegisterViewModel = productRegisterViewModel
        binding.presenter = mProductRegisterPresenter
       //mProductRegisterPresenter.getProductCategoris()

        binding.recyclerview.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(
            context,
            androidx.recyclerview.widget.LinearLayoutManager.VERTICAL,
            false
        )
        binding.recyclerview.adapter = recyclerBindingAdapter

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        pageTv.let { it.setText(R.string.titleRegisterProduct) }
        activity!!.pageTv.setText(R.string.titleRegisterProduct)


    }

    override fun onResume() {
        super.onResume()
    }

    override fun showPictureDialog() {
        this.activity?.let {
            CropImage.activity(null)
//                .setMaxCropResultSize(1920,1080)
//                .setMinCropResultSize(1920, 100.toPx())
//                .setAspectRatio(3,1)
//                .setRequestedSize(150,50, CropImageView.RequestSizeOptions.RESIZE_EXACT)
                .setGuidelines(CropImageView.Guidelines.ON)
                .start(it)
        }
    }

    override fun onItemClick(position: Int, item: BaseParameter) {
        if (item.type == ParameterType.LIST)
        {
            mProductRegisterPresenter.showLookup(item)
        }
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
