package dragau.o2o.customer.ui.fragment.product

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.Observer
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import dragau.o2o.customer.App
import dragau.o2o.customer.Constants
import dragau.o2o.customer.R
import dragau.o2o.customer.api.requests.ProductRegisterViewModel
import dragau.o2o.customer.api.response.BaseResponse
import dragau.o2o.customer.api.response.ProductResponce
import dragau.o2o.customer.databinding.FragmentProductShowBinding
import dragau.o2o.customer.presentation.presenter.product.ProductShowPresenter
import dragau.o2o.customer.presentation.view.product.ProductShowView
import dragau.o2o.customer.ui.fragment.BaseMvpFragment
import kotlinx.android.synthetic.main.activity_home.*
import ru.terrakok.cicerone.Router
import javax.inject.Inject

class ProductShowFragment: BaseMvpFragment(), ProductShowView{
    companion object{
        const val TAG = "ProductShowFragment"

        fun newInstance(productId: String?, productName: String?): ProductShowFragment{
            val fragment: ProductShowFragment = ProductShowFragment()
            val args: Bundle = Bundle()
            args.putString(Constants.PRODUCT_ID, productId)
            args.putString(Constants.PRODUCT_NAME, productName)
            fragment.arguments = args
            return fragment
        }
    }

    private var productId: String? = null

    private var productName: String? = null
     fun getAnimationDestionationId(): Int {
        return arguments!!.getInt(Constants.ARG_ANIM_DESTINATION)
    }

    fun setAnimationDestinationId(resId: Int) {
        if (arguments == null) arguments = Bundle()
        arguments!!.putInt(Constants.ARG_ANIM_DESTINATION, resId)
    }


    @Inject
    lateinit var router: Router

    @InjectPresenter
    lateinit var mProductShowPresenter: ProductShowPresenter

    lateinit var binding: FragmentProductShowBinding

    private val lifecycleRegistry = LifecycleRegistry(this)

    private var data = ProductRegisterViewModel()

    @ProvidePresenter
    fun providePressenter(): ProductShowPresenter{
        return ProductShowPresenter(router)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        App.appComponent.inject(this)
        super.onCreate(savedInstanceState)
        productId = arguments?.getString(Constants.PRODUCT_ID)
        productName = arguments?.getString(Constants.PRODUCT_NAME)
        mProductShowPresenter.attachLifecycle(lifecycleRegistry)
        mProductShowPresenter.getProductById(productId)
        mProductShowPresenter.observeProductResponseBoundary()
            .observe(this, Observer {
                responce -> responce.let {
                showProduct(responce)
            }
            })
    }

    fun showProduct(response: ProductResponce){
        data.title = response.resultObject!!.name
        data.imageUri = if(response.resultObject.productThumbnails == null || response.resultObject.productThumbnails.isNullOrEmpty()) null else response.resultObject.productThumbnails!!.peek().body
        data.describe = response.resultObject.description

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_product_show, container, false)
        binding.presenter = mProductShowPresenter
        binding.pageTv.text = productName?.toUpperCase()
//        binding.backButtonIb.setOnClickListener{
//            router.exit()
//        }
        activity?.appbar_layout?.visibility = View.GONE
        activity?.pageTv?.visibility = View.GONE
//        activity?.actionBar?.setHomeAsUpIndicator(R.drawable.ic_left_arrow)
        binding.data = data
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
    }
}