package za.co.app.budgetbee.ui.custom_views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.layout_year_switcher.view.*
import za.co.app.budgetbee.R
import java.util.*

class YearSwitcher @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, dyfStyleAttr: Int = 0) :
        ConstraintLayout(context, attrs, dyfStyleAttr) {

    private val publishYear = PublishSubject.create<Int>()
    var year = 0
        set(value) {
            field = value
            year_text.text = year.toString()
        }

    init {
        LayoutInflater.from(context).inflate(R.layout.layout_year_switcher, this)
    }

    fun init(calendar: Calendar) {

        year = calendar[Calendar.YEAR]
        year_text.text = year.toString()
        left_selector.setOnClickListener {
            year = year.dec()
            publishYearAndUpdateText()
        }
        right_selector.setOnClickListener {
            year = year.inc()
            publishYearAndUpdateText()
        }
    }

    private fun publishYearAndUpdateText() {
        publishYear.onNext(year)
        year_text.text = year.toString()
    }

    fun getSelectedYear(): Observable<Int> {
        return publishYear.hide()
    }
}