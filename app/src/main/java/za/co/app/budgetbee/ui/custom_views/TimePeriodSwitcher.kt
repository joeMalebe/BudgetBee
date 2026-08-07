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
import za.co.app.budgetbee.databinding.LayoutTimePeriodSwitcherBinding
import za.co.app.budgetbee.ui.report.BalanceReportPresenter

class TimePeriodSwitcher @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    dyfStyleAttr: Int = 0
) :
    ConstraintLayout(context, attrs, dyfStyleAttr) {

    private var timePeriodTextView: TextView
    private var leftSelector: AppCompatImageView
    private var rightSelector: AppCompatImageView
    private val publishTimePeriod = PublishSubject.create<BalanceReportPresenter.PERIOD>()
    private val timePeriods = BalanceReportPresenter.PERIOD.values()
    private var currentTimePeriod = 0
        set(value) {
            field = value
            binding.timePeriodText.text = timePeriods[currentTimePeriod].textValue
        }
    private var binding: LayoutTimePeriodSwitcherBinding

    init {
        binding = LayoutTimePeriodSwitcherBinding.inflate(LayoutInflater.from(context), this, true)

        timePeriodTextView = binding.timePeriodText
        leftSelector = binding.leftSelector
        rightSelector = binding.rightSelector
        val typedArray =
            context.theme.obtainStyledAttributes(attrs, R.styleable.TimePeriodSwitcherStyle, 0, 0)
        val switcherColor =
            typedArray.getColor(R.styleable.TimePeriodSwitcherStyle_time_switcher_color, -1)
        timePeriodTextView.setTextColor(switcherColor)
        leftSelector.setColorFilter(switcherColor)
        rightSelector.setColorFilter(switcherColor)
    }

    fun init(period: BalanceReportPresenter.PERIOD) {

        timePeriodTextView.text = period.textValue
        val numberOfTimePeriods = timePeriods.size
        leftSelector.setOnClickListener {
            currentTimePeriod =
                if (currentTimePeriod == 0) numberOfTimePeriods - 1 else currentTimePeriod.dec()
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