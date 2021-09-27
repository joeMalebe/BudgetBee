package za.co.app.budgetbee.ui.report

import com.github.mikephil.charting.data.Entry
import za.co.app.budgetbee.utils.getDateStringByFormat
import java.util.*

class LineChartEntry(x :Float, y :Float, val dateInMillis : Long) : Entry(x,y) {
    fun getDateString(): String {
        val date = Calendar.getInstance()
        date.timeInMillis = dateInMillis
        return date.getDateStringByFormat("EEE, dd MMM")
    }
}