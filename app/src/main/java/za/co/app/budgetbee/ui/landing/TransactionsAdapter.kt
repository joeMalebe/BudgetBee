package za.co.app.budgetbee.ui.landing

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import za.co.app.budgetbee.R
import za.co.app.budgetbee.data.model.domain.Transaction
import za.co.app.budgetbee.ui.landing.TransactionsAdapter.TransactionsViewHolder

class TransactionsAdapter(private val transactions: ArrayList<Transaction>) :
    RecyclerView.Adapter<TransactionsViewHolder>() {
    val publishTransaction = PublishSubject.create<Transaction>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionsViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_transaction, parent, false)
        return TransactionsViewHolder(view)
    }

    override fun onBindViewHolder(holder: TransactionsViewHolder, position: Int) {
        holder.display(transactions.get(position))
    }

    override fun getItemCount(): Int {
        return transactions.size
    }

    fun getSelectedTransaction():Observable<Transaction>{
        return publishTransaction.hide()
    }

    inner class TransactionsViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val transactionCategoryTextView: TextView =
            view.findViewById(R.id.text_transaction_category)
        val transactionDescriptionTextView: TextView =
            view.findViewById(R.id.text_transaction_description)
        val transactionAmountTextView: TextView =
            view.findViewById(R.id.text_transaction_amount)

        val transactionDate = R.id.text_transaction_amount
        fun display(transaction: Transaction) {
            transactionCategoryTextView.text = transaction.transactionCategoryName
            transactionDescriptionTextView.text = transaction.transactionDescription
            transactionAmountTextView.text = transaction.transactionAmount.toString()
            itemView.setOnClickListener {
                publishTransaction.onNext(transaction)
            }
        }
    }
}