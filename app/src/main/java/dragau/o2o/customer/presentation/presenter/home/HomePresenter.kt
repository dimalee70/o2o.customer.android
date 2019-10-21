package dragau.o2o.customer.presentation.presenter.home

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import dragau.o2o.customer.presentation.view.home.HomeView
import ru.terrakok.cicerone.Router

@InjectViewState
class HomePresenter(private var router: Router) : MvpPresenter<HomeView>() {

}
