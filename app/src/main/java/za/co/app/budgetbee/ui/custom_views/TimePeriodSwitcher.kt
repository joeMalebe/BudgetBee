package za.co.app.budgetbee.ui.custom_views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.constraintlayout.widget.ConstraintLayout
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.layout_time_period_switcher.view.*
import za.co.app.budgetbee.R
import za.co.app.budgetbee.ui.report.BalanceReportPresenter

class TimePeriodSwitcher @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, dyfStyleAttr: Int = 0) :
        ConstraintLayout(context, attrs, dyfStyleAttr) {

    var timePeriodTextView: TextView
    var leftSelector: AppCompatImageView
    var rightSelector: AppCompatImageView
    private val publishTimePeriod = PublishSubject.create<BalanceReportPresenter.PERIOD>()
    val timePeriods = BalanceReportPresenter.PERIOD.values()
    var currentTimePeriod = 0
        set(value) {
            field = value
            time_period_text.text =  timePeriods[currentTimePeriod].textValue
        }

    init {
        LayoutInflater.from(context).inflate(R.layout.layout_time_period_switcher, this)

        timePeriodTextView = time_period_text
        leftSelector = left_selector
        rightSelector = right_selector
        val typedArray = context.theme.obtainStyledAttributes(attrs, R.styleable.TimePeriodSwitcherStyle, 0, 0)
        val switcherColor = typedArray.getColor(R.styleable.TimePeriodSwitcherStyle_time_switcher_color, -1)
        timePeriodTextView.setTextColor(switcherColor)
        leftSelector.setColorFilter(switcherColor)
        rightSelector.setColorFilter(switcherColor)
    }

    fun init(period : BalanceReportPresenter.PERIOD) {

        timePeriodTextView.text = period.textValue
        val numberOfTimePeriods = timePeriods.size
        leftSelector.setOnClickListener {
            currentTimePeriod = if (currentTimePeriod == 0) numberOfTimePeriods - 1 else currentTimePeriod.dec()
            publishTimePeriodText()
        }
        rightSelector.setOnClickListener {
            currentTimePeriod = currentTimePeriod.inc() % numberOfTimePeriods
            publishTimePeriodText()
        }
    }

    private fun publishTimePeriodText() {
        publishTimePeriod.onNext(timePeriods[currentTimePeriod])
        timePeriodTextView.text = timePeriods[currentTimePeriod].textValue
    }

    fun getSelectedTimePeriod(): Observable<BalanceReportPresenter.PERIOD> {
        return publishTimePeriod.hide()
    }

}