package za.co.app.budgetbee.ui.report

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import za.co.app.budgetbee.data.model.domain.Transaction
import za.co.app.budgetbee.databinding.ItemTransactionReportBinding

private const val PROGRESS_BAR_PERCENTAGE_TEXT_OFFSET = 60

class ReportAdapter(val transactions: List<Transaction>, private val colors: Array<String>) : RecyclerView.Adapter<ReportAdapter.TransactionReportViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionReportViewHolder {
        val binding = ItemTransactionReportBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TransactionReportViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TransactionReportViewHolder, position: Int) {
        val transaction = transactions[position]
        holder.display(transaction, transactions, colors[getValidColorIndex(position,colors.size)], isLastItem(position))
    }

    private fun getValidColorIndex(position: Int, numberOfColors: Int): Int {
        return position % numberOfColors
    }

    private fun isLastItem(position: Int) = transactions.size - 1 == position

    override fun getItemCount() = transactions.size

    class TransactionReportViewHolder(val binding: ItemTransactionReportBinding) : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun display(transaction: Transaction, transactionsList: List<Transaction>, color: String, isLastItem: Boolean) {
            binding.apply {
                textTransactionDescription.text = transaction.transactionCategoryName
                textTransactionAmount.text = transaction.transactionAmount.toString()
                val percentage = getAveragePercentage(transactionsList, transaction)
                if(percentage > PROGRESS_BAR_PERCENTAGE_TEXT_OFFSET) {
                    textTransactionAmountPercentage.setTextColor(Color.WHITE)
                }
                progressBar.progress = percentage
                progressBar.progressTintList = ColorStateList.valueOf(Color.parseColor(color))
                textTransactionAmountPercentage.text = "$percentage%"
                divider.visibility = if (isLastItem) {
                    GONE
                } else {
                    VISIBLE
                }
            }
        }

        private fun getAveragePercentage(transactionsList: List<Transaction>, transaction: Transaction) =
                (transaction.transactionAmount / transactionsList.sumOf { it.transactionAmount } * 100).toInt()
    }
}