package za.co.app.budgetbee.ui.transaction

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_add_income.*
import za.co.app.budgetbee.BudgetBeeApplication
import za.co.app.budgetbee.R
import za.co.app.budgetbee.base.BaseObserver
import za.co.app.budgetbee.data.model.domain.TransactionCategory
import za.co.app.budgetbee.data.repository.TransactionsRepository
import za.co.app.budgetbee.ui.transactions_category.TransactionCategoryListAdapter
import java.lang.ref.WeakReference
import javax.inject.Inject


class AddIncomeFragment : Fragment() {

    @Inject
    lateinit var transactionsRepository: TransactionsRepository

    companion object {
        fun newInstance(): AddIncomeFragment {
            return AddIncomeFragment()
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        BudgetBeeApplication.instance.feather.injectFields(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_add_income, container, false)

        return view
    }

    override fun onResume() {
        super.onResume()
        transactionsRepository.getAllTransactionCategories()
            .subscribeOn(
                Schedulers.io()
            ).observeOn(AndroidSchedulers.mainThread()).subscribe(
                TransactionCategoryObserver(
                    this
                )
            )
    }

    fun buildScreen(transactionCategoryList: List<TransactionCategory>) {
        val incomeRecyclerView = income_category_recyclerView
        incomeRecyclerView.layoutManager = LinearLayoutManager(this.context)

        val transactionCategoryListAdapter =
            TransactionCategoryListAdapter(
                transactionCategoryList
            )

        transactionCategoryListAdapter.getClickTransactionCategory()
            .subscribe { transactionCategory ->
                startActivity(
                    AddTransactionActivity.getStartIntent(
                        this.activity?.applicationContext,
                        transactionCategory
                    )
                )
            }

        incomeRecyclerView.adapter = transactionCategoryListAdapter
    }

    class TransactionCategoryObserver(addIncomeFragment: AddIncomeFragment) :
        BaseObserver<ArrayList<TransactionCategory>>() {
        val fragment = WeakReference(addIncomeFragment).get()

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

}// Required empty public constructor

