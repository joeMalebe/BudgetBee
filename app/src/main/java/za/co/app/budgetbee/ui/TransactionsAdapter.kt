package za.co.app.budgetbee.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import za.co.app.budgetbee.R
import za.co.app.budgetbee.data.model.Transaction
import za.co.app.budgetbee.ui.TransactionsAdapter.TransactionsViewHolder

class TransactionsAdapter(val transactions: ArrayList<Transaction>) :
    RecyclerView.Adapter<TransactionsViewHolder>() {

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

    class TransactionsViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val transactionCategoryTextView: TextView =
            view.findViewById(R.id.text_transaction_category)
        val transactionDescriptionTextView: TextView =
            view.findViewById(R.id.text_transaction_description)

        val transactionDate = R.id.text_transaction_amount
        fun display(transaction: Transaction) {
            transactionCategoryTextView.setText(transaction.transactionCategoryId)
            transactionDescriptionTextView.text = transaction.transactionDescription
        }
    }
}