package za.co.app.budgetbee.ui.custom_views

import android.content.Context
import android.view.LayoutInflater
import android.widget.TextView
import com.github.mikephil.charting.components.MarkerView
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.utils.MPPointF
import za.co.app.budgetbee.R
import za.co.app.budgetbee.ui.report.LineChartEntry
import kotlin.math.roundToInt


class LineChartMarkerView(context: Context, layoutResource: Int) :
    MarkerView(context, layoutResource) {
    private val amountTextView: TextView
    private val dateTextView: TextView

    // callbacks everytime the MarkerView is redrawn, can be used to update the
    // content (user-interface)
    override fun refreshContent(entry: Entry, highlight: Highlight?) {
        entry as LineChartEntry
        amountTextView.text = "Amount* ${entry.y.roundToInt()}"
        dateTextView.text = entry.getDateString()
    }

    override fun getOffset(): MPPointF {
        return MPPointF((-(width / 2)).toFloat(), (-height).toFloat())
    }

    init {
        val view = LayoutInflater.from(getContext()).inflate(layoutResource, this)
        amountTextView = view.findViewById(R.id.text_amount_value)
        dateTextView = view.findViewById(R.id.text_date)
    }
}
