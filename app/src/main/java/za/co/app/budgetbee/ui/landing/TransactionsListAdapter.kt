package za.co.app.budgetbee.ui.landing

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.item_transaction.view.*
import za.co.app.budgetbee.R
import za.co.app.budgetbee.data.model.domain.Transaction
import za.co.app.budgetbee.data.model.domain.TransactionCategoryType
import za.co.app.budgetbee.ui.landing.TransactionsListAdapter.TransactionsViewHolder
import za.co.app.budgetbee.utils.displayLongDouble

class TransactionsListAdapter(private val transactions: ArrayList<Transaction>) :
    RecyclerView.Adapter<TransactionsViewHolder>() {
    val publishTransaction = PublishSubject.create<Transaction>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionsViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_transaction, parent, false)
        return TransactionsViewHolder(view)
    }

    override fun onBindViewHolder(holder: TransactionsViewHolder, position: Int) {
        holder.display(transactions.get(position), isLastItem(position))
    }

    private fun isLastItem(position: Int): Boolean {
        return position == getItemCount() - 1
    }

    override fun getItemCount(): Int {
        return transactions.size
    }

    fun getSelectedTransaction():Observable<Transaction>{
        return publishTransaction.hide()
    }

    inner class TransactionsViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val transactionCategoryTextView: TextView =
            view.findViewById(R.id.text_transaction_category)
        val transactionDescriptionTextView: TextView =
            view.findViewById(R.id.text_transaction_description)
        val transactionAmountTextView: TextView =
            view.findViewById(R.id.text_transaction_amount)
        val divider: View = view.findViewById(R.id.transaction_divider)

        val transactionDate = R.id.text_transaction_amount
        fun display(transaction: Transaction, islastItem: Boolean) {
            view.apply {
                val isIncome = isIncomeTransaction(transaction)
                transactionCategoryTextView.text = transaction.transactionCategoryName
                transactionDescriptionTextView.text = transaction.transactionDescription
                transactionAmountTextView.text = transaction.transactionAmount.displayLongDouble()
                text_plus_minus_sign.setTextColor(itemView.resources.getColor(getTransactionColor(transaction)))
                text_plus_minus_sign.text = if (isIncome) {
                    image_arrow.setImageResource(R.drawable.ic_arrow_up)
                    context.getString(R.string.plus)
                } else {
                    image_arrow.setImageResource(R.drawable.ic_arrow_down)
                    context.getString(R.string.minus)
                }

                divider.visibility = if (islastItem) View.GONE else View.VISIBLE
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