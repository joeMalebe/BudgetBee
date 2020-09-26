package za.co.app.budgetbee.ui.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.TextView
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
    var monthTextView: TextView
    private val publishCalender = PublishSubject.create<Calendar>()
    private val publishClickMnnthSubject = PublishSubject.create<Boolean>()

    init{
        LayoutInflater.from(context).inflate(R.layout.layout_month_switcher, this)
        monthTextView = month_text
    }

    fun init(calendar: Calendar) {
        this.calendar.set(
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH), 1
        )

        monthTextView.text = this.calendar.getDateStringByFormat(MONTH_YEAR_DATE_FORMAT)
        monthTextView.setOnClickListener {
            publishClickMnnthSubject.onNext(true)
        }

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
        monthTextView.text = this.calendar.getDateStringByFormat(MONTH_YEAR_DATE_FORMAT)
        publishCalender.onNext(this.calendar)
    }

    fun getSelectedDate(): Observable<Calendar> {
        return publishCalender.hide()
    }

    fun onMonthClicked(): Observable<Boolean> {
        return publishClickMnnthSubject.hide()
    }

    fun updateDate(calendar: Calendar) {
        this.calendar.set(calendar[Calendar.YEAR], calendar[Calendar.MONTH], 1)
        monthTextView.text = this.calendar.getDateStringByFormat(MONTH_YEAR_DATE_FORMAT)
    }
}