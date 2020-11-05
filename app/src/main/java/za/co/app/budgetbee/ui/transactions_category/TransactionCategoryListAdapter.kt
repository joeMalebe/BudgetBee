package za.co.app.budgetbee.ui.transactions_category

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.item_add_new.view.*
import kotlinx.android.synthetic.main.item_transaction_category.view.*
import za.co.app.budgetbee.R
import za.co.app.budgetbee.data.model.domain.TransactionCategory

class TransactionCategoryListAdapter(val transactionCategoryList: List<TransactionCategory>) :
        RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val ADD_NEW_CATEGORY = 1
    private val DEFAULT_VIEW_TYPE = 0
    private val NEW_CATEGORY_VIEW_TYPE = 1
    val onClickSubject: PublishSubject<TransactionCategory> = PublishSubject.create()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = getView(viewType, parent)
        return getViewHolder(viewType, view)
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == transactionCategoryList.size) NEW_CATEGORY_VIEW_TYPE else DEFAULT_VIEW_TYPE
    }

    override fun getItemCount(): Int {
        return transactionCategoryList.size + ADD_NEW_CATEGORY
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (position == transactionCategoryList.size) {
            setupAddNewClickEvent(holder as AddNewViewHolder)
        } else {
            bindTransactionCategoryViewHolder(holder as TransactionCategoryViewHolder, position)
        }
    }

    fun getView(viewType: Int, parent: ViewGroup): View {
        val layoutInflater = LayoutInflater.from(parent.context)
        return if (viewType == DEFAULT_VIEW_TYPE) layoutInflater
                .inflate(R.layout.item_transaction_category, parent, false) else layoutInflater
                .inflate(R.layout.item_add_new, parent, false)
    }

    fun getViewHolder(viewType: Int, view: View): RecyclerView.ViewHolder {
        return if (viewType == DEFAULT_VIEW_TYPE) TransactionCategoryViewHolder(view) else AddNewViewHolder(view)
    }

    private fun setupAddNewClickEvent(addNewViewHolder: AddNewViewHolder) {
        addNewViewHolder.addNewLayout.setOnClickListener {
            it.context.startActivity(AddTransactionCategoryActivity.getStartIntent(it.context))
        }
    }

    fun bindTransactionCategoryViewHolder(holder: TransactionCategoryViewHolder, position: Int) {
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

    class AddNewViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val addNewLayout = itemView.add_new_layout
    }
}