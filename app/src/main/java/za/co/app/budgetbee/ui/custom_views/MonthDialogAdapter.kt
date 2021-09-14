package za.co.app.budgetbee.ui.custom_views

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.item_months.view.*
import za.co.app.budgetbee.R
import za.co.app.budgetbee.data.model.domain.Month
import java.util.*

open class MonthDialogAdapter(val months: ArrayList<Month>, var year: Int) : RecyclerView.Adapter<MonthDialogAdapter.MonthViewHolder>() {
    private val publishMonth = PublishSubject.create<Calendar>()

    override fun getItemCount(): Int {
        return months.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MonthViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_months, parent, false)
        return MonthViewHolder(view)
    }

    override fun onBindViewHolder(holder: MonthViewHolder, position: Int) {
        holder.display(months[position], year)
    }

    fun updateYear(year: Int) {
        this.year = year
    }

    fun getSelectedMonth(): Observable<Calendar> {
        return publishMonth.hide()
    }

    inner class MonthViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun display(month: Month, year: Int) {
            itemView.month_text.text = month.displayName
            itemView.setOnClickListener {
                val calendar = Calendar.getInstance()
                calendar.set(year, month.position, 1)
                publishMonth.onNext(calendar)
            }
        }
    }
}
