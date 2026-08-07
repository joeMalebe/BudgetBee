package za.co.app.budgetbee.ui.custom_views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import za.co.app.budgetbee.databinding.LayoutYearSwitcherBinding
import java.util.Calendar

class YearSwitcher @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    dyfStyleAttr: Int = 0
) :
    ConstraintLayout(context, attrs, dyfStyleAttr) {

    private val publishYear = PublishSubject.create<Int>()
    private var binding: LayoutYearSwitcherBinding
    var year = 0
        set(value) {
            field = value
            binding.yearText.text = year.toString()
        }

    init {
        binding = LayoutYearSwitcherBinding.inflate(LayoutInflater.from(context), this, true)
    }

    fun init(calendar: Calendar) {

        year = calendar[Calendar.YEAR]
        binding.yearText.text = year.toString()
        binding.leftSelector.setOnClickListener {
            year = year.dec()
            publishYearAndUpdateText()
        }
        binding.rightSelector.setOnClickListener {
            year = year.inc()
            publishYearAndUpdateText()
        }
    }

    private fun publishYearAndUpdateText() {
        publishYear.onNext(year)
        binding.yearText.text = year.toString()
    }

    fun getSelectedYear(): Observable<Int> {
        return publishYear.hide()
    }
}