package za.co.app.budgetbee.ui.report

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_transaction_report.view.*
import za.co.app.budgetbee.R
import za.co.app.budgetbee.data.model.domain.Transaction

class ReportAdapter(val transactions: List<Transaction>, val colors: Array<String>) : RecyclerView.Adapter<ReportAdapter.TransactionReportViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionReportViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_transaction_report, parent, false)
        return TransactionReportViewHolder(view)
    }

    override fun onBindViewHolder(holder: TransactionReportViewHolder, position: Int) {
        val transaction = transactions[position]
        holder.display(transaction, transactions, colors[getValidColorIndex(position,colors.size)], isLastItem(position))
    }

    fun getValidColorIndex(position: Int, numberOfColors: Int): Int {
        return position % numberOfColors
    }

    fun isLastItem(position: Int) = transactions.size - 1 == position

    override fun getItemCount() = transactions.size

    class TransactionReportViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        @SuppressLint("SetTextI18n")
        fun display(transaction: Transaction, transactionsList: List<Transaction>, color: String, isLastItem: Boolean) {
            view.apply {
                text_transaction_description.text = transaction.transactionCategoryName
                text_transaction_amount.text = transaction.transactionAmount.toString()
                val percentage = getAveragePercentage(transactionsList, transaction)
                progress_bar.progress = percentage
                progress_bar.setProgressTintList(ColorStateList.valueOf(Color.parseColor(color)));
                text_transaction_amount_percentage.text = "$percentage%"
                divider.visibility = if (isLastItem) {
                    GONE
                } else {
                    VISIBLE
                }
            }
        }

        private fun getAveragePercentage(transactionsList: List<Transaction>, transaction: Transaction) =
                (transaction.transactionAmount / transactionsList.sumByDouble { it.transactionAmount } * 100).toInt()
    }
}