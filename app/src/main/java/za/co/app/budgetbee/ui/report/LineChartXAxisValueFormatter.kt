package za.co.app.budgetbee.ui.report

import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import za.co.app.budgetbee.utils.getDateStringByFormat
import java.util.*


class LineChartXAxisValueFormatter : IndexAxisValueFormatter() {
    override fun getFormattedValue(value: Float): String {

        // Convert from seconds back to milliseconds to format time  to show to the user
        val transactionsDateInMillis = value.toLong() * 1000

        // Show time in local version
        val date = Calendar.getInstance()
        date.timeInMillis = transactionsDateInMillis
        return date.getDateStringByFormat("MMM yyyy")
    }
}