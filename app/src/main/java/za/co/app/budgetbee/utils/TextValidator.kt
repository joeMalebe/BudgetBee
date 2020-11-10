package za.co.app.budgetbee.utils

import android.text.Editable
import android.text.TextWatcher
import android.widget.TextView

abstract class TextValidator(val textView: TextView) : TextWatcher {
    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        /* no-op */
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        /* no-op */
    }

    override fun afterTextChanged(s: Editable?) {
        val text = textView.text.toString()
        validate(textView, text)
    }

    abstract fun validate(textView: TextView, text: String)
}