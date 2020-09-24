package za.co.app.budgetbee.ui.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.layout_month_switcher.view.*
import za.co.app.budgetbee.R
import za.co.app.budgetbee.utils.getDateStringByFormat
import java.util.*

class MonthSwitcher @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, dyfStyleAttr: Int = 0) :
        ConstraintLayout(context, attrs, dyfStyleAttr) {

    private val MONTH_YEAR_DATE_FORMAT = "MMM yyyy"
    val calendar: Calendar = Calendar.getInstance()
    val publishCalender = PublishSubject.create<Calendar>()

    fun init(calendar: Calendar) {
        this.calendar.set(
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH), 1
        )

        LayoutInflater.from(context).inflate(R.layout.layout_month_switcher, this)
        month_text.text = this.calendar.getDateStringByFormat(MONTH_YEAR_DATE_FORMAT)

        left_selector.setOnClickListener {
            updateCalenderMonths(-1)
        }
        right_selector.setOnClickListener {
            updateCalenderMonths(1)
        }
    }

    private fun updateCalenderMonths(monthsToUpdate: Int) {
        this.calendar.set(Calendar.DAY_OF_MONTH, 1)
        this.calendar.add(Calendar.MONTH, monthsToUpdate)
        month_text.text = this.calendar.getDateStringByFormat(MONTH_YEAR_DATE_FORMAT)
        publishCalender.onNext(this.calendar)
    }

    fun getSelectedDate(): Observable<Calendar> {
        return publishCalender.hide()
    }
}