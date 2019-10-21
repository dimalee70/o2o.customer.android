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
import dragau.o2o.customer.R
import dragau.o2o.customer.Screens
import dragau.o2o.customer.databinding.FragmentHomeMainBinding
import dragau.o2o.customer.presentation.presenter.home.HomeMainPresenter
import dragau.o2o.customer.presentation.view.home.HomeMainView
import dragau.o2o.customer.ui.activity.product.ScanActivity
import dragau.o2o.customer.ui.fragment.BaseMvpFragment
import ru.terrakok.cicerone.Router
import java.lang.ClassCastException
import javax.inject.Inject
import kotlin.collections.ArrayList

class HomeMainFragment : BaseMvpFragment(), HomeMainView
//    , OnItemClickListener<OrdersByOutletResult>
{



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

//    lateinit var recyclerCustomsAdapter: RecyclerBindingAdapter<OrdersByOutletResult>
//
//    lateinit var recyclerTypesAdapter: RecyclerBindingAdapter<Types>
//
//    private var onCustomClickListenerRecycler: OnItemClickListener<OrdersByOutletResult>? = this

    private val lifecycleRegistry = LifecycleRegistry(this)
//    var customs = ObservableArrayList<Customs>()
//    var types = ObservableArrayList<Types>()

//    private var onTypeClickListenerRecycle: OnItemClickListener<Types>? = object: OnItemClickListener<Types>{
//        override fun onItemClick(position: Int, item: Types) {
//            Toast.makeText(context!!, item.text, Toast.LENGTH_SHORT).show()
//        }
//
//    }
//        object: RecyclerBindingAdapter.OnItemClickListener<Customs>{
//            override fun onItemClick(position: Int, item: Customs) {
//                Toast.makeText(context!!, item.text, Toast.LENGTH_SHORT).show()
//            }
//        }


    override fun onCreate(savedInstanceState: Bundle?) {
        App.appComponent.inject(this)
        super.onCreate(savedInstanceState)
//        mHomeMainPresenter.attachLifecycle(lifecycleRegistry)
//        mHomeMainPresenter.observeForOrderByOutletResponseBoundary()
//            .observe(this, Observer {
//                response -> response.let {
//                setOrderByOutlet(response)
//            }
//            })
//        mHomeMainPresenter.getOrdersByOtlet("fe28218f-10b9-4d70-50a1-08d73cba8606")
//        recyclerCustomsAdapter = RecyclerBindingAdapter(R.layout.item_custom, BR.data, context!!)
//        recyclerTypesAdapter = RecyclerBindingAdapter(R.layout.item_type, BR.data, context!!)
//        if(onCustomClickListenerRecycler != null){
//            recyclerCustomsAdapter.setOnItemClickListener(onCustomClickListenerRecycler!!)
//        }
//        if(onTypeClickListenerRecycle != null)
//            recyclerTypesAdapter.setOnItemClickListener(onTypeClickListenerRecycle!!)
    }

//    private fun setOrderByOutlet(ordersByOutletResponce: OrdersByOutletResponce?) {
//        customs.addAll(ordersByOutletResponce!!.resultObject!!)
//        recyclerCustomsAdapter.setItems(customs)
//        recyclerCustomsAdapter.notifyDataSetChanged()
//    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home_main, container, false)
        binding.presenter = mHomeMainPresenter

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
//        binding.customsRv.adapter = recyclerCustomsAdapter
//        binding.typesRv.setHasFixedSize(true)
//        binding.customsRv.setHasFixedSize(true)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

//    override fun onAttach(context: Context) {
//        super.onAttach(context)
//        try {
//            onCustomClickListenerRecycler = this
//            onTypeClickListenerRecycle = object: OnItemClickListener<Types>{
//                override fun onItemClick(position: Int, item: Types) {
//                    Toast.makeText(context, item.text, Toast.LENGTH_SHORT).show()
//                }
//
//            }
//        }catch (e: Throwable){
//            throw ClassCastException(context.toString())
//        }
//    }

    override fun onDetach() {
        super.onDetach()
//        onCustomClickListenerRecycler = null
//        onTypeClickListenerRecycle = null
    }

//    override fun openCustomsScreen() {
//        router.navigateTo(Screens.CustomScreen())
//    }

    override fun onResume() {
        super.onResume()
//        binding.customsRv.adapter!!.notifyDataSetChanged()
    }

    override fun openScanActivity() {
//        PhoneNumberScreen
//        router.navigateTo(Screens.PhoneNumberScreen())
//        router.navigateTo(Screens.ScanScreen())
        var intent = Intent(context, ScanActivity::class.java)
        activity!!.startActivity(intent)
    }
}
