package dragau.o2o.customer.extensions

import android.widget.EditText
import com.google.android.material.textfield.TextInputEditText

inline fun EditText.getIntValue(): Int
{
    return this.text.toString().toInt()
}