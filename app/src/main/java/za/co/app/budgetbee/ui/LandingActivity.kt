package za.co.app.budgetbee.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_landing.*
import za.co.app.budgetbee.BudgetBeeApplication
import za.co.app.budgetbee.R
import za.co.app.budgetbee.data.model.domain.Transaction
import za.co.app.budgetbee.ui.ILandingMvp.View
import za.co.app.budgetbee.ui.transaction.transactions_category.TransactionCategoryActivity
import javax.inject.Inject

class LandingActivity : AppCompatActivity(), View {

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
        BudgetBeeApplication.instance.feather.injectFields(this)
        displayScreen()
        presenter.start(this)
    }

    private fun buildScreen() {
        val addTransactionButton = add_transaction_fab
        //todo add adapter
        addTransactionButton.setOnClickListener {
            startActivity(TransactionCategoryActivity.getStartIntent(this))
        }
        presenter.getTransactions()
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
            Log.d(
                TAG,
                "displayTransactions size ${transactions.size}- ${transactions[(transactions.size - 1)]}"
            )
        }
    }

    override fun openTransactionCategoryActivity() {
        startActivity(TransactionCategoryActivity.getStartIntent(this))
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
        presenter.stop()
        super.onDestroy()
    }
}