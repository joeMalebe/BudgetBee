package za.co.app.budgetbee.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.item_transaction.view.*
import za.co.app.budgetbee.R
import za.co.app.budgetbee.data.model.TransactionCategory

class TransactionCategoryListAdapter(val transactionCategoryList: List<TransactionCategory>) :
    RecyclerView.Adapter<TransactionCategoryListAdapter.TransactionCategoryViewHolder>() {
    val onClickSubject: PublishSubject<TransactionCategory> = PublishSubject.create()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TransactionCategoryViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_transaction, parent, false)
        return TransactionCategoryViewHolder(view)

    }

    override fun getItemCount(): Int {
        return transactionCategoryList.size
    }

    override fun onBindViewHolder(holder: TransactionCategoryViewHolder, position: Int) {
        val transactionCategory = transactionCategoryList.get(position)
        holder.incomeText.text = transactionCategory.transactionCategoryName
        holder.incomeText.setOnClickListener {
            onClickSubject.onNext(transactionCategory)
        }
    }

    fun getClickTransactionCategory(): Observable<TransactionCategory> {
        return onClickSubject.hide()
    }

    class TransactionCategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val incomeText = itemView.item_text_view
    }
}