package za.co.app.budgetbee.utils

import android.app.DatePickerDialog
import android.content.Context
import android.widget.DatePicker
import com.google.android.material.textfield.TextInputEditText
import java.math.BigDecimal
import java.text.SimpleDateFormat
import java.util.*

//Wed, 4 Jul 2001
val DAY_MONTH_YEAR = "EEE, d MMM yyyy"

fun Calendar.getDateStringByFormat(format: String = ""): String {
    val simpleDateFormat = getSimpleDateFormat(format)
    return simpleDateFormat.format(timeInMillis)
}

private fun getSimpleDateFormat(format: String): SimpleDateFormat {
    val simpleDateFormat = if (format.isBlank()) {
        SimpleDateFormat(DAY_MONTH_YEAR, Locale.ENGLISH)
    } else {
        SimpleDateFormat(format, Locale.ENGLISH)
    }
    return simpleDateFormat
}

fun Calendar.showDatePickerDialogAndDisplaySelectedDateTextToView(context: Context, displaySelectedDateTextView: TextInputEditText) {
    setupDateDialogue(this, context, displaySelectedDateTextView)
}

private fun setupDateDialogue(calendar: Calendar, context: Context, displaySelectedDateTextView: TextInputEditText) {
    val year = calendar.get(Calendar.YEAR)
    val monthOfYear = calendar.get(Calendar.MONTH)
    val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)
    DatePickerDialog(context, (displaySelectedDateToView(calendar, displaySelectedDateTextView)), year, monthOfYear, dayOfMonth)
            .show()
}

private fun displaySelectedDateToView(calendar: Calendar, displaySelectedDateTextView: TextInputEditText): (DatePicker, Int, Int, Int) -> Unit {
    return { datePicker, selectedYear, selectedMonth, selectedDay ->
        calendar.set(selectedYear, selectedMonth, selectedDay)
        displaySelectedDateTextView.setText(calendar.getDateStringByFormat())
    }
}

fun Double.displayLongDouble(): String {
    return BigDecimal.valueOf(this).toPlainString()
}