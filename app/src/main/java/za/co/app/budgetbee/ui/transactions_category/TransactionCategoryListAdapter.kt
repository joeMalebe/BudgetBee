package za.co.app.budgetbee.ui.transactions_category

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.item_add_new_category.view.*
import kotlinx.android.synthetic.main.item_transaction_category.view.*
import za.co.app.budgetbee.R
import za.co.app.budgetbee.data.model.domain.TransactionCategory

class TransactionCategoryListAdapter(val transactionCategoryList: List<TransactionCategory>) :
        RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val ADD_NEW_CATEGORY = 1
    private val DEFAULT_VIEW_TYPE = 0
    private val NEW_CATEGORY_VIEW_TYPE = 1
    val onCategorySelectSubject: PublishSubject<TransactionCategory> = PublishSubject.create()
    val onAddNewCategorySubject: PublishSubject<Context> = PublishSubject.create()

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
            setupAddNewCategoryClickEvent(holder as AddNewViewHolder)
        } else {
            bindTransactionCategoryViewHolder(holder as TransactionCategoryViewHolder, position)
        }
    }

    fun getView(viewType: Int, parent: ViewGroup): View {
        val layoutInflater = LayoutInflater.from(parent.context)
        return if (viewType == DEFAULT_VIEW_TYPE) layoutInflater
                .inflate(R.layout.item_transaction_category, parent, false) else layoutInflater
                .inflate(R.layout.item_add_new_category, parent, false)
    }

    fun getViewHolder(viewType: Int, view: View): RecyclerView.ViewHolder {
        return if (viewType == DEFAULT_VIEW_TYPE) TransactionCategoryViewHolder(view) else AddNewViewHolder(view)
    }

    private fun setupAddNewCategoryClickEvent(addNewViewHolder: AddNewViewHolder) {
        val view = addNewViewHolder.itemView
        val button = view.add_new_category
                button.typeface = ResourcesCompat.getFont(view.context, R.font.poppins_regular)
        button.setOnClickListener {
            onAddNewCategorySubject.onNext(it.context)
        }
    }

    fun bindTransactionCategoryViewHolder(holder: TransactionCategoryViewHolder, position: Int) {
        val transactionCategory = transactionCategoryList.get(position)

        holder.incomeText.text = transactionCategory.transactionCategoryName
        holder.divider.visibility = if (position == itemCount - 2) View.GONE else View.VISIBLE
        holder.incomeText.setOnClickListener {}
        holder.itemView.setOnClickListener {
            onCategorySelectSubject.onNext(transactionCategory)
        }
    }

    fun getSelectedTransactionCategory(): Observable<TransactionCategory> {
        return onCategorySelectSubject.hide()
    }

    fun getAddNewCategoryClick(): Observable<Context> {
        return onAddNewCategorySubject.hide()
    }

    class TransactionCategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val incomeText = itemView.item_text_view
        val divider = itemView.transaction_category_divider
    }

    class AddNewViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}