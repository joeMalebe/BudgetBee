package za.co.app.budgetbee.ui.custom_views

import android.content.Context
import android.widget.TextView
import com.github.mikephil.charting.components.MarkerView
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.utils.MPPointF
import kotlinx.android.synthetic.main.line_chart_marker.view.*
import za.co.app.budgetbee.ui.report.LineChartEntry
import kotlin.math.roundToInt


class LineChartMarkerView(context: Context, layoutResource: Int) : MarkerView(context, layoutResource) {
        private val amountTextView: TextView
        private val dateTextView: TextView

        // callbacks everytime the MarkerView is redrawn, can be used to update the
        // content (user-interface)
        override fun refreshContent(entry: Entry, highlight: Highlight?) {
            val lineChartEntry = entry as LineChartEntry
            amountTextView.text = "Amount* ${entry.y.roundToInt()}"
            dateTextView.text = entry.getDateString()
        }

    /*override fun setOffset(offsetX: Float, offsetY: Float) {
        super.setOffset(getXOffset(offsetX), getYOffset(offsetY))
    }*/

    override fun getOffset(): MPPointF {
        return MPPointF((-(width / 2)).toFloat(), (-height).toFloat())
    }

    /*fun getXOffset(xpos: Float): Float {
            // this will center the marker-view horizontally
            return -(width.toFloat() / 2f)
        }

        fun getYOffset(ypos: Float): Float {
            // this will cause the marker-view to be above the selected value
            return -height.toFloat()
        }*/

        init {
            // this markerview only displays a textview
            amountTextView = text_amount_value
            dateTextView = text_date
        }
    }
