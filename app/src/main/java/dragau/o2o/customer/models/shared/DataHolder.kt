package dragau.o2o.customer.models.shared


import androidx.preference.PreferenceManager
import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import dragau.o2o.customer.App
import dragau.o2o.customer.Constants
import dragau.o2o.customer.models.objects.User


object DataHolder : BaseObservable()
{
    val sharedPref = PreferenceManager.getDefaultSharedPreferences(App.appComponent.context())

    var user: User? = null
    @Bindable get
    set(value) {
        field = value
        //notifyPropertyChanged(BR.user)
    }

    var userId: String?
    set(value) {
        sharedPref.edit().putString(Constants.userIdPrefsKey, value).apply()
    }
    get() {
        return sharedPref.getString(Constants.userIdPrefsKey, null)
    }

    var jwt: String?
    set(value) {
        sharedPref.edit().putString(Constants.jwtPrefsKey, value).apply()
    }
    get() {
        return sharedPref.getString(Constants.jwtPrefsKey, null)
    }
}