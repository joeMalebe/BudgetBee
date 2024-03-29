package za.co.app.budgetbee.ui.select_transaction_category

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.reactivex.disposables.CompositeDisposable
import za.co.app.budgetbee.base.BaseFragment
import za.co.app.budgetbee.data.model.domain.TransactionCategory
import za.co.app.budgetbee.data.model.domain.TransactionCategoryType
import za.co.app.budgetbee.databinding.FragmentAddTransactionBinding
import za.co.app.budgetbee.ui.add_transaction.AddTransactionActivity.Companion.EXTRA_TRANSACTION_CATEGORY
import za.co.app.budgetbee.ui.transactions_category.AddTransactionCategoryActivity
import za.co.app.budgetbee.ui.transactions_category.TransactionCategoryListAdapter

private const val TRANSACTION_CATEGORY = 2

class SelectTransactionCategoryFragment(
    val transactionCategoryType: TransactionCategoryType,
    private val transactionCategoryList: MutableList<TransactionCategory>,
    var presenter: ISelectTransactionCategoryMvp.Presenter
) :
    BaseFragment(), ISelectTransactionCategoryMvp.View {

    private lateinit var binding: FragmentAddTransactionBinding
    private lateinit var incomeRecyclerView: RecyclerView
    val TAG = SelectTransactionCategoryFragment::class.simpleName

    private val compositeDisposable = CompositeDisposable()

    companion object {
        fun newInstance(
            transactionCategoryType: TransactionCategoryType,
            presenter: ISelectTransactionCategoryMvp.Presenter
        ): SelectTransactionCategoryFragment {
            return SelectTransactionCategoryFragment(
                transactionCategoryType,
                mutableListOf(),
                presenter
            )
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentAddTransactionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.attachView(this)
        incomeRecyclerView = binding.incomeCategoryRecyclerView
        incomeRecyclerView.layoutManager = LinearLayoutManager(this.context)
        displayScreen()
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.dispose()
        presenter.detachView()
    }

    private fun buildScreen() {
        val transactionCategoryListAdapter =
            TransactionCategoryListAdapter(transactionCategoryList)

        transactionCategoryListAdapter.getSelectedTransactionCategory()
            .subscribe { transactionCategory ->
                val intent = Intent()
                intent.putExtra(EXTRA_TRANSACTION_CATEGORY, transactionCategory)

                activity?.setResult(TRANSACTION_CATEGORY, intent)
                activity?.finish()
            }.let { compositeDisposable.add(it) }

        transactionCategoryListAdapter.getAddNewCategoryClick()
            .subscribe {
                startActivityForResult(AddTransactionCategoryActivity.getStartIntent(it), 2)
            }.let { compositeDisposable.add(it) }
        incomeRecyclerView.adapter = transactionCategoryListAdapter
    }

    override fun displayCategories(transactionCategories: ArrayList<TransactionCategory>) {
        val adapter = incomeRecyclerView.adapter
        if (adapter == null) {
            this.transactionCategoryList.addAll(transactionCategories.sortedBy { it.transactionCategoryName }
                .toMutableList())
            buildScreen()
        } else {
            updateTransactionsCategories(transactionCategories, adapter)
        }
    }

    private fun updateTransactionsCategories(
        transactionCategories: ArrayList<TransactionCategory>,
        adapter: RecyclerView.Adapter<*>
    ) {
        transactionCategoryList.clear()
        transactionCategoryList.addAll(transactionCategories.sortedBy { it.transactionCategoryName })
        adapter.notifyDataSetChanged()
    }

    override fun showError(error: Throwable) {
        Log.d(TAG, "showError() called")
    }

    override fun showLoading() {
        Log.d(TAG, "showLoading() called")
    }

    override fun dismissLoading() {
        Log.d(TAG, "dismissLoading() called")
    }

    override fun displayScreen() {
        showLoading()
        presenter.getAllTransactionCategoriesByType(transactionCategoryType)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        displayScreen()
    }
}