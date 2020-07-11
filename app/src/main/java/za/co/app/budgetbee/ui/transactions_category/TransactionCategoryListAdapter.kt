package za.co.app.budgetbee.ui.transactions_category

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.item_transaction_category.view.*
import za.co.app.budgetbee.R
import za.co.app.budgetbee.data.model.domain.TransactionCategory

class TransactionCategoryListAdapter(val transactionCategoryList: List<TransactionCategory>) :
    RecyclerView.Adapter<TransactionCategoryListAdapter.TransactionCategoryViewHolder>() {
    private val ADD_NEW_CATEGORY = 1
    private val DEFAULT_VIEW_TYPE = 0
    private val NEW_CATEGORY_VIEW_TYPE = 1
    val onClickSubject: PublishSubject<TransactionCategory> = PublishSubject.create()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TransactionCategoryViewHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_transaction_category, parent, false)
        return TransactionCategoryViewHolder(
            view
        )

    }

    override fun getItemViewType(position: Int): Int {
        return if (position == transactionCategoryList.size) NEW_CATEGORY_VIEW_TYPE else DEFAULT_VIEW_TYPE
    }

    override fun getItemCount(): Int {
        return transactionCategoryList.size + ADD_NEW_CATEGORY
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

    class AddNewViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}