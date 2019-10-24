package dragau.o2o.customer.ui.fragment.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.databinding.ObservableArrayList
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.Observer
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import dragau.o2o.customer.App
import dragau.o2o.customer.BR
import dragau.o2o.customer.R
import dragau.o2o.customer.Screens
import dragau.o2o.customer.api.response.ProductResponce
import dragau.o2o.customer.api.response.ProductResponceContact
import dragau.o2o.customer.databinding.FragmentHomeMainBinding
import dragau.o2o.customer.models.objects.Product
import dragau.o2o.customer.models.shared.DataHolder
import dragau.o2o.customer.presentation.presenter.home.HomeMainPresenter
import dragau.o2o.customer.presentation.view.home.HomeMainView
import dragau.o2o.customer.ui.activity.product.ScanActivity
import dragau.o2o.customer.ui.adapters.RecyclerBindingAdapter
import dragau.o2o.customer.ui.fragment.BaseMvpFragment
import kotlinx.android.synthetic.main.activity_home.*
import ru.terrakok.cicerone.Router
import java.lang.ClassCastException
import javax.inject.Inject
import kotlin.collections.ArrayList

class HomeMainFragment : BaseMvpFragment(), HomeMainView,
    RecyclerBindingAdapter.OnItemClickListener<Product>
//    , OnItemClickListener<OrdersByOutletResult>
{
    override fun onItemClick(position: Int, item: Product) {

        this.position = position
        mHomeMainPresenter.openProductShow(item.productId, item.name)

//        Toast.makeText(context!!, item.name, Toast.LENGTH_SHORT).show()
    }

    var position: Int? = null


    //    private var onItemClickListenerRecycler
    companion object {
        const val TAG = "HomeMainFragment"

        fun newInstance(): HomeMainFragment {
            val fragment: HomeMainFragment = HomeMainFragment()
            val args: Bundle = Bundle()
            fragment.arguments = args
            return fragment
        }
    }

    var products: ObservableArrayList<Product> = ObservableArrayList()

//    override fun onItemClick(position: Int, item: OrdersByOutletResult) {
//        Toast.makeText(context!!, item.contactId, Toast.LENGTH_SHORT).show()
//    }

//    @Inject
//    lateinit var customs: ObservableArrayList<OrdersByOutletResult>

    @Inject
    lateinit var router: Router

    @InjectPresenter
    lateinit var mHomeMainPresenter: HomeMainPresenter

    @ProvidePresenter
    fun providePresenter(): HomeMainPresenter{
        return HomeMainPresenter(router)
    }

    lateinit var binding: FragmentHomeMainBinding

    lateinit var recyclerProductsAdapter: RecyclerBindingAdapter<Product>

    private val lifecycleRegistry = LifecycleRegistry(this)
    private var onCustomClickListenerRecycler: RecyclerBindingAdapter.OnItemClickListener<Product>? = this

    override fun onCreate(savedInstanceState: Bundle?) {
        App.appComponent.inject(this)
        super.onCreate(savedInstanceState)
        mHomeMainPresenter.attachLifecycle(lifecycleRegistry)
        mHomeMainPresenter.observeProductResponseBoundary()
            .observe(this, Observer {
                response -> response.let {
                setProductsByContact(response)
            }
            })

        println("OnStart")
        recyclerProductsAdapter = RecyclerBindingAdapter(R.layout.item_product, BR.data, context!!)
        if(onCustomClickListenerRecycler != null){
            recyclerProductsAdapter.setOnItemClickListener(onCustomClickListenerRecycler!!)
        }

        mHomeMainPresenter.getCategories()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home_main, container, false)
        binding.presenter = mHomeMainPresenter

        activity?.appbar_layout?.visibility = View.VISIBLE
        activity?.pageTv?.visibility = View.VISIBLE


//
//        val typesList = ArrayList<Types>()
//        typesList.add(Types("Акции", "#FF7058", "https://img.icons8.com/carbon-copy/2x/instagram-new.png"))
//        typesList.add(Types("Поставщики", "#FFB980", "https://img.icons8.com/carbon-copy/2x/instagram-new.png"))
//        typesList.add(Types("Сервис обслуживания", "#7985EB", "https://img.icons8.com/carbon-copy/2x/instagram-new.png"))
//        typesList.add(Types("Заказы", "#2CC245", "https://img.icons8.com/carbon-copy/2x/instagram-new.png"))
//        typesList.add(Types("Добавить торговую точку", "#FF7058", "https://img.icons8.com/carbon-copy/2x/instagram-new.png"))
//        typesList.add(Types("Доставки", "#4B5BE6", "https://img.icons8.com/carbon-copy/2x/instagram-new.png"))
//        обслуживания
//        торговую точку

//        val customList = ArrayList<Customs>()
//        customList.add(Customs("https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcSUcp-vFwbcy-8m14jgD4p947IV_1TqO--f87Y54u-JvyiiDh1k",
//            "Константин",
//            "Аль фараби, к45Б 8 этаж/105",
//            5500))
//        customList.add(Customs("https://media.vanityfair.com/photos/58c2f5aa0a144505fae9e9ee/master/pass/avatar-sequels-delayed.jpg",
//            "Дмитрий",
//            "Аль фараби, к47Б",
//            10_000))
//        customList.add(Customs("https://cdn6.f-cdn.com/contestentries/918774/22954115/586eea98be949_thumb900.jpg",
//            "Бородач",
//            "Масанчи, к45Б 8 этаж/105",
//            1_000_000_500))
//        customList.add(Customs("https://cdn0.iconfinder.com/data/icons/iconshock_guys/512/andrew.png",
//            "Волосач",
//            "Аль фараби, к4Б 8 этаж/105, подъезд номер 5, ключи под поласом возле входной двери",
//            5500))
//        customList.add(Customs("Hello2"))
//        customList.add(Customs("Hello3"))
//        customList.add(Customs("Hello4"))
//        customList.add(Customs("Hello5"))
//        customList.add(Customs("Hello6"))
//        types.addAll(typesList)
//        customs.addAll(customList)
//        recyclerTypesAdapter.setItems(types)

//        binding.typesRv.adapter = recyclerTypesAdapter
//        recyclerProductsAdapter.setHasStableIds(true)
        binding.productsRv.adapter = recyclerProductsAdapter
//        binding.productsRv.setHasFixedSize(true)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            onCustomClickListenerRecycler = this
        }catch (e: Throwable){
            throw ClassCastException(context.toString())
        }
    }

    override fun onDetach() {
        super.onDetach()
        onCustomClickListenerRecycler = null

    }

//    override fun openCustomsScreen() {
//        router.navigateTo(Screens.CustomScreen())
//    }

    override fun onResume() {
        super.onResume()
        println("OnResume")
        if(DataHolder.user!!.id != null){
            mHomeMainPresenter.getProductByContactId(DataHolder.user!!.id)
        }
//        binding.productsRv.adapter!!.notifyDataSetChanged()
//        binding.customsRv.adapter!!.notifyDataSetChanged()
    }

    override fun openScanActivity() {
//        PhoneNumberScreen
//        router.navigateTo(Screens.PhoneNumberScreen())
//        router.navigateTo(Screens.ScanScreen())
//        var intent = Intent(context, ScanActivity::class.java)
//        activity!!.startActivity(intent)
    }

    fun setProductsByContact(response: ProductResponceContact){
        products.clear()
//        products.addAll(response.resultObject!!)
        products.addAll(response.resultObject!!)
        recyclerProductsAdapter.setItems(products)
//        recyclerProductsAdapter.notifyDataSetChanged()
    }
}
