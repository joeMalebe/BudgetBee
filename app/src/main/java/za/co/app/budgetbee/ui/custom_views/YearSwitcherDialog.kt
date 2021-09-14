package za.co.app.budgetbee.ui.custom_views

import android.content.Context
import android.view.MotionEvent
import android.view.View
import java.util.*

class YearSwitcherDialog(context: Context) : androidx.appcompat.app.AlertDialog(context) {
    private lateinit var firstSixMonthsAdapter: MonthDialogAdapter
    private lateinit var lastSixMonthsAdapter: MonthDialogAdapter
    private lateinit var yearSwitcher: YearSwitcher
    private lateinit var monthSwitcher: MonthSwitcher

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN) {
            val currentYear = monthSwitcher.calendar[Calendar.YEAR]
            updateYearSwitcherMonths(firstSixMonthsAdapter, currentYear, lastSixMonthsAdapter)
            yearSwitcher.year = currentYear
            this.dismiss()

        }
        return super.onTouchEvent(event)
    }

    fun init(firstSixMonthsAdapter: MonthDialogAdapter, lastSixMonthsAdapter: MonthDialogAdapter, view: View, yearSwitcher: YearSwitcher, monthSwitcher: MonthSwitcher) {
        this.firstSixMonthsAdapter = firstSixMonthsAdapter
        this.lastSixMonthsAdapter = lastSixMonthsAdapter
        this.yearSwitcher = yearSwitcher
        this.monthSwitcher = monthSwitcher
        this.setView(view)
    }

    private fun updateYearSwitcherMonths(firstSixMonthsAdapter: MonthDialogAdapter, selectedYear: Int, lastSixMonthsAdapter: MonthDialogAdapter) {
        firstSixMonthsAdapter.updateYear(selectedYear)
        firstSixMonthsAdapter.notifyDataSetChanged()
        lastSixMonthsAdapter.updateYear(selectedYear)
        lastSixMonthsAdapter.notifyDataSetChanged()
    }
}