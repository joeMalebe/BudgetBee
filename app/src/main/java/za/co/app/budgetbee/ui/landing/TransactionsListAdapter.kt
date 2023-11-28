package za.co.app.budgetbee.ui.landing

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import za.co.app.budgetbee.R
import za.co.app.budgetbee.data.model.domain.Transaction
import za.co.app.budgetbee.data.model.domain.TransactionCategoryType
import za.co.app.budgetbee.databinding.ItemTransactionBinding
import za.co.app.budgetbee.ui.landing.TransactionsListAdapter.TransactionsViewHolder
import za.co.app.budgetbee.utils.displayLongDouble

class TransactionsListAdapter(private val transactions: ArrayList<Transaction>) :
    RecyclerView.Adapter<TransactionsViewHolder>() {
    val publishTransaction = PublishSubject.create<Transaction>()
    lateinit var binding: ItemTransactionBinding
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionsViewHolder {
        binding = ItemTransactionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TransactionsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TransactionsViewHolder, position: Int) {
        holder.display(transactions[position], isLastItem(position))
    }

    private fun isLastItem(position: Int): Boolean {
        return position == itemCount - 1
    }

    override fun getItemCount(): Int {
        return transactions.size
    }

    fun getSelectedTransaction():Observable<Transaction>{
        return publishTransaction.hide()
    }

    inner class TransactionsViewHolder(val binding: ItemTransactionBinding) : RecyclerView.ViewHolder(binding.root) {

        val transactionDate = R.id.text_transaction_amount
        fun display(transaction: Transaction, islastItem: Boolean) {
            binding.apply {
                val isIncome = isIncomeTransaction(transaction)
                textTransactionCategory.text = transaction.transactionCategoryName
                textTransactionDescription.text = transaction.transactionDescription
                textTransactionAmount.text = transaction.transactionAmount.displayLongDouble()
                textPlusMinusSign.setTextColor(itemView.resources.getColor(getTransactionColor(transaction)))
                textPlusMinusSign.text = if (isIncome) {
                    imageArrow.setImageResource(R.drawable.ic_arrow_up)
                   root.context.getString(R.string.plus)
                } else {
                    imageArrow.setImageResource(R.drawable.ic_arrow_down)
                    root.context.getString(R.string.minus)
                }

                transactionDivider.visibility = if (islastItem) View.GONE else View.VISIBLE
                itemView.setOnClickListener {
                    publishTransaction.onNext(transaction)
                }
            }
        }

        private fun getTransactionColor(transaction: Transaction): Int {
            return if (isIncomeTransaction(transaction))
                R.color.green else R.color.red
        }

        private fun isIncomeTransaction(transaction: Transaction): Boolean {
            return (transaction.transactionCategoryType == TransactionCategoryType.INCOME.value)
        }
    }
}