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
import za.co.app.budgetbee.R
import za.co.app.budgetbee.data.model.domain.TransactionCategory
import za.co.app.budgetbee.databinding.ItemAddNewCategoryBinding
import za.co.app.budgetbee.databinding.ItemTransactionCategoryBinding

private const val ADD_NEW_CATEGORY = 1
private const val DEFAULT_VIEW_TYPE = 0
private const val NEW_CATEGORY_VIEW_TYPE = 1

class TransactionCategoryListAdapter(private val transactionCategoryList: List<TransactionCategory>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private lateinit var bindingAddNewCategory: ItemAddNewCategoryBinding
    private lateinit var bindingTransactionCategory: ItemTransactionCategoryBinding
    val onCategorySelectSubject: PublishSubject<TransactionCategory> = PublishSubject.create()
    private val onAddNewCategorySubject: PublishSubject<Context> = PublishSubject.create()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return getViewHolder(viewType, parent)
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

    private fun getViewHolder(viewType: Int, parent: ViewGroup): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return if (viewType == DEFAULT_VIEW_TYPE) {
            bindingTransactionCategory =
                ItemTransactionCategoryBinding.inflate(layoutInflater, parent, false)

            TransactionCategoryViewHolder(
                bindingTransactionCategory
            )
        } else {
            bindingAddNewCategory = ItemAddNewCategoryBinding.inflate(layoutInflater, parent, false)
            AddNewViewHolder(bindingAddNewCategory)
        }
    }

    private fun setupAddNewCategoryClickEvent(addNewViewHolder: AddNewViewHolder) {
        val view = addNewViewHolder.itemView
        val button = view.add_new_category
        button.typeface = ResourcesCompat.getFont(view.context, R.font.poppins_regular)
        button.setOnClickListener {
            onAddNewCategorySubject.onNext(it.context)
        }
    }

    private fun bindTransactionCategoryViewHolder(
        holder: TransactionCategoryViewHolder,
        position: Int
    ) {
        val transactionCategory = transactionCategoryList[position]
        holder.display(transactionCategory)
    }

    fun getSelectedTransactionCategory(): Observable<TransactionCategory> {
        return onCategorySelectSubject.hide()
    }

    fun getAddNewCategoryClick(): Observable<Context> {
        return onAddNewCategorySubject.hide()
    }

    inner class TransactionCategoryViewHolder(private val binding: ItemTransactionCategoryBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun display(transactionCategory: TransactionCategory) {
            binding.itemTextView.text = transactionCategory.transactionCategoryName
            binding.transactionCategoryDivider.visibility =
                if (layoutPosition == itemCount - 2) View.GONE else View.VISIBLE
            binding.root.setOnClickListener {
                onCategorySelectSubject.onNext(transactionCategory)
            }
        }
    }

    class AddNewViewHolder(binding: ItemAddNewCategoryBinding) :
        RecyclerView.ViewHolder(binding.root)
}