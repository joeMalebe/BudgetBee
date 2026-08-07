package za.co.app.budgetbee.ui.custom_views

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import za.co.app.budgetbee.data.model.domain.Month
import za.co.app.budgetbee.databinding.ItemMonthsBinding
import java.util.Calendar

open class MonthDialogAdapter(val months: ArrayList<Month>, var year: Int) : RecyclerView.Adapter<MonthDialogAdapter.MonthViewHolder>() {
    private val publishMonth = PublishSubject.create<Calendar>()
    private lateinit var binding: ItemMonthsBinding
    override fun getItemCount(): Int {
        return months.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MonthViewHolder {
        binding = ItemMonthsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MonthViewHolder(binding)
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

    inner class MonthViewHolder(val binding: ItemMonthsBinding) : RecyclerView.ViewHolder(binding.root) {
        fun display(month: Month, year: Int) {
            binding.monthText.text = month.displayName
            binding.root.setOnClickListener {
                val calendar = Calendar.getInstance()
                calendar.set(year, month.position, 1)
                publishMonth.onNext(calendar)
            }
        }
    }
}
