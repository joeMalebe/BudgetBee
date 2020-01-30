package za.co.app.budgetbee.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_add_income.*
import za.co.app.budgetbee.R
import za.co.app.budgetbee.data.model.BudgetBeeDatabase
import za.co.app.budgetbee.data.model.TransactionCategory
import java.lang.ref.WeakReference


class AddIncomeFragment : Fragment() {

    companion object {
        fun newInstance(): AddIncomeFragment {
            return AddIncomeFragment()
        }
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
        BudgetBeeDatabase.getInstance(this.requireContext()).getTransactionCategoryDao()
            .getAllTransactionCategories()
            .subscribeOn(
                Schedulers.io()
            ).observeOn(AndroidSchedulers.mainThread()).subscribe(TransactionCategoryObserver(this))
    }

    fun buildScreen(transactionCategoryList: List<TransactionCategory>) {
        val incomeRecyclerView = income_category_recyclerView
        incomeRecyclerView.layoutManager = LinearLayoutManager(this.context)
        val transactionCategoryListAdapter = TransactionCategoryListAdapter(transactionCategoryList)
        transactionCategoryListAdapter.getClickTransactionCategory()
            .subscribe({ transactionCategory ->
                startActivity(
                    AddTransactionActivity.getStartIntent(
                        this.activity?.applicationContext,
                        transactionCategory
                    )
                )
            })

        incomeRecyclerView.adapter = transactionCategoryListAdapter


    }

    class TransactionCategoryObserver(addIncomeFragment: AddIncomeFragment) :
        Observer<List<TransactionCategory>> {
        val fragment = WeakReference(addIncomeFragment).get()
        override fun onComplete() {
            //do nothing
        }

        override fun onSubscribe(d: Disposable) {
        }

        override fun onNext(transactionCategoryList: List<TransactionCategory>) {
            if (fragment != null) {
                fragment.buildScreen(transactionCategoryList)
            }
        }

        override fun onError(e: Throwable) {
            Toast.makeText(
                fragment?.context,
                "Error in database lookup ${e.message}",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

}// Required empty public constructor

