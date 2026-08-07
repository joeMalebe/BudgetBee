package za.co.app.budgetbee.ui.custom_views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.constraintlayout.widget.ConstraintLayout
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import za.co.app.budgetbee.R
import za.co.app.budgetbee.databinding.LayoutMonthSwitcherBinding
import za.co.app.budgetbee.utils.getDateStringByFormat
import java.util.Calendar

private const val MONTH_YEAR_DATE_FORMAT = "MMM yyyy"

class MonthSwitcher @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    dyfStyleAttr: Int = 0
) :
    ConstraintLayout(context, attrs, dyfStyleAttr) {


    val calendar: Calendar = Calendar.getInstance()
    private var monthTextView: TextView
    private var leftSelector: AppCompatImageView
    private var rightSelector: AppCompatImageView
    private var binding: LayoutMonthSwitcherBinding

    private val publishCalender = PublishSubject.create<Calendar>()
    private val publishClickMonthSubject = PublishSubject.create<Boolean>()

    init {
        binding = LayoutMonthSwitcherBinding.inflate(LayoutInflater.from(context), this, true)
        monthTextView = binding.monthText
        leftSelector = binding.leftSelector
        rightSelector = binding.rightSelector
        val typedArray =
            context.theme.obtainStyledAttributes(attrs, R.styleable.MonthSwitcherStyle, 0, 0)
        val switcherColor = typedArray.getColor(R.styleable.MonthSwitcherStyle_switcher_color, -1)
        monthTextView.setTextColor(switcherColor)
        leftSelector.setColorFilter(switcherColor)
        rightSelector.setColorFilter(switcherColor)
    }

    fun init(calendar: Calendar) {
        this.calendar.set(
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH), 1
        )

        monthTextView.text = this.calendar.getDateStringByFormat(MONTH_YEAR_DATE_FORMAT)
        monthTextView.setOnClickListener {
            publishClickMonthSubject.onNext(true)
        }

        leftSelector.setOnClickListener {
            updateCalenderMonths(-1)
        }

        rightSelector.setOnClickListener {
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
        return publishClickMonthSubject.hide()
    }

    fun updateDate(calendar: Calendar) {
        this.calendar.set(calendar[Calendar.YEAR], calendar[Calendar.MONTH], 1)
        monthTextView.text = this.calendar.getDateStringByFormat(MONTH_YEAR_DATE_FORMAT)
    }
}