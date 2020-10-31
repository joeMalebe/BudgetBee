package za.co.app.budgetbee.ui.add_transaction.select_transaction_category

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.fragment_add_transaction.*
import za.co.app.budgetbee.R
import za.co.app.budgetbee.base.BaseFragment
import za.co.app.budgetbee.data.model.domain.TransactionCategory
import za.co.app.budgetbee.data.model.domain.TransactionCategoryType
import za.co.app.budgetbee.ui.add_transaction.AddTransactionActivity.Companion.EXTRA_TRANSACTION_CATEGORY
import za.co.app.budgetbee.ui.transactions_category.TransactionCategoryListAdapter
import java.util.*
import javax.inject.Inject


class SelectTransactionCategoryFragment(val transactionCategoryType: TransactionCategoryType) :
        BaseFragment(), ISelectTransactionCategoryMvp.View {

    val TAG = SelectTransactionCategoryFragment::class.simpleName

    @Inject
    lateinit var presenter: ISelectTransactionCategoryMvp.Presenter

    private val compositeDisposable = CompositeDisposable()

    companion object {
        fun newInstance(transactionCategoryType: TransactionCategoryType): SelectTransactionCategoryFragment {
            return SelectTransactionCategoryFragment(transactionCategoryType)
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        presenter.attachView(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_add_transaction, container, false)
        return view
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        displayScreen()
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.dispose()
        presenter.detachView()
    }

    fun buildScreen(transactionCategoryList: List<TransactionCategory>) {
        val incomeRecyclerView = income_category_recyclerView
        incomeRecyclerView.layoutManager = LinearLayoutManager(this.context)

        val transactionCategoryListAdapter =
                TransactionCategoryListAdapter(transactionCategoryList)

        transactionCategoryListAdapter.getClickTransactionCategory()

                .subscribe { transactionCategory ->
                    val intent = Intent()
                    intent.putExtra(EXTRA_TRANSACTION_CATEGORY,transactionCategory)
                    val TRANSACTION_CATEGORY = 2
                    activity?.setResult(TRANSACTION_CATEGORY, intent)
                    activity?.finish()
                }.let { compositeDisposable.add(it) }
        incomeRecyclerView.adapter = transactionCategoryListAdapter
    }

    override fun displayCategories(transactionCategories: ArrayList<TransactionCategory>) {
        buildScreen(transactionCategories)
    }

    override fun showError() {
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
}