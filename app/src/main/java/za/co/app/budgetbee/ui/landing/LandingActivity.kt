package za.co.app.budgetbee.ui.landing

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_landing.*
import za.co.app.budgetbee.R
import za.co.app.budgetbee.base.AppCompatBaseActivity
import za.co.app.budgetbee.data.model.domain.Transaction
import za.co.app.budgetbee.ui.landing.ILandingMvp.View
import za.co.app.budgetbee.ui.transactions_category.TransactionCategorySelectCategoryActivity
import javax.inject.Inject

class LandingActivity : AppCompatBaseActivity(), View {

    val TAG = LandingActivity::class.simpleName

    @Inject
    lateinit var presenter: ILandingMvp.Presenter

    companion object {
        fun getStartIntent(context: Context): Intent {
            val intent = Intent(context, LandingActivity::class.java)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_landing)
        presenter.attachView(this)
        /*displayScreen()*/
    }

    override fun onResume() {
        super.onResume()
        displayScreen()
    }

    override fun showLoading() {
        Log.d(TAG, "showLoading")
    }

    override fun dismissLoading() {
        Log.d(TAG, "dismissLoading")
    }

    override fun showError(error: Throwable) {
        Log.e(TAG, "showError")
    }

    override fun displayTransactions(transactions: ArrayList<Transaction>) {
        dismissLoading()
        if (transactions.isNotEmpty()) {
            //todo add adapter
            val adapter = TransactionsAdapter(transactions)
            recycler_transactions.adapter = adapter
            recycler_transactions.layoutManager = LinearLayoutManager(this)
            recycler_transactions.isNestedScrollingEnabled = false
            Log.d(
                TAG,
                "displayTransactions size ${transactions.size}- ${transactions[(transactions.size - 1)]}"
            )
        }
    }

    override fun openTransactionCategoryActivity() {
        startActivity(TransactionCategorySelectCategoryActivity.getStartIntent(this))
    }

    override fun displayScreen() {
        showLoading()
        presenter.getTransactions()
        val addTransactionButton = add_transaction_fab
        addTransactionButton.setOnClickListener {
            openTransactionCategoryActivity()
        }
    }

    override fun onDestroy() {
        presenter.detachView()
        super.onDestroy()
    }
}