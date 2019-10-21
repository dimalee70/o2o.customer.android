package dragau.o2o.customer.presentation.presenter.dialogs

import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import dragau.o2o.customer.R
import dragau.o2o.customer.models.enums.DialogSize

class ConfirmDialogPresenter (private var context: Context, private val title: Int, private val message: Int): BaseDialogHelper(){

    override val dialogView: View by lazy {
        LayoutInflater.from(context).inflate(R.layout.confirm, null)
    }

    override val builder: AlertDialog.Builder = AlertDialog.Builder(context).setView(dialogView)

    override fun create(dialogSize: DialogSize?): AlertDialog {
        dialog = super.create(null)
        dialogView.findViewById<TextView>(R.id.titleTv)!!.setText(title)
        dialogView.findViewById<TextView>(R.id.confirmTv)!!.setText(message)
        dialog?.setCanceledOnTouchOutside(false)
        return dialog!!

    }


    private val yesBtn: Button by lazy {
        dialogView.findViewById<Button>(R.id.yesBtn)
    }

    //  closeIconClickListener with listener
    fun yesBtnClickListener(func: (() -> Unit)? = null) =
        with(yesBtn) {
            setClickListenerToDialogButton(func)
        }

    private val noBtn: Button by lazy {
        dialogView.findViewById<Button>(R.id.noBtn)
    }

    //  closeIconClickListener with listener
    fun noBtnClickListener(func: (() -> Unit)? = null) =
        with(noBtn) {
            setClickListenerToDialogButton(func)
        }
//

}