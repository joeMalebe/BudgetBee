package za.co.app.budgetbee.ui.views

import android.content.Context
import android.util.AttributeSet
import android.view.View
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.layout_year_switcher.view.*
import java.util.*

class YearSwitcher @JvmOverloads constructor(context: Context, attributeSet: AttributeSet? = null, defStyleRef: Int = 0) :
        View(context, attributeSet, defStyleRef) {

    val publishYear = PublishSubject.create<Int>()

    fun init() {
        val year = Calendar.getInstance()[Calendar.YEAR]
        year_text.setText(year)
        left_selector.setOnClickListener {
            publishYear.onNext(year.dec())
        }
        right_selector.setOnClickListener {
            publishYear.onNext(year.inc())
        }
    }
}