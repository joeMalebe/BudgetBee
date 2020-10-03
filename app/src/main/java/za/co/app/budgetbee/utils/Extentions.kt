package za.co.app.budgetbee.utils

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