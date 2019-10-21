package dragau.o2o.customer.presentation.presenter.dialogs

import android.content.Context
import androidx.appcompat.app.AlertDialog
import dragau.o2o.customer.Constants

class ProductExistDialog(val context: Context) {
    private var runner: Runnable? = null
    var dialog: AlertDialog? = null

    fun show(afterDelayMilliSec: Long = Constants.progressDelay) {

        this.runner = Runnable {
            try {
                if (dialog == null || (dialog != null && !dialog!!.isShowing)) {
                    dialog = ProductExistDialogPresenter(context).apply {}.create(null)
                    dialog?.setCancelable(false)
                    dialog?.show()
                }
            } catch (e: Exception)
            {
                //Toast.makeText(context, e.localizedMessage, Toast.LENGTH_SHORT).show()
            }
        }

        DialogHandler.dialogHandler.postDelayed(this.runner!!, afterDelayMilliSec)
    }

    fun cancel() {
        DialogHandler.dialogHandler.removeCallbacks(runner!!)
        dialog?.cancel()
        dialog = null
    }
}