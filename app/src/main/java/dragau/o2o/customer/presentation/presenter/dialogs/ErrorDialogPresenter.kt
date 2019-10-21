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

class ErrorDialogPresenter(private val context: Context, val title: String?, val message: String?) : BaseDialogHelper() {
    //  dialog view
    override val dialogView: View by lazy {
        LayoutInflater.from(context).inflate(R.layout.dialog_error, null)
    }

    override val builder: AlertDialog.Builder = AlertDialog.Builder(context).setView(dialogView)

    override fun create(dialogSize: DialogSize?): AlertDialog {
        dialog = super.create(null)

        dialogView.findViewById<TextView>(R.id.dialog_title)!!.text = title

        if (message != null)
            dialogView.findViewById<TextView>(R.id.dialog_info)!!.text = message

        val windowlp = dialog?.window?.attributes
        windowlp?.windowAnimations = R.style.DialogAnimation

        windowlp?.gravity = Gravity.BOTTOM
        dialog?.window?.attributes = windowlp
        dialog?.setCanceledOnTouchOutside(false)

        return dialog!!
    }

    private val okButton: Button by lazy {
        dialogView.findViewById<Button>(R.id.dialog_ok)
    }

    //  closeIconClickListener with listener
    fun closeIconClickListener(func: (() -> Unit)? = null) =
        with(okButton) {
            setClickListenerToDialogButton(func)
        }
}
