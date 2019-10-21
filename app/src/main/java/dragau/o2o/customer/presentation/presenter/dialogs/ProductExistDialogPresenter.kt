package dragau.o2o.customer.presentation.presenter.dialogs

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import dragau.o2o.customer.R
import dragau.o2o.customer.models.enums.DialogSize

class ProductExistDialogPresenter(private var context: Context): BaseDialogHelper(){

    override val dialogView: View by lazy {
        LayoutInflater.from(context).inflate(R.layout.product_exist_dialog, null)
    }

    override val builder: AlertDialog.Builder = AlertDialog.Builder(context).setView(dialogView)

    override fun create(dialogSize: DialogSize?): AlertDialog {
        dialog = super.create(null)
        dialog?.setCanceledOnTouchOutside(false)
        return dialog!!
    }

    private val okBtn: Button by lazy {
        dialogView.findViewById<Button>(R.id.okBtn)
    }

    private val showBtn: Button by lazy {
        dialogView.findViewById<Button>(R.id.showBtn)
    }

    fun okBtnClicklistener(func: (() -> Unit)? = null) =
        with(okBtn){
            setClickListenerToDialogButton(func)
        }

    fun showBtnClickListener(func: (() -> Unit)? = null) =
        with(showBtn){
            setClickListenerToDialogButton(func)
        }

}