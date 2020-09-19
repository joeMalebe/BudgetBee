package za.co.app.budgetbee.ui.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.layout_switcher.view.*
import za.co.app.budgetbee.R
import za.co.app.budgetbee.utils.getDateStringByFormat
import java.util.*

class Switcher @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    dyfStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, dyfStyleAttr) {
    private val MONTH_YEAR__DATE_FORMAT = "MMM yyyy"
    val calendar: Calendar = Calendar.getInstance()
    val publishCalender = PublishSubject.create<Calendar>()
    fun init(calendar: Calendar) {
        this.calendar.set(
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        LayoutInflater.from(context).inflate(R.layout.layout_switcher, this)
        selected_text.text = this.calendar.getDateStringByFormat(MONTH_YEAR__DATE_FORMAT)
        left_selector.setOnClickListener {
            updateCalenderMonths(-1)
        }
        right_selector.setOnClickListener {
            updateCalenderMonths(1)
        }
    }

    private fun updateCalenderMonths(monthsToUpdate: Int) {
        this.calendar.add(Calendar.MONTH, monthsToUpdate)
        selected_text.text = this.calendar.getDateStringByFormat(MONTH_YEAR__DATE_FORMAT)
        publishCalender.onNext(this.calendar)
    }
}