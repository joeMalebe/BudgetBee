package za.co.app.budgetbee.ui.add_transaction

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_add_transaction.*
import za.co.app.budgetbee.R
import za.co.app.budgetbee.base.BaseFragment
import za.co.app.budgetbee.base.BaseObserver
import za.co.app.budgetbee.data.model.domain.TransactionCategory
import za.co.app.budgetbee.data.model.domain.TransactionCategoryType
import za.co.app.budgetbee.data.repository.IDatabaseRepository
import za.co.app.budgetbee.ui.transactions_category.TransactionCategoryListAdapter
import java.lang.ref.WeakReference
import javax.inject.Inject


class AddTransactionFragment(val transactionCategoryType: TransactionCategoryType) :
        BaseFragment() {

    @Inject
    lateinit var transactionsRepository: IDatabaseRepository
    private val compositeDisposable = CompositeDisposable()

    companion object {
        fun newInstance(transactionCategoryType: TransactionCategoryType): AddTransactionFragment {
            return AddTransactionFragment(transactionCategoryType)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_add_transaction, container, false)
        return view
    }

    override fun onResume() {
        super.onResume()
        transactionsRepository.getAllTransactionCategoriesByType(transactionCategoryType.value)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(TransactionCategoryObserver(this))
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.dispose()

    }

    fun buildScreen(transactionCategoryList: List<TransactionCategory>) {
        val incomeRecyclerView = income_category_recyclerView
        incomeRecyclerView.layoutManager = LinearLayoutManager(this.context)

        val transactionCategoryListAdapter =
                TransactionCategoryListAdapter(transactionCategoryList)

        val disposable = transactionCategoryListAdapter.getClickTransactionCategory()
                .subscribe { transactionCategory ->
                    startActivity(AddTransactionActivity.getStartIntent(this.activity?.applicationContext, transactionCategory))
                }
        compositeDisposable.add(disposable)
        incomeRecyclerView.adapter = transactionCategoryListAdapter
    }

    class TransactionCategoryObserver(addTransactionFragment: AddTransactionFragment) :
            BaseObserver<ArrayList<TransactionCategory>>() {
        val fragment = WeakReference(addTransactionFragment).get()

        override fun onNext(value: ArrayList<TransactionCategory>) {
            if (fragment != null) {
                fragment.buildScreen(value)
            }
        }

        override fun onError(error: Throwable) {
            Toast.makeText(
                    fragment?.context,
                    "Error in database lookup ${error.message}",
                    Toast.LENGTH_SHORT
            ).show()
        }
    }
}